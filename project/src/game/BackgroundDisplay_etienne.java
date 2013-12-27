package game;


import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.Timer;



/**
 * Cette classe gère l'affichage du background mouvant en arrière-plan.
 * Pour cela il faut lui indiquer le chemin de l'image à utiliser et le JFrame dans lequel
 * afficher.
 * @author Florian Nguyen
 *
 */
public class BackgroundDisplay_etienne extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BufferedImage background;
	public int height,width;
	public int Y;
	public JFrame frame;



	public BackgroundDisplay_etienne(JFrame f,final JButton[] buttons,final JLabel label, String sourcename)
	{
		try {
			frame = f;
			background = ImageIO.read(new File(sourcename));
			height = 2*background.getHeight();
			width = background.getWidth();
			setLayout(new OverlayLayout(this));
			//t = new Timer(16,this);
			//t.start();
				
			//on cré un compteur qui repeint tout les delay ms et on réaffiche les boutons et les labels
			
			int delay = 15; //milliseconds

			
			ActionListener taskPerformer = new ActionListener() {
			  public void actionPerformed(ActionEvent evt) {
			    frame.repaint();
			    for(int i=0; i< buttons.length;i++){
			    	buttons[i].repaint();
			    }
			    label.repaint();
			  }
			};
			new Timer(delay, taskPerformer).start();
		
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * Méthode utilisée par le BackgroundDisplay au rythme du Timer.
 */
	public void paint(Graphics g) {
		super.paint(g);
		Y=Y+1; 
		for(int i=(Y%background.getHeight())-background.getHeight();i<background.getHeight();i+=background.getHeight()) {
			
			g.drawImage(background,0,i+background.getHeight(),width,height/2,this); // drawImage(image, positionX,positionY,tailleX,tailleY,this);
			g.drawImage(background,0,i,width,height/2,this); // drawImage(image, positionX,positionY,tailleX,tailleY,this);
			//g.drawImage(background,0,i+4*height,this);
		}
		g.dispose();
	}
///**
// * Test d'affichage de la classe BackgroundDisplay.
// * @param args
// */
//	public static void main(String args[])
//	{
//		JFrame frame = new JFrame();
//		BackgroundDisplay animatedBackground = new BackgroundDisplay(frame,"background.png");
//		frame.setSize(animatedBackground.getWidth(),(int)(animatedBackground.getHeight()*0.75));
//		frame.setVisible(true);
//		frame.add(animatedBackground);
//		//frame.setResizable(false);
//		frame.setLocationRelativeTo(null);
//
//	}

//	public void actionPerformed(ActionEvent e) {
//		repaint();
//	}

	

	public int getHeight()
	{
		return height;
	}

	public int getWidth()
	{
		return width;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	
	

}
