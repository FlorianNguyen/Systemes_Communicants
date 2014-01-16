package others;

import game.BackgroundDisplayClient;
import game.ProcessingThread;
import server.Client;

public class JoinGame {

	
	public static void main(String args[])
	{
		BackgroundDisplayClient game= new BackgroundDisplayClient("Player","background2.png");
		ProcessingThread pt = new ProcessingThread();
		Client client = new Client(game,pt);
		pt.start();
		client.start();
	}
}
