package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Turret {
	DEFAULT_ONE(0,1000,100),
	DEFAULT_TWO(1,750,500);

	private int id;
	private int life;
	private int damage;
	public BufferedImage sprite;

	private Turret(int id,int life,int dmg)
	{
		this.id=id;
		this.life=life;
		damage=dmg;
		try {
			sprite = ImageIO.read(new File("turret_"+id));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getID()
	{
		return id;
	}

	public int getLife()
	{
		return life;
	}

	public int getDamage()
	{
		return damage;
	}
	
	public void explosion()
	{
		//à compléter
	}




}
