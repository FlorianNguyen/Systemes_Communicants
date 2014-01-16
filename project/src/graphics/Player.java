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

	public static final int RELOADTIME=20;
	public static final int HITCASE_CENTERING = 32;
	private static BufferedImage avatar; // sprite du vaisseau joueur
	private String name; // pseudo du joueur
	private float score; // score du joueur
	private int[] upgrades; // armes et bonus ranges dans un tableau
	private int life; // points de vie du joueur
	private int x,y; // position du joueur
	private int X,Y; // position du sprite
	private Ship pShip; // modele de vaisseau du joueur
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
	public Player(String name)
	{
		this.name=name;

		//Position par défaut du joueur lorsqu'il apparaît
		x = 190;
		y = 400;

		life = 10000;
		score = 0;
		upgrades = new int[3];
		upgrades[0]=0;
		upgrades[1]=0;
		upgrades[2]=0;
		pShip = Ship.BASIC_PLAYER;
		lastShotTime = System.currentTimeMillis();
		avatar = Ship.BASIC_PLAYER.getSprite();
		X = x-avatar.getWidth()/2;
		Y = y-avatar.getHeight()/2+HITCASE_CENTERING;
	}

	/**
	 * Permet d'incrémenter le score de la partie
	 * @param value Valeur à augmenter au score.
	 */
	public void addToScore(int value, int level)
	{
		score+=(int)(value*2/Math.PI*Math.atan(Math.PI*0.5/20*level));
		System.out.println(score);
	}

	public void upgrade(int id)
	{
		upgrades[id]++;
	}

	public synchronized void getHitBy(BallManagement pool,int level)
	{
		if(pool.getBalls().size()!=0)
		{
			for(int i=0;i<pool.getBalls().size();i++)
			{
				Bullet b = pool.getBalls().get(i);
				if(life>0 && (b.getY()>this.getY()-12 && b.getY()<this.getY()+12) && 
						(b.getX()<this.getX()+12 && b.getX()>this.getX()-12))
				{
					pool.remove(b);
					this.life-=BulletType.getFromID(b.getID()).getDamage(level);
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
	 * Retourne le sprite du player
	 * @return le sprite associé au player
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
	 * Retourne l'ordonnée Y du joueur.
	 * @return l'ordonnée Y du joueur.
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

	public Ship getShip()
	{
		return pShip;
	}
}
