package game;
import graphics.Bullet;
import graphics.Player;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Cette classe gère l'affichage du background mouvant en arrière-plan.
 * Pour cela il faut lui indiquer le chemin de l'image à utiliser et le JFrame dans lequel
 * afficher.
 * @author Florian Nguyen
 *
 */
public class BackgroundDisplay extends JPanel implements ActionListener {

	Player player;
	static final int RELOADTIME = 50;
	public BufferedImage background;
	public int height,width;
	public int Y;
	public JFrame frame;
	public Timer t = new Timer(16,this);
	public Timer reloadTimer = new Timer(RELOADTIME,this);
	public ArrayList<Bullet> availableBalls = new ArrayList<Bullet>(100);
	public ArrayList<Bullet> usedBalls = new ArrayList<Bullet>(100);
	
/**
 * Constructeur de la classe BackgroundDisplay.
 * @param f JFrame dans lequel l'on souhaite réaliser l'affichage.
 * @param sourcename Source du fichier image (png) qui sera utilisé en fond d'écran.
 * L'image doit être cyclique, c'est à dire que la partie haute et la partie basse sont compatibles.
 */
	public BackgroundDisplay(String name,int id,String sourcename)
	{
		try {
			//Initialisations
			player = new Player(name,id);
			frame = new JFrame();
			background = ImageIO.read(new File(sourcename));
			height = background.getHeight();
			width = background.getWidth();
			for(Bullet b:availableBalls)
			{
				b = new Bullet(0,0,0,0,0);
			}
			setLayout(new OverlayLayout(this));
			t.start();
			
			//Options du JFrame
			frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
					new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB),
					new Point(0,0),"Blank cursor"));
			frame.setSize(this.getWidth(),(int)(this.getHeight()*0.75));
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.add(this);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
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
			g.drawImage(
					player.getSprite(),
					player.getX()-(int)player.getSprite().getWidth()/2,
					player.getY()-(int)player.getSprite().getHeight()/2+32, //centrage sur la hit-case
					this);
		}
		g.dispose();
	}

	/**
	 * Définit l'action réalisée en rythme avec le Timer.
	 */
	public void actionPerformed(ActionEvent e) {
		Point point = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(point, this);
		System.out.println(player.x + "      " + player.y); //375 525
		player.setXY(point.x,point.y);
		repaint();
	}

	public boolean isInScreen(Bullet b)
	{
		boolean p=true;
		if(b.getX()<0||b.getX()>background.getWidth()||b.getY()<0||b.getY()>background.getHeight())
		{
			p=false;
		}
		return p;
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
	
	public void cleanBalls(){
		for(Bullet e: usedBalls)
		{
			if(!this.isInScreen(e))
			{
				e.setVisible(false);
				e.reset();
				usedBalls.remove(e);
				availableBalls.add(e);
			}
		}
	}
}
