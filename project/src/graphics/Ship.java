package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Ship {
	BASIC_CRUISER(0,500),
	ENNEMY_MEDIUM0(0,5000);

	public BufferedImage sprite;
	public int id,life;
	public boolean isAlive;

	private Ship(int id, int life)
	{
		try {
			this.id=id;
			this.life=life;
			sprite = ImageIO.read(new File("enemy_medium"+id+".png"));
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

	public BufferedImage getSprite()
	{
		return sprite;
	}

	public boolean isAlive()
	{
		return isAlive;
	}
}
