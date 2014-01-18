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
	BASIC_PLAYER(0,100*100,0,0),
	FRIEND(-1,100*100,0,0),
	ENEMY_1(1,100*30,1000,10),
	ENEMY_2(2,100*50,3000,20),
	BOSS(3,100*80,10000,100);

	private BufferedImage sprite;
	private int id,life,score,xp;

	/**
	 * Constructeur par défaut de Ship
	 * @param id ID du ship
	 * @param life Points de vie du Ship
	 */
	private Ship(int id, int life, int score,int xp)
	{
		try {
			this.id=id;
			this.life=life;
			this.score=score;
			this.xp=xp;
			if(id>0)
			{
				sprite = ImageIO.read(new File("enemy"+id+".png"));
			}
			else if(id<0)
			{
				sprite = ImageIO.read(new File("shipfriend.png"));
			}
			else if(id==0)
			{
				sprite = ImageIO.read(new File("ship0.png"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getXP()
	{
		return xp;
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
