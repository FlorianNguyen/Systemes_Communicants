package graphics;

import game.BackgroundDisplayHost;
import game.BallManagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Timer;

/**
 * Classe Enemy regroupant les informations caractérisant un vaisseau ennemi : 
 * type de munitions utilisées, vaisseau, points de vie, position...
 * @author Florian
 *
 */
public class Enemy {
	private final int hMargin;
	private BulletType bt;
	private Ship ship;
	private int x,y,X,Y;
	private int life;
	Timer timer;
	int left=0,right=0;
	private boolean isAlive;
	private boolean inScreen;
	private boolean ready;
	private long lastShotTime;

	/**
	 * Constructeur par défaut de Enemy
	 * @param x Abscisse du joueur (pas du sprite !)
	 * @param y Ordonnée du joueur
	 * @param bt Type de munitions utilisé par le vaisseau
	 * @param ship Type de vaisseau de l'Enemy
	 */
	public Enemy(int x,int y,BulletType bt,Ship ship,int level)
	{
		super();
		this.x=x;
		this.y=y;
		this.ship=ship;
		X=x-ship.getSprite().getWidth()/2;
		Y=y-ship.getSprite().getHeight()/2;
		hMargin = BackgroundDisplayHost.background.getWidth()/2-this.getSprite().getWidth()/2;
		left = hMargin/2;
		this.bt=bt;
		this.ship=ship;
		lastShotTime = System.currentTimeMillis();
		isAlive = true;
		ready = false;
		inScreen = false;
		life=ship.getLife(level);
	}

	/**
	 * Déplace l'Enemy (sprite et joueur) de DX selon X et DY selon Y
	 * @param dx Déplacement selon x
	 * @param dy Déplacement selon y
	 */
	public void move(int dx,int dy)
	{
		x+=dx;
		X+=dx;
		y+=dy;
		Y+=dy;
	}

	/**
	 * Retourne la coordonnée x du joueur (position centrée sur le sprite)
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Retourne la coordonnée y du joueur (position centrée sur le sprite)
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Retourne le Ship du vaisseau Enemy
	 * @return
	 */
	public Ship getShip()
	{
		return ship;
	}

	/**
	 * Retourne le BulletType de l'Enemy
	 * @return
	 */
	public BulletType getBulletType()
	{
		return bt;
	}

	/**
	 * Permet de savoir si le vaisseau a encore de la vie ou non
	 * @return true si vivant, false sinon
	 */
	public boolean isAlive()
	{
		return isAlive;
	}

	public void update()
	{
		if(!inScreen)
		{
			move(0,+2);
			if(y>getSprite().getHeight()+30){inScreen=true;ready=true;}
		}
		if(isAlive && inScreen)
		{
			if(left>0)
			{
				this.move(-2,0);
				left--;
				if(left==0){right=hMargin;}
			}

			if(right>0)
			{
				this.move(2,0);
				right--;
				if(right==0){left=hMargin;}
			}
		}
	}

	/**
	 * Méthode de tir propre à l'Enemy
	 * @param pool le BallManagement où envoyer le Bullet(s) tiré(s)
	 */
	public void shoot(BallManagement pool)
	{
		if(ready)
		{
			if(System.currentTimeMillis()-lastShotTime>bt.getReloadTime())
			{
				if(bt.getID()==1)
				{
				pool.addBall(x,y,0,5,bt.getID());
				lastShotTime = System.currentTimeMillis();
				}
				else if(bt.getID()==2)
				{
					
				}
				else if(bt.getID()==3)
				{
					
				}
			}
		}
	}

	public void getHitBy(BallManagement pool,int level)
	{
		synchronized(pool.getBalls())
		{
			if(pool.getBalls().size()!=0)
			{
				for(int i=0;i<pool.getBalls().size();i++)
				{
					Bullet b = pool.getBalls().get(i);
					if(life>0 && (b.getY()>this.getY()-50 && b.getY()<this.getY()+10) && 
							(b.getX()<this.getX()+this.getSprite().getWidth()/2 && b.getX()>this.getX()-this.getSprite().getWidth()/2))
					{
						pool.remove(b);
						this.life-=BulletType.getFromID(b.getID()).getDamage(level);
					}
				}
			}
			if(life<=0){isAlive=false;}
		}
	}
	
	/**
	 * Retourne l'abscisse du sprite du vaisseau (coin en haut à gauche)
	 */
	public int getSpriteX() {
		return X;
	}

	/**
	 * Retourne l'ordonnée du sprite du vaisseau (coin en haut à gauche)
	 */
	public int getSpriteY() {
		// TODO Auto-generated method stub
		return Y;
	}

	/**
	 * Retourne les points de vie du vaisseau
	 */
	public int getLife() {
		return life;
	}
	
	/**
	 * Retourne le sprite du vaisseau
	 */
	public BufferedImage getSprite() {
		return ship.getSprite();	
	}
}
