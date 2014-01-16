package game;
import graphics.Bullet;
import graphics.BulletType;
import graphics.Enemy;
import graphics.FictiveEnemy;
import graphics.Player;
import graphics.Ship;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;

/**
 * Cette classe gète l'affichage et le rafraichissement des images pendant la partie.
 * Les images sont rafraichies toutes les 16ms, et le MouseListener permet de tirer sur les vaisseaux ennemis.
 * Les balles sortant de l'écran sont automatiquement supprimées (ou plutôt, l'espace contenant l'information est
 * remis à disposition pour la création de nouvelles balles).
 * @author Florian Nguyen
 *
 */
public class BackgroundDisplayClient extends JPanel implements ActionListener {

	private Player player1,player2; //JOUEUR
	private static final int LOWERLIMIT = 40; //LIMITE BASSE EN DESSOUS DE LAQUELLE LE PLAYER NE PEUT PAS ALLER
	private static final int RIGHTLIMIT = 5; // LIMITE DROITE (limite gauche fonctionnelle par défaut)
	public static final int DEFAULT_FREQUENCY=10; // TEMPS DE RAFRAICHISSEMENT DES IMAGES
	public static final int BOSSFREQUENCY = 5; // NOMBRE DE NIVEAUX ENTRE CHAQUE BOSS
	public static BufferedImage background; // IMAGE SERVANT DE FOND D'ECRAN DEFILANT
	private int height,width,level;
	private int Y; // VARIABLE SERVANT AU DEFILEMENT DU BACKGROUND
	private JFrame frame;
	private Mouse mouse = new Mouse(); // MOUSELISTENER PERMETTANT DE CONTROLER LE JOUEUR
	private BallManagement pBalls,eBalls; // OBJETS BULLET A PEINDRE A L'INSTANT T (BULLET PLAYER OU ENEMY)
	private FictiveEnemy penemy;
	private volatile ArrayList<Enemy> enemies = new ArrayList<Enemy>(0);
	private java.util.Timer enemyTimer = new java.util.Timer();
	private javax.swing.Timer t = new javax.swing.Timer(DEFAULT_FREQUENCY,this); // TIMER POUR LE RAFRAICHISSEMENT DES IMAGES
	private long lastNextTime;
	private boolean notSpawnedYet;
	private long lastSpawnTime;
	private long time = System.currentTimeMillis();
	private boolean isOver,enablePlayer1;

	public static final int[][] levels = // NIVEAUX ET VAGUES DE VAISSEAUX PAR NIVEAU
		{
		{1,0,0}, //niveau 1, 3 vagues...
		{2,0,0}, //niveau 2
		{0,1,0}, //niveau 3
		{0,2,0},  // etc
		{0,0,1}};

	/**
	 * Constructeur de la classe BackgroundDisplay.
	 * @param f JFrame dans lequel l'on souhaite réaliser l'affichage.
	 * @param sourcename Source du fichier image (png) qui sera utilisé en fond d'écran.
	 * L'image doit être cyclique, c'est à dire que la partie haute et la partie basse sont compatibles.
	 */
	public BackgroundDisplayClient(String name, String sourcename)
	{
		try {
			// INITIALISATIONS
			player2 = new Player(name,false);
			frame = new JFrame();
			//			notSpawnedYet=true;
			//			lastSpawnTime = 0;
			background = ImageIO.read(new File(sourcename));
			height = background.getHeight();
			width = background.getWidth();
			level = -1;
			isOver = false;
			pBalls = new BallManagement();
			eBalls = new BallManagement();

			// RECUPERATION DES DONNEES DU HOST
			player1 = new Player("P1",true);
			enablePlayer1 = false;

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
			//lancement uniquement quand connection réussie...
			t.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

		// RAFFRAICHISSEMENT DE LA POSITION DU JOUEUR 2
		g.drawImage(
				player2.getSprite(),
				player2.getSpriteX(),
				player2.getSpriteY(), //centrage souris sur la hit-case
				this);

		// RAFFRAICHISSEMENT DE LA POSITION DU JOUEUR 1
		g.drawImage(
				player1.getSprite(),
				player1.getSpriteX(),
				player1.getSpriteY(), //centrage souris sur la hit-case
				this);

		g.dispose();
	}

	/**
	 * Définit l'action réalisée en rythme avec le Timer. 
	 * Il faut notamment gérer les cas de figure où la souris sort de la fenêtre de jeu,
	 * pour donner au Player les bonnes positions dans chaque cas.
	 */
	public void actionPerformed(ActionEvent e) {

		// MISE A JOUR DES BALLES 
		pBalls.update();

		//GESTION DU JOUEUR EN FONCTION DU POINTEUR SOURIS
		Point point = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(point, this);
		if(point.x>0 && point.x<background.getWidth()-RIGHTLIMIT && point.y>0 && point.y<background.getHeight()-LOWERLIMIT)
		{
			player1.setXY(point.x,point.y);
		}
		else if(point.x<0)
		{
			if(point.y<0){player1.setXY(0,0);}
			else if(point.y>background.getHeight()-LOWERLIMIT){player2.setXY(0,background.getHeight()-LOWERLIMIT);}
			else player2.setXY(0,point.y);
		}
		else if(point.x>background.getWidth()-RIGHTLIMIT)
		{
			if(point.y<0){player1.setXY(background.getWidth()-RIGHTLIMIT,0);}
			else if(point.y>background.getHeight()-LOWERLIMIT){player2.setXY(background.getWidth()-RIGHTLIMIT,background.getHeight()-LOWERLIMIT);}
			else player2.setXY(background.getWidth()-RIGHTLIMIT,point.y);
		}
		else if(point.y<0)
		{
			if(point.x<0){player2.setXY(0,0);}
			else if(point.x>background.getWidth()-RIGHTLIMIT){player2.setXY(background.getWidth()-RIGHTLIMIT,0);}
			else player2.setXY(point.x,0);
		}
		else if(point.y>background.getHeight()-LOWERLIMIT)
		{
			if(point.x<0){player2.setXY(0,background.getHeight()-LOWERLIMIT);}
			else if(point.x>background.getWidth()-RIGHTLIMIT){player2.setXY(background.getWidth()-RIGHTLIMIT,background.getHeight()-LOWERLIMIT);}
			else player2.setXY(point.x,background.getHeight()-LOWERLIMIT);
		}

		// PARTIE RELATIVE AU TIR DU VAISSEAU PLAYER
		if(mouse.get()==true)
		{
			player2.primaryShooting(pBalls); // primaryShooting prend en compte le temps de rechargement
		}
		
		repaint();
	}

	public ArrayList<Bullet> getPlayerBalls()
	{
		return pBalls.getBalls();
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

	public void addMultiplayerData(ArrayList<Bullet> balls)
	{
		for(int i=0;i<balls.size();i++)
		{
			pBalls.getBalls().add(balls.get(i));
		}
	}

	public void setMultiplayerXY(int a, int b)
	{
		player2.setXY(a,b);
	}
	
	public int getX()
	{
		return player2.getX();
	}
	
	public int getY()
	{
		return player2.getY();
	}
}
