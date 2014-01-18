package server;

import game.BackgroundDisplayHost;
import graphics.Enemy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Class Server qui permet au J1 d'hoster la partie sur laquelle il attend le J2.
 * Les deux joueurs s'échangent à intervalle réguliers les informations permettant
 * de reconstruire l'environnement de l'un chez l'autre.
 * @author Florian
 *
 */
public class HostServer extends Thread {

	DataInputStream inFromClient;
	DataOutputStream outToClient;
	private BackgroundDisplayHost game;
	private int x,y,life; // Position du player 2 et vie
	private boolean playerConnected;
	public static long FREQUENCY = 10;
	private long startTime;

	/**
	 * Constructeur par défaut.
	 * @param game Partie à considérer
	 */
	public HostServer(BackgroundDisplayHost game)
	{
		startTime = System.currentTimeMillis();
		this.game = game;
		playerConnected = false;
	}

	/**
	 * Méthode appelée dans le run.
	 * Elle attribue une socket entre le client du J2 et le Server sur un port à préciser.
	 */
	public void run()
	{
		try {
			ServerSocket connectionSocket;
			connectionSocket = new ServerSocket(6789); // mobilisation du port 6789
			Socket socket = connectionSocket.accept(); // attente de connexion
			System.out.println("CONNEXION SUCCEEDED");
			inFromClient = new	DataInputStream(socket.getInputStream()); // réception du flux de données
			outToClient = new DataOutputStream(socket.getOutputStream());
			long time = System.currentTimeMillis();
			outToClient.writeBoolean(true);	// le serveur est prêt			
			boolean cReady = false;

			while(!game.isOver())
			{
				if(!cReady)
				{
					// CONFIRMATION POUR DEMARRAGE PSEUDO-"SIMULTANE"
					cReady = inFromClient.readBoolean();
					game.enableMultiplayer();
					playerConnected = true;
				}
				if(System.currentTimeMillis()-time>50)
				{
					// PRISE D'UN JETON SEMAPHORE
					game.canIterate.acquire();
					
					// RECEPTION POSITION PLAYER2 + TIR
					x = inFromClient.readInt(); 
					y = inFromClient.readInt();
					life = inFromClient.readInt();

					// ENVOI POSITIONS JOUEUR1
					outToClient.writeInt(game.getPlayer1().getX());
					outToClient.writeInt(game.getPlayer1().getY());
					outToClient.writeInt(game.getPlayer1().getScore());
					game.setPlayer2XY(x,y);
					game.getPlayer2().setLife(life);

					// ENVOI BULLETS JOUEUR
					outToClient.writeBoolean(game.needShooting());
					inFromClient.readBoolean();
					game.setNeedShooting(false);
					boolean shoot = inFromClient.readBoolean();
					inFromClient.readBoolean();
					if(shoot)
					{
						game.getPlayer2().primaryShooting(game.getPlayerBalls());
					}
					
					game.canIterate.release();
				}
				else yield();
			}
			
			socket.close();
			connectionSocket.close();
//			time = System.currentTimeMillis();
//			while(System.currentTimeMillis()-time<1000){}
//			System.out.println(game.getScore());
//			game.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean playerConnected()
	{
		return playerConnected;
	}
}
