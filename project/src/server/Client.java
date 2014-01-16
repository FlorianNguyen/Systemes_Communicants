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
 * Classe Client permettant au J2 de se connecter � la partie du J1, de recevoir
 * les donn�es de la partie h�berg�e sur la machine du J1, et d'envoyer celles qui
 * permettront � ce dernier d'obtenir les informations relatives au J2. 
 * Cet �change est fait � intervalle r�gulier gr�ce � un Timer et � l'h�ritage des propriet�s
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
	 * M�thode permettant de se connecter � un Server pr�existant, 
	 * et d'�changer avec ce dernier des informations.
	 */
	public void run() 
	{
		try {
			clientSocket = new Socket("25.111.33.223",6789);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new DataInputStream(clientSocket.getInputStream());
			int NUMBER;
			int tempX,tempY,tempID;
			double tempDX,tempDY;
			Bullet tempBullet;
			ArrayList<Bullet> balls = game.getPlayerBalls();

			while(!game.isOver()) // FAUSSE CONDITION : � changer
			{
				// PHASE D'ENVOI DU CLIENT
				NUMBER = balls.size();
				outToServer.writeInt(NUMBER); System.out.println("number sent");
				outToServer.writeInt(game.getX()); System.out.println("x sent");
				outToServer.writeInt(game.getY()); System.out.println("y sent");
				for(int i=0;i<NUMBER;i++)
				{
					tempBullet = balls.get(i);
					outToServer.writeInt(tempBullet.getX());
					outToServer.writeInt(tempBullet.getY());
					outToServer.writeDouble(tempBullet.getDX());
					outToServer.writeDouble(tempBullet.getDY());
					outToServer.writeInt(tempBullet.getID());
					System.out.println(tempBullet.getX() + "   ;   " +tempBullet.getY());
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
			clientSocket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
