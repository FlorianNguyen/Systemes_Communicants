package game;
import graphics.Player;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.Timer;

import others.Bullet;

/**
 * Cette classe gère l'affichage du background mouvant en arrière-plan.
 * Pour cela il faut lui indiquer le chemin de l'image à utiliser et le JFrame dans lequel
 * afficher.
 * @author Florian Nguyen
 *
 */
public class BackgroundDisplay extends JPanel implements ActionListener{

	public BufferedImage background;
	public int height,width;
	public int Y;
	public JFrame frame;
	public Timer t;
	
/**
 * Constructeur de la classe BackgroundDisplay.
 * @param f JFrame dans lequel l'on souhaite réaliser l'affichage.
 * @param sourcename Source du fichier image (png) qui sera utilisé en fond d'écran.
 * L'image doit être cyclique, c'est à dire que la partie haute et la partie basse sont compatibles.
 */
	public BackgroundDisplay(JFrame f,String sourcename)
	{
		try {
			frame = f;
			background = ImageIO.read(new File(sourcename));
			height = background.getHeight();
			width = background.getWidth();
			t = new Timer(16,this);
			setLayout(new OverlayLayout(this));
			t.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * Méthode utilisée par défaut par JPanel.
 */
	public void paint(Graphics g) {
		super.paint(g);
		Y+=1;
		for(int i=(Y%background.getHeight())-background.getHeight();i<background.getHeight();i+=background.getHeight()) {
			g.drawImage(background,0,i,this);
			g.drawImage(background,0,i+2*background.getHeight(),this);
		}
		g.dispose();
	}
/**
 * Test d'affichage de la classe BackgroundDisplay.
 * @param args
 */
	public static void main(String args[])
	{
		JFrame frame = new JFrame();
		BackgroundDisplay animatedBackground = new BackgroundDisplay(frame,"background2.png");
		frame.setSize(animatedBackground.getWidth(),(int)(animatedBackground.getHeight()*0.75));
		frame.setVisible(true);
		frame.add(animatedBackground);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

	}

	/**
	 * Définit l'action réalisée en rythme avec le Timer.
	 */
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	/**
	 * Renvoie la hauteur de l'image servant de background, et donc du background.  
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Renvoie la largeur du background/de l'image servant de background.
	 */
	public int getWidth()
	{
		return width;
	}

}
