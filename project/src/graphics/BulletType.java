package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum BulletType {
	BASIC_ONE(0,100,2),
	BASIC_TWO(1,150,2);
	
	private int id,damage,speed;
	private BufferedImage sprite;
	
	private BulletType(int id,int damage,int speed)
	{
		this.id=id;
		this.damage=damage;
		this.speed=speed;
		if(id==0)
		{
			try {
				sprite = ImageIO.read(new File("projectile_basic.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(id==1)
		{
			try {
				sprite = ImageIO.read(new File("projectile_basic.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public int getID()
	{
		return id;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public BufferedImage getSprite()
	{
		return sprite;
	}
	
}
