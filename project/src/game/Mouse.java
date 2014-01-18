package game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Classe permettant de recueillir l'information de clic de la souris.
 * @author Florian
 *
 */
public class Mouse implements MouseListener{

	private boolean mouseDown;

	/**
	 * Retourne mouseDown.
	 * @return mouseDown
	 */
	public boolean get()
	{
		return mouseDown;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Modifie la valeur de mousePressed quand la souris est pressée.
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			mouseDown = true;
		}

	}

	/**
	 * Modifie la valeur de mousePressed quand la souris est relachée.
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			mouseDown = false;
		}
	}
}
