package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Classe BulletType définissant le type de munitions utilisées par un Ship, un Player...
 * Les munitions seront caractérisées par un ID, des dommages, et éventuellement une vitesse propre.
 * @author Florian
 *
 */
public enum BulletType {
	BASIC_ENNEMY(0,100,2,20),
	BASIC_PLAYER(1,150,2,15);
	
	private int id,damage,speed,reloadTime;
	private BufferedImage sprite;
	
	/**
	 * Constructeur de l'enum.
	 * @param id ID du type de munitions
	 * @param damage Dommages du type de munitions
	 * @param speed Vitesse des balles
	 */
	private BulletType(int id,int damage,int speed,int reload)
	{
		this.id=id;
		this.damage=damage;
		this.speed=speed;
		this.reloadTime=reload;
			try {
				sprite = ImageIO.read(new File("projectile_basic"+id+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	/**
	 * Retourne l'ID du type de munitions
	 * @return
	 */
	public int getID()
	{
		return id;
	}
	
	public int getReloadTime()
	{
		return reloadTime;
	}
	
	public static BulletType getFromID(int id)
	{
		switch(id)
		{
		case 0 : return BASIC_ENNEMY;
		case 1 : return BASIC_PLAYER;
		default : return BASIC_PLAYER;
		}
	}
	
	/**
	 * Retourne la vitesse du type de munitions
	 */
	public int getSpeed()
	{
		return speed;
	}
	
	/**
	 * Retourne les dommages par défaut du type de munitions
	 * @return
	 */
	public int getDamage()
	{
		return damage;
	}
	
	/**
	 * Retourne le sprite du type de munitions.
	 * @return
	 */
	public BufferedImage getSprite()
	{
		return sprite;
	}
	
}
