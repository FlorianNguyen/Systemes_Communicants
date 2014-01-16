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
	BASIC_PLAYER(0,100*100,0),
	ENEMY_1(1,100*30,1000),
	ENEMY_2(2,100*50,3000),
	BOSS(3,100*80,10000);

	private BufferedImage sprite;
	private int id,life,score;

	/**
	 * Constructeur par défaut de Ship
	 * @param id ID du ship
	 * @param life Points de vie du Ship
	 */
	private Ship(int id, int life, int score)
	{
		try {
			this.id=id;
			this.life=life;
			this.score=score;
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

	public int getScore()
	{
		return score;
	}
	/**
	 * Retourne les points de vie maximum du ship
	 * @return
	 */
	public int getLife(int level)
	{
		return (int)(life*(1+2/Math.PI*Math.atan(Math.PI*0.5/20*level)));
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
