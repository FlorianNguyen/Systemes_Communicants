package graphics;

import game.BallManagement;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Le Player doit regrouper toutes les informations propres à un joueur.
 * On y trouvera donc le pseudo du joueur, l'ID, etc, sa position sur l'écran, etc.
 * Le player sera utilisé pour l'affichage graphique, associé à un objet Mouse.
 * @author Florian
 *
 */
public class Player {

	public static BufferedImage avatar; // sprite du vaisseau joueur
	public String name; // pseudo du joueur
	public float score; // score du joueur
	public int[] upgrades; // armes et bonus ranges dans un tableau
	public int life; // points de vie du joueur
	public int x,y; // position du joueur
	public PlayerShip pShip; // modele de vaisseau du joueur
	public static int[][] DEFAULTDAMAGE ={{100,80,60},{110,85,65},{130,90,75}};
						// dommages par defaut en fonction du niveau d'amelioration
	public ArrayList<Bullet> balls = new ArrayList<Bullet>();
	public Semaphore shootSem = new Semaphore(1);

	/**
	 * Constructeur de la classe Player.
	 * @param name Pseudo du joueur
	 * @param playershipID ID du joueur
	 */
	public Player(String name,int playershipID)
	{
		this.name=name;
		
		//Position par défaut du joueur lorsqu'il apparaît
		x = 190;
		y = 400;
		
		life = 10000;
		score = 0;
		upgrades = new int[5];
		upgrades[0]=0;
		upgrades[1]=0;
		upgrades[2]=0;
		upgrades[3]=0;
		upgrades[4]=0;
		pShip = PlayerShip.getShip(playershipID);
		try {
			avatar = ImageIO.read(new File("left_11.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'incrémenter le score de la partie
	 * @param value Valeur à augmenter au score.
	 */
	public void addToScore(int value)
	{
		score+=value;
	}
	
	public void upgrade(int id)
	{
		upgrades[id]++;
	}
	
	/**
	 * Permet d'incrémenter la vie du joueur.
	 * @param value valeur à ajouter à la vie actuelle du joueur (peut être négative...)
	 */
	public void life(int value)
	{
		life+=value;
	}
	
	/**
	 * Modifie la position du joueur.
	 * @param x Nouvelle abscisse du joueur
	 * @param y Nouvelle ordonnée du joueur
	 */
	public void setXY(int x,int y)
	{
		this.x=x;
		this.y=y;
		
	}
	
	/**
	 * Retourne le sprite du player
	 * @return le sprite associé au player
	 */
	public BufferedImage getSprite()
	{
		return avatar;
	}
	
	/**
	 * Retourne l'ascisse X du joueur.
	 * @return l'abscisse X du joueur.
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * Retourne l'ordonnée Y du joueur.
	 * @return l'ordonnée Y du joueur.
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * La vie du joueur
	 * @return la vie du joueur
	 */
	public int getLife()
	{
		return life;
	}
	
	public void primaryShooting(BallManagement bm)
	{
		if(upgrades[0]==0)
		{
			try {
				shootSem.acquire();
				bm.acquirePB(x,y,0,5,BulletType.BASIC_PLAYER.getID());
				this.wait(BulletType.BASIC_PLAYER.getReloadTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(upgrades[1]==1)
		{
			try {
				bm.acquirePB(x-2,y,0,5,BulletType.BASIC_PLAYER.getID());
				bm.acquirePB(x+2,y,0,5,BulletType.BASIC_PLAYER.getID());
				this.wait(BulletType.BASIC_PLAYER.getReloadTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(upgrades[2]==2)
		{
			try {
				bm.acquirePB(x-3,y-2,0,5,BulletType.BASIC_PLAYER.getID());
				bm.acquirePB(x+3,y-2,0,5,BulletType.BASIC_PLAYER.getID());
				bm.acquirePB(x,y+1,0,5,BulletType.BASIC_PLAYER.getID());
				this.wait(BulletType.BASIC_PLAYER.getReloadTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		shootSem.release();
	}
}
