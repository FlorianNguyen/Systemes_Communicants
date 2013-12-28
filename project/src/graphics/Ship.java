package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Classe Ship définissant les types de vaisseaux pouvant constituer les Enemy.
 * @author Florian
 *
 */
public enum Ship {
	BASIC_CRUISER(0,500),
	ENNEMY_MEDIUM0(0,5000);

	public BufferedImage sprite;
	public int id,life;

	/**
	 * Constructeur par défaut de Ship
	 * @param id ID du ship
	 * @param life Points de vie du Ship
	 */
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

	/**
	 * Retourne l'ID du Ship
	 * @return
	 */
	public int getID()
	{
		return id;
	}

	/**
	 * Retourne les points de vie maximum du ship
	 * @return
	 */
	public int getLife()
	{
		return life;
	}

	/**
	 * Retourne le sprite du Ship
	 * @return le sprite du Ship
	 */
	public BufferedImage getSprite()
	{
		return sprite;
	}
}
