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
public class Bullet implements ActionListener {

	public int id;
	public int x,y,X,Y,dx,dy;
	boolean isVisible;
	Timer timer;
	
	/**
	 * Constructeur par défaut de Bullet
	 * @param x0 Abscisse du Bullet à la création
	 * @param y0 Ordonnée du Bullet à la création
	 * @param dx0 Vitesse de progression selon x (algébrique)
	 * @param dy0 Vitesse de progression selon y (algébrique)
	 */
	public Bullet(int no, int x0, int y0, int dx0, int dy0)
	{
		id=no;
		x=x0;
		X=x0-BulletType.getFromID(no).getSprite().getWidth()/2;
		y=y0;
		Y=x0-BulletType.getFromID(no).getSprite().getHeight()/2;
		dx=dx0;
		dy=dy0;
		isVisible=false;
		timer = new Timer(BulletType.getFromID(id).getReloadTime(),this);
		timer.start();
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
	
	public boolean isVisible()
	{
		return isVisible;
	}
	
	public void setVisible(boolean b)
	{
		isVisible = b;
	}
	
	public void setID(int id)
	{
		this.id = id;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getDX()
	{
		return dx;
	}
	
	public int getDY()
	{
		return dy;
	}
	
	public int getSpriteX()
	{
		return X;
	}
	
	public int getSpriteY()
	{
		return Y;
	}
	
	public BufferedImage getSprite()
	{
		return BulletType.getFromID(id).getSprite();
	}
	
	public void setXY(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public void setDXDY(int x,int y)
	{
		this.dx=x;
		this.dy=y;
	}
	
	public void reset()
	{
		timer.stop();
		timer.restart();
		x=0;
		y=0;
		dx=0;
		dy=0;
		isVisible=false;
		id=0;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		update();
	}
}