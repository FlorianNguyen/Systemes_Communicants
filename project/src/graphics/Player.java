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
 * Le Player doit regrouper toutes les informations propres � un joueur.
 * On y trouvera donc le pseudo du joueur, l'ID, etc, sa position sur l'�cran, etc.
 * Le player sera utilis� pour l'affichage graphique, associ� � un objet Mouse.
 * @author Florian
 *
 */
public class Player {

	public static final int RELOADTIME=20;
	public static final int HITCASE_CENTERING = 32;
	private static BufferedImage avatar; // sprite du vaisseau joueur
	private String name; // pseudo du joueur
	private float score; // score du joueur
	private int[] upgrades; // armes et bonus ranges dans un tableau
	private int life; // points de vie du joueur
	private int x,y; // position du joueur
	private int X,Y; // position du sprite
	private PlayerShip pShip; // modele de vaisseau du joueur
	private static BulletType bt1 = BulletType.BASIC_PLAYER;
	public static int[][] DEFAULTDAMAGE ={{100,80,60},{110,85,65},{130,90,75}};
	// dommages par defaut en fonction du niveau d'amelioration
	private ArrayList<Bullet> balls = new ArrayList<Bullet>();
	private Semaphore shootSem = new Semaphore(1);
	private long lastShotTime;

	/**
	 * Constructeur de la classe Player.
	 * @param name Pseudo du joueur
	 * @param playershipID ID du joueur
	 */
	public Player(String name,int playershipID)
	{
		this.name=name;

		//Position par d�faut du joueur lorsqu'il appara�t
		x = 190;
		y = 400;

		life = 10000;
		score = 0;
		upgrades = new int[3];
		upgrades[0]=0;
		upgrades[1]=0;
		upgrades[2]=0;
		pShip = PlayerShip.getShip(playershipID);
		lastShotTime = System.currentTimeMillis();
		try {
			avatar = ImageIO.read(new File("left_11.png"));
			X = x-avatar.getWidth()/2;
			Y = y-avatar.getHeight()/2+HITCASE_CENTERING;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Permet d'incr�menter le score de la partie
	 * @param value Valeur � augmenter au score.
	 */
	public void addToScore(int value)
	{
		score+=value;
	}

	public void upgrade(int id)
	{
		upgrades[id]++;
	}
	
	public synchronized void getHitBy(BallManagement pool,int level)
	{
		if(pool.getBalls().size()!=0)
		{
			for(Bullet b : pool.getBalls())
			{
				if(life>0 && (b.getY()>this.getY()-10 && b.getY()<this.getY()+10) && 
						(b.getX()<this.getX()+this.getSprite().getWidth()/2 && b.getX()>this.getX()-this.getSprite().getWidth()/2))
				{
					pool.remove(b);
					this.life-=BulletType.getFromID(b.getID()).getDamage(level);
				}
			}
		}
	}

	/**
	 * Permet d'incr�menter la vie du joueur.
	 * @param value valeur � ajouter � la vie actuelle du joueur (peut �tre n�gative...)
	 */
	public void addTolife(int value)
	{
		life+=value;
	}

	/**
	 * Modifie la position du joueur.
	 * @param x Nouvelle abscisse du joueur
	 * @param y Nouvelle ordonn�e du joueur
	 */
	public void setXY(int x,int y)
	{
		this.x=x;
		this.X=x-avatar.getWidth()/2;
		this.y=y;
		this.Y=y-avatar.getHeight()/2+32;

	}

	/**
	 * Retourne le sprite du player
	 * @return le sprite associ� au player
	 */
	public BufferedImage getSprite()
	{
		return avatar;
	}

	/**
	 * Retourne l'ascisse X du joueur.
	 * @return l'abscisse X du joueur.
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Retourne l'ordonn�e Y du joueur.
	 * @return l'ordonn�e Y du joueur.
	 */
	public int getY()
	{
		return y;
	}

	public int getSpriteX()
	{
		return X;
	}

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

	public void primaryShooting(BallManagement bm)
	{
		if(System.currentTimeMillis()-lastShotTime>BulletType.BASIC_PLAYER.getReloadTime())
		{
			if(upgrades[0]==0)
			{
				try {
					shootSem.acquire();
					bm.addBall(x,y,0,-BulletType.BASIC_PLAYER.getSpeed(),BulletType.BASIC_PLAYER.getID());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else if(upgrades[0]==1)
			{
				try {
					shootSem.acquire();
					bm.addBall(x-10,y,-0.4,-BulletType.BASIC_PLAYER.getSpeed(),BulletType.BASIC_PLAYER.getID());
					bm.addBall(x+10,y,0.4,-BulletType.BASIC_PLAYER.getSpeed(),BulletType.BASIC_PLAYER.getID());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else if(upgrades[0]==2)
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

	public PlayerShip getShip()
	{
		return pShip;
	}
}
