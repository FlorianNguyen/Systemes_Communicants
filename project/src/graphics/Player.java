package graphics;

import game.BallManagement;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Le Player doit regrouper toutes les informations propres à un joueur.
 * On y trouvera donc le pseudo du joueur, l'ID, etc, sa position sur l'écran, etc.
 * Le player sera utilisé pour l'affichage graphique, associé à un objet Mouse.
 * @author Florian
 *
 */
public class Player {

	public static int[][] DEFAULTDAMAGE ={{100,80,60},{110,85,65},{130,90,75}};
	private static BufferedImage avatar; // sprite du vaisseau joueur
	public static final int HITCASE_CENTERING = 32;

	private String name; // pseudo du joueur
	private int score; // score du joueur
	private int life; // points de vie du joueur
	private int x,y; // position du joueur
	private int X,Y; // position du sprite
	private int xp; // xp du joueur
	private int level; // niveau du joueur et de la partie
	private Ship pShip; // modele de vaisseau du joueur
	private ArrayList<Bullet> balls = new ArrayList<Bullet>();
	private Semaphore shootSem = new Semaphore(1);
	private long lastShotTime;

	/**
	 * Constructeur de la classe Player.
	 * @param name Pseudo du joueur
	 * @param playershipID ID du joueur
	 */
	public Player(String name, boolean client)
	{
		this.name=name;

		//Position par défaut du joueur lorsqu'il apparaît
		x = 190;
		y = 400;

		life = 10000;
		score = 0;
		xp=0;
		pShip = Ship.BASIC_PLAYER;
		lastShotTime = System.currentTimeMillis();
		avatar = Ship.BASIC_PLAYER.getSprite();
		X = x-avatar.getWidth()/2;
		Y = y-avatar.getHeight()/2+HITCASE_CENTERING;
	}

	/**
	 * Permet d'incrémenter le score de la partie.
	 * @param value Valeur à augmenter au score
	 */
	public void addToScore(int value, int level)
	{
		score+=(int)(1+value*2/Math.PI*Math.atan(Math.PI*0.5/20*level));
	}

	/**
	 * Incrémente l'XP.
	 * @param toAdd valeur à ajouter
	 */
	public void addToXp(int toAdd)
	{
		xp+=toAdd;
	}

	/**
	 * Met XP à la valeur désirée.
	 * @param value
	 */
	public void setXP(int value)
	{
		xp = value;
	}

	/**
	 * Retourne le level.
	 * @return level
	 */
	public int getLevel()
	{
		return level;
	}
	
	/**
	 * Met le level à la valeur désirée.
	 * @param value nouvelle valeur du level
	 */
	public void setLevel(int value)
	{
		this.level = value;
	}
	
	/**
	 * Retourne l'XP.
	 * @return XP
	 */
	public int getXP()
	{
		return xp;
	}

	/**
	 * Le player se fait toucher par les balles de l'array.
	 * @param pool Array à considérer
	 * @param level niveau actuel
	 */
	public void getHitBy(BallManagement pool,int level)
	{
		synchronized(pool.getBalls())
		{
			if(pool.getBalls().size()!=0)
			{
				for(int i=0;i<pool.getBalls().size();i++)
				{
					Bullet b = pool.getBalls().get(i);
					if(life>0 && (b.getY()>this.getY()-13 && b.getY()<this.getY()+13) && 
							(b.getX()<this.getX()+13 && b.getX()>this.getX()-13))
					{
						pool.remove(b);
						this.life-=BulletType.getFromID(b.getID()).getDamage(level);
					}
				}
			}
		}
	}

	/**
	 * Permet d'incrémenter la vie du joueur.
	 * @param value valeur à ajouter à la vie actuelle du joueur (peut être négative...)
	 */
	public void addTolife(int value)
	{
		life+=value;
	}

	/**
	 * Modifie la position du joueur.
	 * @param x Nouvelle abscisse du joueur
	 * @param y Nouvelle ordonnée du joueur
	 */
	public void setXY(int x,int y)
	{
		this.x=x;
		this.X=x-avatar.getWidth()/2;
		this.y=y;
		this.Y=y-avatar.getHeight()/2+32;

	}

	/**
	 * Retourne le sprite du player.
	 * @return le sprite associé au player
	 */
	public BufferedImage getSprite()
	{
		return avatar;
	}

	/**
	 * Retourne l'ascisse X du joueur.
	 * @return l'abscisse X du joueur
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Retourne l'ordonnée Y du joueur.
	 * @return l'ordonnée Y du joueur
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Retourne le X du sprite.
	 * @return le X du sprite
	 */
	public int getSpriteX()
	{
		return X;
	}

	/**
	 * Retourne le Y du sprite.
	 * @return le Y du sprite
	 */
	public int getSpriteY()
	{
		return Y;
	}
	/**
	 * La vie du joueur
	 * @return la vie du joueur
	 */
	public int getLife()
	{
		return life;
	}

	/**
	 * Fixe la vie à une valeur donnée.
	 * @param life la nouvelle valeur de la vie
	 */
	public void setLife(int life)
	{
		this.life=life;
	}
	/**
	 * Met le score à la valeur désirée.
	 * @param value la nouvelle valeur du score
	 */
	public void setScore(int value)
	{
		score = value;
	}

	/**
	 * Méthode de tir pour le Player
	 * @param bm l'array dans lequel stocker les balles
	 */
	public void primaryShooting(BallManagement bm)
	{
		synchronized(bm.getBalls())
		{
			if(System.currentTimeMillis()-lastShotTime>BulletType.BASIC_PLAYER.getReloadTime())
			{
				if(xp<100)
				{
					try {
						shootSem.acquire();
						bm.addBall(x,y,0,-BulletType.BASIC_PLAYER.getSpeed(),BulletType.BASIC_PLAYER.getID());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else if(xp<1000)
				{
					try {
						shootSem.acquire();
						bm.addBall(x-10,y,-0.4,-BulletType.BASIC_PLAYER.getSpeed(),BulletType.BASIC_PLAYER.getID());
						bm.addBall(x+10,y,0.4,-BulletType.BASIC_PLAYER.getSpeed(),BulletType.BASIC_PLAYER.getID());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else if(xp>=1000)
				{
					try {
						shootSem.acquire();
						bm.addBall(x-17,y,-0.6,-BulletType.BASIC_PLAYER.getSpeed(),BulletType.BASIC_PLAYER.getID());
						bm.addBall(x+17,y,0.6,-BulletType.BASIC_PLAYER.getSpeed(),BulletType.BASIC_PLAYER.getID());
						bm.addBall(x,y-12,0,-BulletType.BASIC_PLAYER.getSpeed(),BulletType.BASIC_PLAYER.getID());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				lastShotTime = System.currentTimeMillis();
				shootSem.release();
			}
		}
	}

	/**
	 * Retourne le score.
	 * @return le score
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * Permet de savoir si l'on peut tirer.
	 * @return true si oui, false sinon
	 */
	public boolean canShoot()
	{
		if(System.currentTimeMillis()-lastShotTime>BulletType.BASIC_PLAYER.getReloadTime())
		{
			return true;
		}
		else return false;
	}

	/**
	 * Retourne le ship du Player.
	 * @return le ship
	 */
	public Ship getShip()
	{
		return pShip;
	}
}
