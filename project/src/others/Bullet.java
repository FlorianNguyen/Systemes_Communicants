package others;
import java.awt.image.BufferedImage;


public class Bullet {

	static BufferedImage sprite;
	private double x, y, dx, dy;
	
	public Bullet(double x, double y, double dx, double dy) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
	}
	
	public int getX() {
		return (int) x;
	}
	
	public int getY() {
		return (int) y;
	}
	
	public void update() {
		x+=dx;
		y+=dy;
	}
	
	public boolean isInScreen(int width, int height) {
		return (x+sprite.getWidth()/2>0)&&(y+sprite.getHeight()/2>0)&&(x-sprite.getWidth()/2<width)&&(y-sprite.getHeight()/2<height);
	}
}
