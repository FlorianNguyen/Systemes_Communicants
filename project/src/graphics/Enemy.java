package graphics;

import game.BackgroundDisplay;
import game.BallManagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Timer;

/**
 * Classe Enemy regroupant les informations caract�risant un vaisseau ennemi : 
 * type de munitions utilis�es, vaisseau, points de vie, position...
 * @author Florian
 *
 */
public class Enemy implements ActionListener {
	public BulletType bt;
	public Ship ship;
	public int x,y,X,Y;
	public int life;
	Timer timer;
	int left=20,right=0;
	
	/**
	 * Constructeur par d�faut de Enemy
	 * @param x Abscisse du joueur (pas du sprite !)
	 * @param y Ordonn�e du joueur
	 * @param bt Type de munitions utilis� par le vaisseau
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
		timer = new Timer(BackgroundDisplay.DEFAULT_FPS,this);
		life=ship.getLife();
		timer.start();
	}
	
	/**
	 * D�place l'Enemy (sprite et joueur) de DX selon X et DY selon Y
	 * @param dx D�placement selon x
	 * @param dy D�placement selon y
	 */
	public void move(int dx,int dy)
	{
		x+=dx;
		X+=dx;
		y+=dy;
		Y+=dy;
	}
	
	/**
	 * Retourne la coordonn�e x du joueur (position centr�e sur le sprite)
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * Retourne la coordonn�e y du joueur (position centr�e sur le sprite)
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
	 * D�finit les actions r�alis�es au rythme du compteur.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
			if(left>0)
			{
				this.move(-1,0);
				left--;
				if(left==0){right=20;}
			}
			
			if(right>0)
			{
				this.move(1,0);
				right--;
				if(right==0){left=20;}
			}
		
	}
	
	/**
	 * D�finit les Enemy qui seront g�n�r�s � une vague donn�e d'un niveau donn�.
	 * @param input Le tableau d'entiers correspondant au nombre de vaisseaux de chaque type g�n�r�s � une vague donn�e d'un niveau donn�.
	 * Ce tableau est l'une des valeurs du tableau levels de BackgroundDisplay.
	 * @return Un ArrayList contenant les Enemy g�n�r�s avec l'input donn�
	 */
	public static ArrayList<Enemy> spawn(int[] input)
	{
		ArrayList<Enemy> array = new ArrayList<Enemy>(0);
		if(input[0]>0)
		{
			for(int i=1;i<input[0];i++)
			{
				array.add(new Enemy(0,-100,BulletType.BASIC_ENNEMY1,Ship.BASIC_CRUISER));
				//cr�ation des ennemis en dehors de la fen�tre de jeu, de sorte � ce que le vaisseau entre en jeu ensuite
			}
		}
		if(input[1]>0)
		{
			for(int i=1;i<input[1];i++)
			{
				array.add(new Enemy(0,-100,BulletType.BASIC_ENNEMY2,Ship.BASIC_CRUISER));
			}
		}
		if(input[2]>0)
		{
			for(int i=1;i<input[2];i++)
			{
				array.add(new Enemy(0,-100,BulletType.BASIC_MEDIUM,Ship.ENNEMY_MEDIUM0));
			}
		}
		
		return array;
	}
	
	/**
	 * M�thode de tir propre � l'Enemy
	 * @param pool le BallManagement o� envoyer le Bullet(s) tir�(s)
	 */
	public void shoot(BallManagement pool)
	{
		
	}
	
	public void behaviour(BulletType bt, BallManagement pool)
	{
		
	}

	/**
	 * Retourne l'abscisse du sprite du vaisseau (coin en haut � gauche)
	 */
	public int getSpriteX() {
		return X;
	}

	/**
	 * Retourne l'ordonn�e du sprite du vaisseau (coin en haut � gauche)
	 */
	public int getSpriteY() {
		// TODO Auto-generated method stub
		return Y;
	}

	/**
	 * Retourne les points de vie du vaisseau
	 */
	public int getLife() {
		// TODO Auto-generated method stub
		return life;
	}
/**
 * Retourne le sprite du vaisseau
 */
	public BufferedImage getSprite() {
		// TODO Auto-generated method stub
		return ship.getSprite();
	}

}
