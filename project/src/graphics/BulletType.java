package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * BulletType est un concept qui sert à définir la notion de type de munitions, 
 * autant pour les ennemis que les joueurs.
 * Les munitions seront caractérisées par un ID, des dommages, et éventuellement une vitesse propre.
 * @author Florian
 *
 */
public enum BulletType {
	BASIC_ONE(0,100,2),
	BASIC_TWO(1,150,2);
	
	private int id,damage,speed;
	private BufferedImage sprite;
	
	/**
	 * Constructeur de l'enum.
	 * @param id ID du type de munitions
	 * @param damage Dommages du type de munitions
	 * @param speed Vitesse des balles
	 */
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

	/**
	 * Retourne l'ID du type de munitions
	 * @return
	 */
	public int getID()
	{
		return id;
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
