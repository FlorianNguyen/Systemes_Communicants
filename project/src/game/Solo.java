package game;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import graphics.*;
/**
 * Modifié par Florian
 * @author Etienne
 *
 *
 */
public class Solo {
	BufferedImage background;
	Player player;

	public Solo(File f, Player p)
	{
		try {
			background = ImageIO.read(f);
			player = p;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
