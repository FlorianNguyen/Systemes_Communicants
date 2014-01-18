package graphics;

import game.BallManagement;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Timer;

/**
 * Classe Enemy regroupant les informations caractérisant un vaisseau ennemi : 
 * type de munitions utilisées, vaisseau, points de vie, position...
 * @author Florian
 *
 */
public class Enemy {
	
	private static int hMargin;
	
	private int levelIndex;
	private BulletType bt;
	private Ship ship;
	private int x,y,X,Y;
	private int life;
	private int left=0,right=0;
	private int direction;
	private boolean isAlive;
	private boolean inScreen;
	private boolean ready;
	private long lastShotTime;
	private double bossRotation =0;
	
	private Timer timer;

	/**
	 * Constructeur par défaut de Enemy
	 * @param x Abscisse du joueur (pas du sprite !)
	 * @param y Ordonnée du joueur
	 * @param bt Type de munitions utilisé par le vaisseau
	 * @param ship Type de vaisseau de l'Enemy
	 */
	public Enemy(int x,int y,BulletType bt,Ship ship,int level,int levelIndex)
	{
		direction=0;
		this.levelIndex = levelIndex;
		this.x=x;
		this.y=y;
		this.ship=ship;
		hMargin=0;
		X=x-ship.getSprite().getWidth()/2;
		Y=y-ship.getSprite().getHeight()/2;
		this.bt=bt;
		lastShotTime = System.currentTimeMillis();
		isAlive = true;
		ready = false;
		inScreen = false;
		try {
			hMargin = ImageIO.read(new File("background2.png")).getWidth()/2-this.getSprite().getWidth()/2;
			left = hMargin/2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 * Retourne le levelIndex
	 * @return levelIndex
	 */
	public int getLevelIndex()
	{
		return levelIndex;
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
	 * Actualise la position de l'Enemy
	 */
	public void update()
	{
		if(!inScreen)
		{
			move(0,+2);
			if(y>getSprite().getHeight()+30){inScreen=true;ready=true;direction=0;}
		}
		if(isAlive && inScreen)
		{
			if(left>0)
			{
				direction=-1;
				this.move(-2,0);
				left--;
				if(left==0){right=hMargin;direction=0;}
			}

			if(right>0)
			{
				direction=+1;
				this.move(2,0);
				right--;
				if(right==0){left=hMargin;direction=0;}
			}
		}
	}

	/**
	 * Méthode de tir propre à l'Enemy
	 * @param pool le BallManagement où envoyer le Bullet(s) tiré(s)
	 */
	public boolean shoot(BallManagement pool,long maxIndex)
	{
		boolean toReturn=false;
		if(ready)
		{
			if(System.currentTimeMillis()-lastShotTime>bt.getReloadTime())
			{
				if(bt.getID()==1)
				{
					synchronized(pool.getBalls())
					{
						pool.addBall(x,y,direction*0.3,bt.getSpeed(),bt.getID(),maxIndex);
						lastShotTime = System.currentTimeMillis();
						toReturn=true;
					}
				}
				else if(bt.getID()==2)
				{
					synchronized(pool.getBalls())
					{
						pool.addBall(x,y,direction*0.9,bt.getSpeed(),bt.getID(),maxIndex);
						pool.addBall(x,y,direction*(-0.9),bt.getSpeed(),bt.getID(),maxIndex);
						lastShotTime = System.currentTimeMillis();
						toReturn=true;
					}
				}
				else if(bt.getID()==3)
				{
					synchronized(pool.getBalls())
					{
						for(double angle = -Math.PI/2; angle<2*Math.PI+-Math.PI/2; angle += 2*Math.PI/12) {
							pool.getBalls().add(new Bullet(bt.getID(),x,y,bt.getSpeed()*Math.cos(angle+bossRotation),bt.getSpeed()*Math.sin(angle+bossRotation),maxIndex+(long)(angle/(Math.PI/2)+1)));
						}
						bossRotation+=10*Math.PI/180;
						lastShotTime = System.currentTimeMillis();
						toReturn=true;
					}
				}
			}
		}
		return toReturn;
	}

	/**
	 * L'Enemy entre en collision avec les balles.
	 * @param pool Array de balles à considérer
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
