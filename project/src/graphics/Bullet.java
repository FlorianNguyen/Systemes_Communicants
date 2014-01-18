package graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

/**
 * Bullet représente un BulletType a une position donnée à l'instant t,
 * une direction et une vitesse données. L'ID permet d'associer à un Bullet
 * son BulletType.
 * @author Florian
 *
 */
public class Bullet {

	private int id;
	private double x,y,X,Y,dx,dy;

	/**
	 * Constructeur par défaut de Bullet
	 * @param x0 Abscisse du Bullet à la création
	 * @param y0 Ordonnée du Bullet à la création
	 * @param dx0 Vitesse de progression selon x (algébrique)
	 * @param dy0 Vitesse de progression selon y (algébrique)
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
	 * Met à jour les positions du Bullet grâce à une incrémentation de x et y
	 */
	public void update()
	{
		x+=dx;
		y+=dy;
		X+=dx;
		Y+=dy;
	}

	/**
	 * Met le Bullet à la valeur désirée.
	 * @param x X
	 * @param y Y
	 * @param dx déplacement selon X
	 * @param dy déplacement selon Y
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
	 * Déplacement selon X.
	 * @return DX
	 */
	public double getDX()
	{
		return dx;
	}

	/**
	 * Déplacement selon Y.
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