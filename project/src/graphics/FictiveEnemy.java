package graphics;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class FictiveEnemy {

	private static BufferedImage sprite1 = Ship.ENEMY_1.getSprite();
	private static BufferedImage sprite2 = Ship.ENEMY_2.getSprite();
	private static BufferedImage sprite3 = Ship.BOSS.getSprite();
	private int x,y,id;

	public FictiveEnemy(int x, int y, int id)
	{
		this.x=x;
		this.y=y;
		this.id=id;
	}

	public void setXY(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public BufferedImage getSprite(int id)
	{
		switch(id)
		{
		case 1 : return sprite1;
		case 2 : return sprite2;
		case 3 : return sprite3;
		default : return null;
		}
	}
}
