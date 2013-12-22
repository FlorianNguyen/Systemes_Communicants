package game;
import graphics.Bullet;
import graphics.BulletType;
import graphics.Ennemy;
import graphics.Player;
import graphics.Ship;

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
import java.util.concurrent.Semaphore;

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
	public static final int LOWERLIMIT = 40;
	public static final int RIGHTLIMIT = 5;
	public static final int DEFAULT_FPS=16;
	static final int RELOADTIME = 50;
	public BufferedImage background;
	public int height,width,level,wave;
	public int Y;
	public JFrame frame;
	Ennemy medium;
	public ArrayList<Bullet> availableBalls = new ArrayList<Bullet>(100);
	public ArrayList<Bullet> usedBalls = new ArrayList<Bullet>(100);
	public Timer t = new Timer(DEFAULT_FPS,this);
	public Timer reloadTimer = new Timer(RELOADTIME,this);
	public static final int[][][] levels = 
		{
		{{2,0,0},{2,0,0},{0,3,0}}, //niveau 1
		{{3,0,0},{3,0,0},{0,3,0}}, //niveau 2
		{{3,0,0},{3,0,0},{0,3,0}}, //niveau 3
		{{3,0,0},{3,0,0},{0,3,0}}  // etc
		};

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
			level=1;
			wave=1;
			
			System.out.println(width + "      " + height);

			//Pool de balles
			for(Bullet b:availableBalls)
			{
				b = new Bullet(0,0,0,0,0);
			}
			setLayout(new OverlayLayout(this));
			t.start();

			// Ennemi test
			medium = new Ennemy(background.getWidth()/2,80,BulletType.BASIC_ENNEMY,Ship.ENNEMY_MEDIUM0);

			//Options du JFrame
			frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
					new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB),
					new Point(0,0),"Blank cursor"));
			frame.setSize(this.getWidth(),(int)(this.getHeight()*1));
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
		}

		g.drawImage(
				medium.getShip().getSprite(),
				medium.getX()-(int)medium.getShip().getSprite().getWidth()/2,
				medium.getY()-(int)medium.getShip().getSprite().getHeight()/2,
				this);

		g.drawImage(
				player.getSprite(),
				player.getX()-(int)player.getSprite().getWidth()/2,
				player.getY()-(int)player.getSprite().getHeight()/2+32, //centrage sur la hit-case
				this);

		for(Bullet b:usedBalls)
		{
			b.update();
			g.drawImage(b.getSprite(),b.getX(),b.getY(),this);
			if(!this.isInScreen(b))
			{
				b.setVisible(false);
				availableBalls.add(b);
				usedBalls.remove(b);
			}
		}
		g.dispose();
	}

	/**
	 * Définit l'action réalisée en rythme avec le Timer.
	 */
	public void actionPerformed(ActionEvent e) {
		Point point = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(point, this);
		//System.out.println(point.x + "      " + point.y); //375 525
		if(point.x>0 && point.x<background.getWidth()-RIGHTLIMIT && point.y>0 && point.y<background.getHeight()-LOWERLIMIT)
		{
			player.setXY(point.x,point.y);
		}
		else if(point.x<0)
		{
			if(point.y<0){player.setXY(0,0);}
			else if(point.y>background.getHeight()-LOWERLIMIT){player.setXY(0,background.getHeight()-LOWERLIMIT);}
			else player.setXY(0,point.y);
		}
		else if(point.x>background.getWidth()-RIGHTLIMIT)
		{
			if(point.y<0){player.setXY(background.getWidth()-RIGHTLIMIT,0);}
			else if(point.y>background.getHeight()-LOWERLIMIT){player.setXY(background.getWidth()-RIGHTLIMIT,background.getHeight()-LOWERLIMIT);}
			else player.setXY(background.getWidth()-RIGHTLIMIT,point.y);
		}
		else if(point.y<0)
		{
			if(point.x<0){player.setXY(0, 0);}
			else if(point.x>background.getWidth()-RIGHTLIMIT){player.setXY(background.getWidth()-RIGHTLIMIT,0);}
			else player.setXY(point.x,0);
		}
		else if(point.y>background.getHeight()-LOWERLIMIT)
		{
			if(point.x<0){player.setXY(0,background.getHeight()-LOWERLIMIT);}
			else if(point.x>background.getWidth()-RIGHTLIMIT){player.setXY(background.getWidth()-RIGHTLIMIT,background.getHeight()-LOWERLIMIT);}
			else player.setXY(point.x,background.getHeight()-LOWERLIMIT);
		}
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
}
