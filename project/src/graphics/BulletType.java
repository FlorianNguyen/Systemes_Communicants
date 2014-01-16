package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Classe BulletType d�finissant le type de munitions utilis�es par un Ship, un Player...
 * Les munitions seront caract�ris�es par un ID, des dommages, et �ventuellement une vitesse propre.
 * @author Florian
 *
 */
public enum BulletType {
	BASIC_1(1,50,5,200),
	BASIC_2(2,100,5,300),
	BOSS(3,200,6,500),
	BASIC_PLAYER(0,100,15,100);
	
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
		case 1 : return BASIC_1;
		case 2 : return BASIC_2;
		case 3 : return BOSS;
		case 0 : return BASIC_PLAYER;
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
	 * Retourne les dommages par d�faut du type de munitions
	 * @return
	 */
	public int getDamage(int level)
	{
		return (int)(damage*0.8+damage*2*2/Math.PI*Math.atan(Math.PI*0.5/20*level));
		// en 51 niveaux les dommages seront multipli�s par 2.5, avec une limite � l'infini de 2.8
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
