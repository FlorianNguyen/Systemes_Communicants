package graphics;

import game.BackgroundDisplay;
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
public class Enemy implements ActionListener {
	private BulletType bt;
	private Ship ship;
	private int x,y,X,Y;
	private int life;
	Timer timer;
	int left=25,right=0;
	private boolean isAlive;
	private long lastShotTime;

	/**
	 * Constructeur par défaut de Enemy
	 * @param x Abscisse du joueur (pas du sprite !)
	 * @param y Ordonnée du joueur
	 * @param bt Type de munitions utilisé par le vaisseau
	 * @param ship Type de vaisseau de l'Enemy
	 */
	public Enemy(int x,int y,BulletType bt,Ship ship)
	{
		super();
		this.x=x;
		this.y=y;
		X=x-ship.getSprite().getWidth()/2;
		Y=y-ship.getSprite().getHeight()/2;
		this.bt=bt;
		this.ship=ship;
		lastShotTime = System.currentTimeMillis();
		isAlive = true;
		timer = new Timer(BackgroundDisplay.DEFAULT_FREQUENCY,this);
		life=ship.getLife();
		timer.start();
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

	/**
	 * Définit les actions réalisées au rythme du compteur.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(isAlive)
		{
			if(left>0)
			{
				this.move(-1,0);
				left--;
				if(left==0){right=50;}
			}

			if(right>0)
			{
				this.move(1,0);
				right--;
				if(right==0){left=50;}
			}
		}
	}

	/**
	 * Méthode de tir propre à l'Enemy
	 * @param pool le BallManagement où envoyer le Bullet(s) tiré(s)
	 */
	public void shoot(BallManagement pool)
	{
		if(System.currentTimeMillis()-lastShotTime>bt.getReloadTime())
		{
			pool.addBall(x,y,0,5,bt.getID());
			lastShotTime = System.currentTimeMillis();
		}
	}

	public synchronized void getHitBy(BallManagement pool,int level)
	{
		if(pool.getBalls().size()!=0)
		{
			for(int i=0;i<pool.getBalls().size();i++)
			{
				Bullet b = pool.getBalls().get(i);
				if(life>0 && (b.getY()>this.getY()-10 && b.getY()<this.getY()+10) && 
						(b.getX()<this.getX()+this.getSprite().getWidth()/2 && b.getX()>this.getX()-this.getSprite().getWidth()/2))
				{
					pool.remove(b);
					this.life-=BulletType.getFromID(b.getID()).getDamage(level);
				}
			}
		}
		if(life<=0){isAlive=false;}
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
