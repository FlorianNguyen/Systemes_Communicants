package game;
import graphics.Mouse;
import graphics.Player;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
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
 * Cette classe g�re l'affichage du background mouvant en arri�re-plan.
 * Pour cela il faut lui indiquer le chemin de l'image � utiliser et le JFrame dans lequel
 * afficher.
 * @author Florian Nguyen
 *
 */
public class BackgroundDisplay extends JPanel implements ActionListener{

	Player player;
	public BufferedImage background;
	public int height,width;
	public int Y;
	public JFrame frame;
	public Timer t;
	
/**
 * Constructeur de la classe BackgroundDisplay.
 * @param f JFrame dans lequel l'on souhaite r�aliser l'affichage.
 * @param sourcename Source du fichier image (png) qui sera utilis� en fond d'�cran.
 * L'image doit �tre cyclique, c'est � dire que la partie haute et la partie basse sont compatibles.
 */
	public BackgroundDisplay(Player p,JFrame f,String sourcename)
	{
		try {
			player = p;
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
 * M�thode utilis�e par d�faut par JPanel.
 */
	public void paint(Graphics g) {
		super.paint(g);
		Y+=1;
		for(int i=(Y%background.getHeight())-background.getHeight();i<background.getHeight();i+=background.getHeight()) {
			g.drawImage(background,0,i,this);
			g.drawImage(
					player.getSprite(),
					player.getX()-(int)player.getSprite().getWidth()/2,
					player.getY()-(int)player.getSprite().getHeight()/2,
					this);
		}
		g.dispose();
	}

	/**
	 * D�finit l'action r�alis�e en rythme avec le Timer.
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
	
	/**
	 * Test d'affichage de la classe BackgroundDisplay.
	 * @param args
	 */
		public static void main(String args[])
		{				
					JFrame frame = new JFrame();
					Player player1 = new Player("player_1",1);
					BackgroundDisplay animatedBackground = new BackgroundDisplay(player1,frame,"background2.png");
					Player player = new Player("player_1",1);				
					Mouse mouse = new Mouse(player1,frame);
					mouse.start();
					
					frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
							new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB),
							new Point(0,0),"Blank cursor"));
					frame.setSize(animatedBackground.getWidth(),(int)(animatedBackground.getHeight()*0.75));
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					frame.add(animatedBackground);
					frame.setResizable(false);
					frame.setLocationRelativeTo(null);

		}
}
