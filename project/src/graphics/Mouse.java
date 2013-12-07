package graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Classe charg�e de la gestion de la souris.
 * Les mouvements de la souris sont directement import�s de sorte � d�placer le sprite du joueur
 * dans la partie. Pour �a on utilise l'interface MouseMotionListener.
 * @author Florian
 *
 */
public class Mouse extends Thread {

	public int x,y,x0,y0;
	Player p;
	JFrame f;

	/**
	 * Constructeur de Mouse. 
	 * Les valeurs de x et y sont rep�r�es dans le rep�re du JFrame fourni en attribut de la m�thode.
	 */
	public Mouse(Player player,JFrame frame) {
		p = player;
		f = frame;
		Point point = new Point(p.getX(),p.getY());
		SwingUtilities.convertPointFromScreen(point,f);
		x=point.x;
		y=point.y;
		x0=x;
		y0=y;
	}

	/**
	 * M�thode run : tant que le joueur est en vie il faut constamment changer la position
	 * de son avatar en fonction des mouvements de la souris. 
	 */
	public void run() {
		while(true) {
			Point point = MouseInfo.getPointerInfo().getLocation();
			SwingUtilities.convertPointFromScreen(point, f);
			p.setXY(point.x,point.y);
			yield();
		}
	}
}
