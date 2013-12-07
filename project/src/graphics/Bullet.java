package graphics;

public class Bullet {

	public BulletType bt;
	public int x,y,dx,dy;
	
	public Bullet(BulletType bt0,int x0, int y0, int dx0, int dy0)
	{
		bt=bt0;
		x=x0;
		y=y0;
		dx=dx0;
		dy=dy0;
	}
	
	public void update()
	{
		x+=dx;
		y+=dy;
	}
}
