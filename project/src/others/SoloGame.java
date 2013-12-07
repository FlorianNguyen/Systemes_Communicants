package others;
import graphics.Bullet;
import graphics.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class SoloGame extends JPanel implements ActionListener {

	private Timer timer; // reste dans Game
	private int height; //adapter au fond d'écran
	private int width; // idem
	//private double bulletSpeed; //propre à chaque munition
	private ArrayList<Bullet> bullets; //propre aux bullets
	private double counter1=100;
	private int counter2=0;
	private boolean goingRight = true;
	private double counter;
	private double playerX, playerY, playerDX, playerDY, mouseX, mouseY;
	static double playerSpeed = 20.0;
	private BufferedImage background;
	private int by;
	public double gameSpeed;
	public Player player;
	public int lvl,wave;
	
	public SoloGame(Player p,BufferedImage bg) {
		try {
			background = ImageIO.read(new File("background.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		height = background.getHeight();
		width = background.getWidth();
		gameSpeed = 3.0; // à choisir par l'utilisateur
		playerDX=0;
		playerDY=0;
		setBackground(Color.BLACK);
        setFocusable(true);
        setDoubleBuffered(true);
        timer = new Timer(16, this);
        JFrame frame = new JFrame();
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB)
        ,new Point(0,0),"Blank cursor"));
		frame.add(this);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setLocation(0,0);
        frame.setResizable(false);
        frame.setVisible(true);    
		playerX = width/2 - player.getSprite().getWidth()/2;
		playerY = 0.8*height - player.getSprite().getHeight()/2;
        bullets = new ArrayList<Bullet>();
        addKeyListener(new TAdapter());
        timer.start();
	}
	
	public void paint(Graphics g) {
        super.paint(g);
        
        by+=1;
        for(int y=(by%background.getHeight())-background.getHeight();y<height;y+=background.getHeight()) {
        	g.drawImage(background,0,y,this);
        }
        
        for(int i=0; i<bullets.size(); i++) {
        	bullets.get(i).update();
        	if(!bullets.get(i).isInScreen(width, height)) { bullets.remove(i); i--; }
        	else { g.drawImage(Bullet.sprite,bullets.get(i).getX()-Bullet.sprite.getWidth()/2,bullets.get(i).getY()-Bullet.sprite.getHeight()/2,this); }
        }
        
        enemyUpdate();
        g.drawImage(boss,(int) (counter1-boss.getWidth()/2)-30,150-boss.getHeight()/2,this);
        counter += 0.01;
        
        g.drawImage(player,(int) playerX,(int) playerY,this);
        playerUpdate();

        g.dispose();
	}
	
	public void actionPerformed(ActionEvent e) {
 
        repaint();  
    }
	
	public void addBullets(double x, double y, int quantity, double angleOffset) {
		
		for(double angle = angleOffset; angle<2*Math.PI+angleOffset; angle += 2*Math.PI/quantity) {
			bullets.add(new Bullet(x,y,bulletSpeed*Math.cos(angle),bulletSpeed*Math.sin(angle)));
		}
	}
	
	private void enemyUpdate() {
		
		if(goingRight) {
			if(counter1 < 500) { counter1 += 2.5; }
			else goingRight = false;
		}
		else {
			if(counter1 > 100) { counter1 -= 2.5; }
			else goingRight = true;
		}
		if(counter2<10) { counter2++; } // HERE IS BULLET FREQUENCY
		else {
			counter2=0;
			addBullets(counter1,150,36,-Math.PI/2+counter); // HERE IS BULLET NUMBER PER SHOT
		}
	}
	
	public void setPlayer(double x, double y) {
		mouseX = x;
		mouseY = y;
	}
	
	private void playerUpdate() {
		
		playerDX = mouseX - playerX;
		playerDY = mouseY - playerY;
	    
		playerDX = Math.max(-playerSpeed, Math.min(playerSpeed, playerDX));
		playerDY = Math.max(-playerSpeed, Math.min(playerSpeed, playerDY));
		
		playerX += playerDX;
        playerY += playerDY;
        
        if(playerX<0) { playerX=0; }
        if(playerX+player.getSprite().getWidth()>width) { playerX = width - player.getSprite().getWidth(); }
        if(playerY<0) { playerY=0; }
        if(playerY+player.getHeight()>height) { playerY = height - player.getHeight(); }
	}

        public void keyPressed(KeyEvent e) {
        	if(e.getKeyCode() == KeyEvent.VK_LEFT) {playerDX=-playerSpeed;}
        	if(e.getKeyCode() == KeyEvent.VK_RIGHT) {playerDX=+playerSpeed;}
        	if(e.getKeyCode() == KeyEvent.VK_UP) {playerDY=-playerSpeed;}
        	if(e.getKeyCode() == KeyEvent.VK_DOWN) {playerDY=playerSpeed;}
        }

    }
}
