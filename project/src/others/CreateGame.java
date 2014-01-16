package others;

import server.HostServer;
import game.BackgroundDisplayHost;
import game.BallManagement;
import game.ProcessingThread;

public class CreateGame {

	public static void main(String args[])
	{
		ProcessingThread pt = new ProcessingThread();
		pt.start();
		BackgroundDisplayHost GAME = new BackgroundDisplayHost("Player_1",true,"background2.png");
		HostServer server = new HostServer(GAME,pt);
		server.start();

	}

}
