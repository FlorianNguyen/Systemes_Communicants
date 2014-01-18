package game;

import graphics.Bullet;
import graphics.BulletType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * L'objet BallManagement contient un array de Bullet et permet leur gestion par les classes d'affichage.
 * @author Florian
 *
 */
public class BallManagement {

	private volatile ArrayList<Bullet> bullets;

	/**
	 * Construction d'un BallManagement avec des array permettant le stockage des informations relatives aux Bullets.
	 */
	public BallManagement()
	{
		bullets = new ArrayList<Bullet>();
	}

	/**
	 * Méthode de sollicitation d'une des lignes de l'array playerBalls.
	 * Cette ligne est mise aux valeurs spécifiées. Les modifications ne peuvent se faire qu'une par une.
	 * @param x Abscisse du Bullet
	 * @param y Ordonnée du Bullet
	 * @param dx Déplacement selon X
	 * @param dy Déplacement selon Y
	 * @param btID ID du BulletType
	 */
	public void addBall(int x,int y, double dx, double dy,int n,long index)
	{
		synchronized(bullets)
		{
			bullets.add(new Bullet(n,x,y,dx,dy,index));
		}
	}

	/**
	 * Déplace toutes les Bullet de l'array.
	 */
	public void update()
	{
		for(int i=0;i<bullets.size();i++) {
			Bullet b = bullets.get(i);
			b.update();
			if(b.getX()<-30||b.getY()<-30||b.getX()>400||b.getY()>750) {
				bullets.remove(b);
				this.remove(b);
			}
		}
	}

	/**
	 * Supprime une Bullet de l'array.
	 * @param b le Bullet à supprimer
	 */
	public void remove(Bullet b)
	{
		synchronized(bullets)
		{
			bullets.remove(b);
		}
	}
	
	/**
	 * Retourne les balles.
	 * @return l'array de balles
	 */
	public ArrayList<Bullet> getBalls()
	{
		return bullets;
	}

	/**
	 * Dit si l'array est vide on non.
	 * @return true si vide, et false sinon
	 */
	public boolean isEmpty()
	{
		return (bullets.size()==0);
	}
}
