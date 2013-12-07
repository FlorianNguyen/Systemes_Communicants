package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Si l'on décide de laisser au joueur le choix de son vaisseau, 
 * ce dernier peut alors choisir son vaisseau parmi ceux d'une liste figurant 
 * dans la classe PlayerShip. Chaque vaisseau a des caractéristiques particulières.
 * @author Florian
 *
 */
public enum PlayerShip {
	FIGHTER_ONE(0,10000),
	FIGHTER_TWO(1,12000);

	public static int[][] DEFAULTDAMAGE ={{100,80,60},{110,85,65},{130,90,75}};
	public int id;
	public int life;
	public BufferedImage sprite;

	/**
	 * Constructeur de PlayerShip
	 * @param id ID du PlayerShip
	 * @param life vie par défaut du PlayerShip
	 */
	private PlayerShip(int id, int life)
	{
		this.id=id;
		this.life=life;
		try {
			sprite = ImageIO.read(new File("playership_"+id+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Retourne le PlayerShip associé à un ID donné.
	 * @param id ID du PlayerShip à retourner.
	 * @return
	 */
	public static PlayerShip getShip(int id)
	{
		if(id==0){return FIGHTER_ONE;}
		else{return FIGHTER_TWO;}
	}


}
