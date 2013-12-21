package graphics;

/**
 * Bullet repr�sente un BulletType a une position donn�e � l'instant t,
 * une direction et une vitesse donn�es. L'ID permet d'associer � un Bullet
 * son BulletType.
 * @author Florian
 *
 */
public class Bullet {

	public int id;
	public int x,y,dx,dy;
	boolean isVisible;
	
	/**
	 * Constructeur par d�faut de Bullet
	 * @param x0 Abscisse du Bullet � la cr�ation
	 * @param y0 Ordonn�e du Bullet � la cr�ation
	 * @param dx0 Vitesse de progression selon x (alg�brique)
	 * @param dy0 Vitesse de progression selon y (alg�brique)
	 */
	public Bullet(int no, int x0, int y0, int dx0, int dy0)
	{
		id=no;
		x=x0;
		y=y0;
		dx=dx0;
		dy=dy0;
		isVisible=false;
	}
	
	/**
	 * Met � jour les positions du Bullet gr�ce � une incr�mentation de x et y
	 */
	public void update()
	{
		x+=dx;
		y+=dy;
	}
	
	public boolean isVisible()
	{
		return isVisible;
	}
	
	public void setVisible(boolean b)
	{
		isVisible = b;
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
	
	public void setXY(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public void reset()
	{
		x=0;
		y=0;
		dx=0;
		dy=0;
		isVisible=false;
		id=0;
	}
}