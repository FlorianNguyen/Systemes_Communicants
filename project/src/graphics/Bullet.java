package graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

/**
 * Bullet repr�sente un BulletType a une position donn�e � l'instant t,
 * une direction et une vitesse donn�es. L'ID permet d'associer � un Bullet
 * son BulletType.
 * @author Florian
 *
 */
public class Bullet {

	private int id;
	private double x,y,X,Y,dx,dy;

	/**
	 * Constructeur par d�faut de Bullet
	 * @param x0 Abscisse du Bullet � la cr�ation
	 * @param y0 Ordonn�e du Bullet � la cr�ation
	 * @param dx0 Vitesse de progression selon x (alg�brique)
	 * @param dy0 Vitesse de progression selon y (alg�brique)
	 */
	public Bullet(int no, int x0, int y0,double dx0, double dy0)
	{
		id=no;
		x=x0;
		X=x0-BulletType.getFromID(no).getSprite().getWidth()/2;
		y=y0;
		Y=y0-BulletType.getFromID(no).getSprite().getHeight()/2;
		dx=dx0;
		dy=dy0;
	}

	/**
	 * Met � jour les positions du Bullet gr�ce � une incr�mentation de x et y
	 */
	public void update()
	{
		x+=dx;
		y+=dy;
		X+=dx;
		Y+=dy;
	}

	/**
	 * Met le Bullet � la valeur d�sir�e.
	 * @param x X
	 * @param y Y
	 * @param dx d�placement selon X
	 * @param dy d�placement selon Y
	 * @param btID ID du BulletType 
	 */
	public void set(int x, int y, int dx, int dy, int btID)
	{
		this.x=x;
		X=x-BulletType.getFromID(btID).getSprite().getWidth()/2;
		this.y=y;
		Y=y-BulletType.getFromID(btID).getSprite().getHeight()/2;
		this.dx=dx;
		this.dy=dy;
		this.id = btID;
	}

	/**
	 * Retourne le X du Bullet
	 * @return X
	 */
	public int getX()
	{
		return (int)x;
	}

	/**
	 * Retourne le Y du Bullet
	 * @return Y
	 */
	public int getY()
	{
		return (int)y;
	}

	/**
	 * D�placement selon X.
	 * @return DX
	 */
	public double getDX()
	{
		return dx;
	}

	/**
	 * D�placement selon Y.
	 * @return DY
	 */
	public double getDY()
	{
		return dy;
	}

	/**
	 * Le X du sprite du Bullet.
	 * @return spriteX
	 */
	public int getSpriteX()
	{
		return (int)X;
	}

	/**
	 * Le Y du sprite du Bullet.
	 * @return spriteY
	 */
	public int getSpriteY()
	{
		return (int)Y;
	}

	/**
	 * Le sprite du Bullet.
	 * @return le sprite du Bullet
	 */
	public BufferedImage getSprite()
	{
		return BulletType.getFromID(id).getSprite();
	}

	/**
	 * Retourne l'ID
	 * @return ID
	 */
	public int getID()
	{
		return id;
	}
}