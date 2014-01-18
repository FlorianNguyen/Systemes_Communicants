package gameLaunchers;

import server.HostServer;
import game.BackgroundDisplayHost;
import game.BallManagement;

public class CreateGame {

	public static void main(String args[])
	{
		long time;
		BackgroundDisplayHost GAME = new BackgroundDisplayHost("Player_1",true,"background2.png");
		Thread game = new Thread(GAME);
		game.start();
		HostServer server = new HostServer(GAME);
		server.start();
//		if(GAME.isOver())
//		{
//			time = System.currentTimeMillis();
//			while(System.currentTimeMillis()-time<500)
//			{
//				game.interrupt();
//				server.interrupt();
//			}
//			
//		}

	}

}
