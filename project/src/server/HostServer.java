package server;

import game.BackgroundDisplayHost;
import game.BallManagement;
import game.ProcessingThread;
import graphics.Enemy;
import graphics.Player;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;

/**
 * Class Server qui permet au J1 d'hoster la partie sur laquelle il attend le J2.
 * Les deux joueurs s'échangent à intervalle réguliers les informations permettant
 * de reconstruire l'environnement de l'un chez l'autre.
 * @author Florian
 *
 */
public class HostServer extends Thread {

	private BackgroundDisplayHost game;
	private int x,y; // Position du player 2
	private ProcessingThread pt; // Traitement de données et envoi vers la partie
	private boolean playerConnected;

	public HostServer(BackgroundDisplayHost game, ProcessingThread pt)
	{
		this.game = game;
		this.pt = pt;
		playerConnected = false;
	}

	/**
	 * Méthode appelée dans le run.
	 * Elle attribue une socket entre le client du J2 et le Server sur un port à préciser.
	 */
	public void run()
	{
		ServerSocket connectionSocket;
		int NUMBER;
		int tempX,tempY,tempID;
		double tempDX,tempDY;
		ArrayList<Enemy> enemies;
		Enemy tempE;
		try {
			connectionSocket = new ServerSocket(6789); // mobilisation du port 6789
			Socket socket = connectionSocket.accept(); // attente de connexion
			DataInputStream inFromClient = new	DataInputStream(socket.getInputStream()); // réception du flux de données
			DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
			game.enableMultiplayer();
			playerConnected = true;

			while(true)
			{
				// PHASE D'ECOUTE DU SERVEUR
				NUMBER = inFromClient.readInt(); // nombre de balles de J2 qui vont être envoyées 
				x = inFromClient.readInt();
				y = inFromClient.readInt();
				for(int i=0;i<NUMBER;i++)
				{
					tempX = inFromClient.readInt();
					tempY = inFromClient.readInt();
					tempDX = inFromClient.readDouble();
					tempDY = inFromClient.readDouble();
					tempID = inFromClient.readInt();
					pt.process(tempX,tempY,tempDX,tempDY,tempID);
				}
				game.setMultiplayerXY(x,y);
				game.addMultiplayerData(pt.getResult());
				pt.reset();

				// PHASE D'ENVOI DU SERVEUR
				NUMBER = inFromClient.readInt(); // nombre d'ennemis qui vont être envoyés 
				enemies = game.getEnemies();
				for(int i=0;i<NUMBER;i++)
				{
					tempE = enemies.get(i);
					tempX = tempE.getX();
					tempY = tempE.getY();
					tempID = tempE.getShip().getID();
					pt.process(tempX,tempY,tempID);
				}
				game.setMultiplayerXY(x,y);
				game.addMultiplayerData(pt.getResult());
				pt.reset();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean playerConnected()
	{
		return playerConnected;
	}
}
