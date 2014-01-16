package others;

import server.HostServer;
import game.BackgroundDisplayHost;
import game.BallManagement;
import game.ProcessingThread;

public class Multi {

	public static void main(String args[])
	{
		ProcessingThread pt = new ProcessingThread();
		BackgroundDisplayHost GAME = new BackgroundDisplayHost("Player_1","background2.png");
		HostServer server = new HostServer(GAME,pt);

	}

}
