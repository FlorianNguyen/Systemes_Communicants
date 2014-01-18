package gameLaunchers;

import game.BackgroundDisplayHost;
import game.BallManagement;

public class Solo {

	public static void main(String args[])
	{
		BackgroundDisplayHost GAME = new BackgroundDisplayHost("Player_1",false,"background2.png");
		new Thread(GAME).start();

	}

}
