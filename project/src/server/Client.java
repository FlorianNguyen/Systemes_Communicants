package server;

import game.BackgroundDisplayClient;
import game.BackgroundDisplayHost;
import game.ProcessingThread;
import graphics.Bullet;
import graphics.Player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Classe Client permettant au J2 de se connecter à la partie du J1, de recevoir
 * les données de la partie hébergée sur la machine du J1, et d'envoyer celles qui
 * permettront à ce dernier d'obtenir les informations relatives au J2. 
 * Cet échange est fait à intervalle régulier grâce à un Timer et à l'héritage des proprietés
 * de ActionListener.
 * @author Florian
 *
 */
public class Client extends Thread {

	DataInputStream inFromUser;
	Socket clientSocket;
	DataOutputStream outToServer;
	DataInputStream inFromServer;
	BackgroundDisplayClient game;
	ProcessingThread pt;
	int x,y,score;

	/**
	 * Constructeur de Client prenant en argument le Player du joueur souhaitant rejoindre une partie.
	 * @param player Player du joueur souhaitant rejoindre la partie
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Client(BackgroundDisplayClient game, ProcessingThread pt) 
	{
		this.game = game;
		this.pt = pt;
	}	

	/**
	 * Méthode permettant de se connecter à un Server préexistant, 
	 * et d'échanger avec ce dernier des informations.
	 */
	public void run() 
	{
		try {
			clientSocket = new Socket("localhost",6789);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new DataInputStream(clientSocket.getInputStream());
			int NUMBER;
			int tempX,tempY,tempID;
			double tempDX,tempDY;
			Bullet tempBullet;
			ArrayList<Bullet> balls = game.getPlayerBalls();

			while(true)
			{
				// PHASE D'ENVOI DU CLIENT
				NUMBER = balls.size();
				outToServer.writeInt(NUMBER);
				outToServer.writeInt(game.getX());
				outToServer.writeInt(game.getY());
				for(int i=0;i<NUMBER;i++)
				{
					tempBullet = balls.get(i);
					outToServer.writeInt(tempBullet.getX());
					outToServer.writeInt(tempBullet.getY());
					outToServer.writeDouble(tempBullet.getDX());
					outToServer.writeDouble(tempBullet.getDY());
					outToServer.writeInt(tempBullet.getID());
				}

				// PHASE D'ECOUTE DU CLIENT
				
				// BULLETS
				NUMBER = inFromServer.readInt();
				for(int i=0;i<NUMBER;i++)
				{
					tempX = inFromServer.readInt();
					tempY = inFromServer.readInt();
					tempDX = inFromServer.readDouble();
					tempDY = inFromServer.readDouble();
					tempID = inFromServer.readInt();
					pt.process(tempX, tempY, tempDX, tempDY, tempID);
				}
				game.addMultiplayerData(pt.getResult());
				pt.reset();
				
				// ENNEMIS
				NUMBER = inFromServer.readInt();
				for(int i=0;i<NUMBER;i++)
				{
					tempX = inFromServer.readInt();
					tempY = inFromServer.readInt();
					tempID = inFromServer.readInt();
					pt.process(tempX, tempY, tempID);
				}
				game.addMultiplayerData(pt.getResult());
				pt.reset();

			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
