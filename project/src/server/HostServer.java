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
	private int x,y; // Position du player 2
	private boolean playerConnected;
	public static long FREQUENCY = 10;
	private long startTime;

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
			int NUMBER;
			int tempX,tempY,tempID;
			double tempDX,tempDY;
			ArrayList<Enemy> enemies;
			Enemy tempE;
			connectionSocket = new ServerSocket(6789); // mobilisation du port 6789
			Socket socket = connectionSocket.accept(); // attente de connexion
			System.out.println("CONNEXION SUCCEEDED");
			inFromClient = new	DataInputStream(socket.getInputStream()); // réception du flux de données
			outToClient = new DataOutputStream(socket.getOutputStream());
			long time = System.currentTimeMillis();
			outToClient.writeBoolean(true);	// le serveur est prêt			
			boolean cReady = false;
			boolean spawn;
			int remove;

			while(true)
			{
				if(!cReady)
				{
					cReady = inFromClient.readBoolean();
					game.enableMultiplayer();
					playerConnected = true;
				}
				if(System.currentTimeMillis()-time>50)
				{
					game.canIterate.acquire();
					// RECEPTION POSITION PLAYER2 + TIR
					x = inFromClient.readInt(); 
					y = inFromClient.readInt();
					//					shoot = inFromClient.readBoolean();

					// ENVOI POSITIONS JOUEUR1
					outToClient.writeInt(game.getPlayer1().getX());
					//System.out.println(game.getPlayer1().getX());
					outToClient.writeInt(game.getPlayer1().getY());
					//System.out.println(game.getPlayer1().getX());
					outToClient.writeInt(game.getPlayer1().getScore());
					outToClient.writeInt(game.getLevel());
					game.setPlayer2XY(x,y);
					//						if(shoot==true)
					//						{
					//							game.getPlayer2().primaryShooting(game.getPlayerBalls());
					//						}

					// ENVOI BULLETS JOUEUR
					outToClient.writeBoolean(game.needShooting());
					game.setNeedShooting(false);
					boolean shoot = inFromClient.readBoolean();
					if(shoot)
					{
						game.getPlayer2().primaryShooting(game.getPlayerBalls());
					}

					// ENVOI ENNEMIS 
					//					spawn = game.needSpawnEnemy();
					//					outToClient.writeBoolean(spawn);
					//					remove = game.getNeedRemove();
					//					outToClient.writeInt(remove);
					//					if(spawn)
					//					{
					//						outToClient.writeInt(game.getLastEnemyIndex());
					//						game.setNeedSpawnEnemy(false);
					//					}
					//					if(remove!=-1)
					//					{
					//						game.setNeedRemove(-1);
					//					}


					//					// ENVOI BALLES JOUEUR
					//					ArrayList<Bullet> balls = game.getPlayerBalls().getBalls();
					//					synchronized(balls)
					//					{
					//						NUMBER = balls.size();
					//						outToClient.writeInt(NUMBER);
					//						for(Bullet b:balls)
					//						{
					//							outToClient.writeInt(b.getX());
					//							outToClient.writeInt(b.getY());
					//							outToClient.writeDouble(b.getDX());
					//							outToClient.writeDouble(b.getDY());
					//							outToClient.writeInt(b.getID());
					//						}
					//					}
					game.canIterate.release();
				}
				else yield();
			}
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
