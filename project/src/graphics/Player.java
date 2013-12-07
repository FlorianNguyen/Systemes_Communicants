package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {

	public static BufferedImage avatar; // sprite du vaisseau joueur
	public String name; // pseudo du joueur
	public float score; // score du joueur
	public int[] upgrades; // armes et bonus rang�s dans un tableau
	public int life; // points de vie du joueur
	public int x,y; // position du joueur
	public PlayerShip pShip; // mod�le de vaisseau du joueur
	public static int[][] DEFAULTDAMAGE ={{100,80,60},{110,85,65},{130,90,75}}; 
						// dommages par d�faut en fonction du niveau d'am�lioration

	public Player(String name,int playershipID)
	{
		this.name=name;
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
	
	public void addToScore(int value)
	{
		score+=value;
	}
	
	public void upgrade(int id)
	{
		upgrades[id]++;
	}
	
	public void life(int value)
	{
		life+=value;
	}
	
	public void setXY(int x,int y)
	{
		this.x=x;
		this.y=y;
		
	}
	
	public BufferedImage getSprite()
	{
		return avatar;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getLife()
	{
		return life;
	}
}
