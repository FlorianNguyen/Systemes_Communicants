package others;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Mouse extends Thread{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedImage bg;
		try {
			bg = ImageIO.read(new File("background2.png"));
			Mouse m = new Mouse(new Game(bg));
			m.start();
			try {
				m.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private Game game;
	
	private int x0, y0;
	
	public Mouse(Game g) {
		game = g;
		x0 = (int) g.getX();
		y0 = (int) g.getY();
	}
	
	public void run() {
		while(game.isShowing()) {
			Point p = MouseInfo.getPointerInfo().getLocation();
			//System.out.println("x = "+p.x+"; y = "+p.y+";");
			game.setPlayer(p.x-x0, p.y-y0);
			yield();
		}
	}

}
