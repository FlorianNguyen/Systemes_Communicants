package gameLaunchers;

import java.net.InetAddress;

import game.BackgroundDisplayClient;
import server.Client;

public class JoinGame {

	
	public static void main(String args[])
	{
		BackgroundDisplayClient game= new BackgroundDisplayClient("Player","background2.png");
		new Thread(game).start();
		Client client = new Client(game,InetAddress.getLocalHost());
		client.start();
	}
}
