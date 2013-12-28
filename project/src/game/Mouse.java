package game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener{

	public boolean mouseDown;

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

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			System.out.println("pressed");
			mouseDown = true;
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			mouseDown = false;
		}
	}
}
