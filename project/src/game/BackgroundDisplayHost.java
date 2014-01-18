package game;
import graphics.Bullet;
import graphics.BulletType;
import graphics.Enemy;
import graphics.Player;
import graphics.Ship;

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Cette classe gète l'affichage et le rafraichissement des images pendant la partie.
 * Les images sont rafraichies toutes les 16ms, et le MouseListener permet de tirer sur les vaisseaux ennemis.
 * Les balles sortant de l'écran sont automatiquement supprimées (ou plutôt, l'espace contenant l'information est
 * remis à disposition pour la création de nouvelles balles).
 * @author Florian Nguyen
 *
 */
public class BackgroundDisplayHost extends JPanel implements ActionListener, Runnable{

	private static final int LOWERLIMIT = 40; //LIMITE BASSE EN DESSOUS DE LAQUELLE LE PLAYER NE PEUT PAS ALLER
	private static final int RIGHTLIMIT = 5; // LIMITE DROITE (limite gauche fonctionnelle par défaut)
	public static final int DEFAULT_FREQUENCY=10; // TEMPS DE RAFRAICHISSEMENT DES IMAGES
	public static final int BOSSFREQUENCY = 5; // NOMBRE DE NIVEAUX ENTRE CHAQUE BOSS
	public static BufferedImage background; // IMAGE SERVANT DE FOND D'ECRAN DEFILANT
	public static BufferedImage friendSprite = Ship.FRIEND.getSprite(); // SPRITE TRANSPARENT
	public static BufferedImage mySprite = Ship.BASIC_PLAYER.getSprite(); // SPRITE PLEIN3
	public static final int[][] levels = // NIVEAUX ET VAGUES DE VAISSEAUX PAR NIVEAU
		{
		{1,0,0}, //niveau 1, 3 vagues...
		{2,0,0}, //niveau 2
		{0,1,0}, //niveau 3
		{0,2,0},  // etc
		{0,0,1}};
	
	// SEMAPHORE POUR LA SYNCHRONISATION AVEC LE SERVEUR
	public Semaphore canIterate = new Semaphore(1);

	private Player player1,player2; //JOUEUR
	private int height,width,level; // HAUTEUR, LARGEUR DE LA FENETRE, ET NIVEAU ACTUEL
	private int Y; // VARIABLE SERVANT AU DEFILEMENT DU BACKGROUND
	private long lastNextTime; // DERNIER TEMPS DE CHANGEMENT DE NIVEAU
	private boolean notSpawnedYet; // VAGUE DE CE NIVEAU DEJA SPAWNEE OU NON
	private long lastSpawnTime; // TEMPS DE DERNIER SPAWN
	private boolean isOver,enablePlayer2; // GAME OVER, PLAYER2 AUTORISE
	private boolean multiOn; // MULTIJOUEUR ACTIVE
	private int needRemove;
	private boolean needShooting;

	private JFrame frame;
	private Mouse mouse = new Mouse(); // MOUSELISTENER PERMETTANT DE CONTROLER LE JOUEUR
	private BallManagement pBalls,eBalls; // OBJETS BULLET A PEINDRE A L'INSTANT T (BULLET PLAYER OU ENEMY)
	private volatile ArrayList<Enemy> enemies = new ArrayList<Enemy>(0);
	private java.util.Timer enemyTimer = new java.util.Timer();
	private javax.swing.Timer t = new javax.swing.Timer(DEFAULT_FREQUENCY,this); // TIMER POUR LE RAFRAICHISSEMENT DES IMAGES
	private long time = System.currentTimeMillis();

	/**
	 * Constructeur de la classe BackgroundDisplay.
	 * @param f JFrame dans lequel l'on souhaite réaliser l'affichage.
	 * @param sourcename Source du fichier image (png) qui sera utilisé en fond d'écran.
	 * L'image doit être cyclique, c'est à dire que la partie haute et la partie basse sont compatibles.
	 */
	public BackgroundDisplayHost(String name, boolean multiOn, String sourcename)
	{
		try {
			// INITIALISATIONS
			player1 = new Player(name,false);
			frame = new JFrame();
			notSpawnedYet=true;
			needShooting=false;
			lastSpawnTime = 0;
			needRemove=-1;
			background = ImageIO.read(new File(sourcename));
			height = background.getHeight();
			width = background.getWidth();
			level = -1;
			isOver = false;
			this.multiOn = multiOn;
			pBalls = new BallManagement();
			eBalls = new BallManagement();

			// CAS DU MULTIJOUEUR
			player2 = new Player("P2",false);
			enablePlayer2 = false;

			// OPTIONS DU JFRAME
			frame.addMouseListener(mouse);
			frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
					new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB),
					new Point(0,0),"Blank cursor"));
			frame.setSize(this.getWidth(),(int)(this.getHeight()*1));
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.add(this);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);

			// LANCEMENT DU TIMER
			t.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retourne le Player du joueur 2.
	 * @return le Player du joueur 2.
	 */
	public Player getPlayer2()
	{
		return player2;
	}
	
	/**
	 * Retourne le Player du joueur 1.
	 * @return le Player du joueur 1
	 */
	public Player getPlayer1()
	{
		return player1;
	}

	/**
	 * Dit si la partie est finie ou non.
	 * @return true si Game Over, false sinon
	 */
	public boolean isOver()
	{
		return isOver;
	}

	/**
	 * Méthode utilisée par défaut par JPanel.
	 */
	public void paint(Graphics g) {
		super.paint(g);

		// DEPLACEMENT DE L'ORDONNEE A LAQUELLE PEINDRE LE BACKGROUND
		Y+=1; 
		for(int i=(Y%background.getHeight())-background.getHeight();i<background.getHeight();i+=background.getHeight()) {
			g.drawImage(background,0,i,this);
		}

		if(!multiOn || (multiOn && enablePlayer2))
		{
			// RAFRAICHISSEMENT DES OBJETS ENEMY
			for(Enemy e:enemies)
			{
				g.drawImage(
						e.getSprite(),
						e.getSpriteX(),
						e.getSpriteY(),
						this);
			}

			// RAFRAICHISSEMENT DES BULLET PLAYER S'IL Y EN A
			if(!pBalls.isEmpty())
			{
				synchronized(pBalls.getBalls()) {
					ArrayList<Bullet> toDraw = pBalls.getBalls();
					for(int i=0;i<toDraw.size();i++)
					{
						g.drawImage(
								toDraw.get(i).getSprite(),
								toDraw.get(i).getSpriteX(),
								toDraw.get(i).getSpriteY(),
								this);
					}
				}
			}

			// RAFRAICHISSEMENT DES BULLETS ENEMY S'IL Y EN A
			if(!eBalls.isEmpty())
			{
				synchronized(eBalls.getBalls()) {
					ArrayList<Bullet> toDraw = eBalls.getBalls();
					eBalls.update();
					for(int i=0;i<toDraw.size();i++)
					{
						g.drawImage(
								toDraw.get(i).getSprite(),
								toDraw.get(i).getSpriteX(),
								toDraw.get(i).getSpriteY(),
								this);
					}
				}
			}
		}


		// RAFFRAICHISSEMENT DE LA POSITION DU JOUEUR 1
		if(player1.getLife()>0)
		{
			g.drawImage(
					mySprite,
					player1.getSpriteX(),
					player1.getSpriteY(), //centrage souris sur la hit-case
					this);
		}

		// EVENTUELLES DONNEES LIEES A UN JOUEUR 2
		if(enablePlayer2)
		{
			// RAFFRAICHISSEMENT DE LA POSITION DU JOUEUR 2
			if(player2.getLife()>0)
			{
				g.drawImage(
						friendSprite,
						player2.getSpriteX(),
						player2.getSpriteY(), //centrage souris sur la hit-case
						this);
			}
		}
		g.dispose();
	}

	/**
	 * Retourne le score
	 * @return le score
	 */
	public int getScore()
	{
		return player1.getScore();
	}

	/**
	 * Retourne le niveau
	 * @return le niveau
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * Met needRemove à la valeur désirée.
	 * @param index la nouvelle valeur de needRemove
	 */
	public void setNeedRemove(int index)
	{
		needRemove=index;
	}

	/**
	 * Retourne needRemove.
	 * @return needRemove
	 */
	public int getNeedRemove()
	{
		return needRemove;
	}

	/**
	 * Retourne needShooting
	 * @return needShooting
	 */
	public boolean needShooting()
	{
		return needShooting;
	}
	
	/**
	 * Met needShooting à la valeur désirée
	 * @param b la nouvelle valeur de needShooting
	 */
	public void setNeedShooting(boolean b)
	{
		needShooting=b;
	}
	
	/**
	 * Définit l'action réalisée en rythme avec le Timer. 
	 * Il faut notamment gérer les cas de figure où la souris sort de la fenêtre de jeu,
	 * pour donner au Player les bonnes positions dans chaque cas.
	 */
	public void actionPerformed(ActionEvent e) {

		canIterate.tryAcquire();
		// MISE A JOUR DES BALLES 
		pBalls.update();

		//GESTION DU JOUEUR EN FONCTION DU POINTEUR SOURIS
		player1.setLevel(level);
		Point point = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(point, this);
		if(point.x>0 && point.x<background.getWidth()-RIGHTLIMIT && point.y>0 && point.y<background.getHeight()-LOWERLIMIT)
		{
			player1.setXY(point.x,point.y);
		}
		else if(point.x<0)
		{
			if(point.y<0){player1.setXY(0,0);}
			else if(point.y>background.getHeight()-LOWERLIMIT){player1.setXY(0,background.getHeight()-LOWERLIMIT);}
			else player1.setXY(0,point.y);
		}
		else if(point.x>background.getWidth()-RIGHTLIMIT)
		{
			if(point.y<0){player1.setXY(background.getWidth()-RIGHTLIMIT,0);}
			else if(point.y>background.getHeight()-LOWERLIMIT){player1.setXY(background.getWidth()-RIGHTLIMIT,background.getHeight()-LOWERLIMIT);}
			else player1.setXY(background.getWidth()-RIGHTLIMIT,point.y);
		}
		else if(point.y<0)
		{
			if(point.x<0){player1.setXY(0,0);}
			else if(point.x>background.getWidth()-RIGHTLIMIT){player1.setXY(background.getWidth()-RIGHTLIMIT,0);}
			else player1.setXY(point.x,0);
		}
		else if(point.y>background.getHeight()-LOWERLIMIT)
		{
			if(point.x<0){player1.setXY(0,background.getHeight()-LOWERLIMIT);}
			else if(point.x>background.getWidth()-RIGHTLIMIT){player1.setXY(background.getWidth()-RIGHTLIMIT,background.getHeight()-LOWERLIMIT);}
			else player1.setXY(point.x,background.getHeight()-LOWERLIMIT);
		}

		// PARTIE RELATIVE AU TIR DU VAISSEAU PLAYER
		if(mouse.get()==true)
		{
			needShooting=true;
			player1.primaryShooting(pBalls); // primaryShooting prend en compte le temps de rechargement
		}

		// INTERACTION PLAYERS/BULLETS ENNEMIES
		player1.getHitBy(eBalls,level);
		if(enablePlayer2)
		{
			player2.getHitBy(eBalls, level);
			player2.setXP(player1.getXP());
			player2.setScore(player1.getScore());
		}

		// GESTION DES VAISSEAUX ABATTUS ET VIVANTS
		for(int i=0;i<enemies.size();i++)
		{
			Enemy enemy = enemies.get(i);
			if(enemy.isAlive() && isInScreen(enemy.getX(),enemy.getY(),enemy.getShip()))
			{
				enemy.update();
				enemy.shoot(eBalls);
				enemy.getHitBy(pBalls,level); //getHitBy() doit tenir compte du niveau pour que la difficulté augmente
			}
			if(!enemy.isAlive())
			{
				player1.addToScore(enemy.getShip().getScore(),level); // J1 détient le score, et pas J2
				setNeedRemove(enemy.getLevelIndex());
				enemies.remove(enemy);
			}
		}
		if(!multiOn || (multiOn && enablePlayer2))
		{
			next();
		}
		if(player1.getLife()<0 && player2.getLife()<0)
		{
			isOver=true;
		}
		canIterate.release();
		repaint();
	}

	/**
	 * Retourne les balles Player 1&2.
	 * @return les balles tirées par les joueurs
	 */
	public BallManagement getPlayerBalls()
	{
		return pBalls;
	}

	/**
	 * Dit si à la position (x,y) donnée, on se trouve dans l'écran.
	 * Cette méthode étant utilisée pour les Bullet, il faut tenir compte de la taille du sprite de la munition
	 * dont on évalue si oui on non elle est complètement sortie de la fenetre.
	 * @param x Coordonnée X du Bullet
	 * @param y Coordonnée Y du Bullet
	 * @param id ID du BulletType
	 * @return true si dans l'écran, false sinon
	 */
	public boolean isInScreen(int x, int y, int id)
	{
		boolean p=true;
		if(x<-BulletType.getFromID(id).getSprite().getWidth()||x>background.getWidth()+BulletType.getFromID(id).getSprite().getWidth()||
				y<-BulletType.getFromID(id).getSprite().getHeight()||y>background.getHeight()+BulletType.getFromID(id).getSprite().getHeight())
		{
			p=false;
		}
		return p;
	}

	/**
	 * Variante pour les vaisseaux.
	 * @param x X du vaisseau
	 * @param y Y du vaisseau
	 * @param ship Vaisseau à évaluer
	 * @return true si dans la fenêtre, false sinon
	 */
	public boolean isInScreen(int x, int y, Ship ship)
	{
		boolean p=true;
		if(x<-ship.getSprite().getWidth()||x>background.getWidth()+ship.getSprite().getWidth()||
				y<-ship.getSprite().getHeight()||y>background.getHeight()+ship.getSprite().getHeight())
		{
			p=false;
		}
		return p;
	}

	/**
	 * Méthode isInScreen prenant directement le Bullet en variable
	 * @param b Bullet à observer
	 * @return true si dans la fenetre, non sinon
	 */
	public boolean isInScreen(Bullet b)
	{
		boolean p=true;
		if(b.getX()<-b.getSprite().getWidth()||b.getX()>background.getWidth()+b.getSprite().getWidth()||
				b.getY()<-b.getSprite().getHeight()||b.getY()>background.getHeight()+b.getSprite().getHeight())
		{
			p=false;
		}
		return p;
	}

	/**
	 * Renvoie la hauteur de l'image servant de background, et donc du background.  
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Renvoie la largeur du background/de l'image servant de background.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Retourne les balles ennemies.
	 * @return les balles ennemies
	 */
	public ArrayList<Enemy> getEnemies()
	{
		return enemies;
	}

	/**
	 * Modifie les coordonnées du Player2.
	 * @param a X
	 * @param b Y
	 */
	public void setPlayer2XY(int a, int b)
	{
		player2.setXY(a,b);
	}

	/**
	 * Autorise le début de partie quand toutes les conditions sont remplies.
	 */
	public void enableMultiplayer()
	{
		enablePlayer2=true;
	}

	/**
	 * Méthode permettant de passer aux vagues et aux niveaux suivants
	 */
	public void next()
	{
		if(level==-1)
		{
			if(System.currentTimeMillis()-lastNextTime>500)
			{
				level++;
				lastNextTime = System.currentTimeMillis();
				lastSpawnTime = lastNextTime;
			}
		}
		else if((level)%(BOSSFREQUENCY)!=BOSSFREQUENCY-1) // NIVEAUX AUTRES QUE BOSS (commence à 1)
		{
			if(System.currentTimeMillis()-lastNextTime>1500)
			{
				if(levels[(level)%(BOSSFREQUENCY)][0]!=0)
				{
					if(notSpawnedYet)
					{
						for(int i=0;i<levels[(level)%BOSSFREQUENCY][0];i++)
						{
							if(i==0)
							{
								enemyTimer.schedule(
										new TimerTask()
										{
											public void run()
											{
												enemies.add(new Enemy(background.getWidth()/2,0,BulletType.BASIC_1,Ship.ENEMY_1,level,0));
											}
										},2000*0);
							}
							else if(i==1)
							{
								enemyTimer.schedule(
										new TimerTask()
										{
											public void run()
											{
												enemies.add(new Enemy(background.getWidth()/2,0,BulletType.BASIC_1,Ship.ENEMY_1,level,1));
												//setNeedSpawnEnemy(true);
											}
										},2000*1);
							}
							if(i==levels[(level)%BOSSFREQUENCY][0]-1)
							{
								lastSpawnTime = System.currentTimeMillis();
								notSpawnedYet = false;
							}
						}
					}
					else if(!notSpawnedYet && enemies.size()==0)
					{
						if(System.currentTimeMillis()-lastSpawnTime>5000 && enemies.size()==0)
						{
							level++;
							notSpawnedYet = true;
						}
					}
				}
				if(levels[(level)%(BOSSFREQUENCY)][1]!=0)
				{
					if(notSpawnedYet)
					{
						for(int i=0;i<levels[(level)%BOSSFREQUENCY][1];i++)
						{
							if(i==0)
							{
								enemyTimer.schedule(
										new TimerTask()
										{
											public void run()
											{
												enemies.add(new Enemy(background.getWidth()/2,0,BulletType.BASIC_2,Ship.ENEMY_2,level,0));
											}
										},2000*0);
							}
							else if(i==1)
							{
								enemyTimer.schedule(
										new TimerTask()
										{
											public void run()
											{
												enemies.add(new Enemy(background.getWidth()/2,0,BulletType.BASIC_2,Ship.ENEMY_2,level,1));
											}
										},2000*1);
							}
							if(i==levels[(level)%BOSSFREQUENCY][1]-1)
							{
								lastSpawnTime = System.currentTimeMillis();
								notSpawnedYet = false;
							}
						}
					}
					else if(!notSpawnedYet)
					{
						if(System.currentTimeMillis()-lastSpawnTime>5000 && enemies.size()==0)
						{
							level++;
							notSpawnedYet = true;
						}
					}
				}
			}
		}
		else if((level)%(BOSSFREQUENCY)==4) // NIVEAUX A BOSS (tous les BOSSFREQUENCY niveaux)
		{
			if(System.currentTimeMillis()-lastNextTime>1500)
			{
				if(levels[level%BOSSFREQUENCY][2]!=0)
				{
					if(notSpawnedYet)
					{
						enemies.add(new Enemy(background.getWidth()/2,0,BulletType.BOSS,Ship.BOSS,level,0));

						lastSpawnTime = System.currentTimeMillis();
						notSpawnedYet = false;
					}
					else if(!notSpawnedYet)
					{
						if(enemies.size()==0)
						{
							level++;
							notSpawnedYet = true;
						}
					}
				}

			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			Thread.yield();
		}
	}
}
