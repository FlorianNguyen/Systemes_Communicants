package graphics;
import java.awt.MouseInfo;
import java.awt.Point;


public class Mouse extends Thread{
	
	private Player player;
	
	private int x0, y0;
	
	public Mouse(Player p) {
		player = p;
		x0 = (int) p.getX();
		y0 = (int) p.getY();
	}
	
	public void run() {
		while(player.getLife()!=0) {
			Point p = MouseInfo.getPointerInfo().getLocation();
			player.setXY(p.x-x0, p.y-y0);
			yield();
		}
	}

}
