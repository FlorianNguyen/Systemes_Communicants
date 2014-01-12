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
	BASIC_PLAYER(0,100*100),
	ENEMY_1(1,100*30),
	ENEMY_2(2,100*70),
	BOSS(3,100*300);

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
			if(id!=0)
			{
				sprite = ImageIO.read(new File("enemy"+id+".png"));
			}
			else
			{
				sprite = ImageIO.read(new File("ship"+id+".png"));
			}
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
