package server;

import game.BackgroundDisplayClient;
import graphics.Bullet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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

	private Socket clientSocket;
	private DataOutputStream outToServer;
	private DataInputStream inFromServer;
	private BackgroundDisplayClient game;
	private ArrayList<Bullet> toSend,nextToSend;
	private int x,y,score,level;
	private InetAddress address;

	/**
	 * Constructeur de Client prenant en argument le Player du joueur souhaitant rejoindre une partie.
	 * @param player Player du joueur souhaitant rejoindre la partie
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Client(BackgroundDisplayClient game, String host) 
	{
		toSend = new ArrayList<Bullet>();
		nextToSend = new ArrayList<Bullet>();
		this.game = game;
		try {
			address=InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	/**
	 * Méthode permettant de se connecter à un Server préexistant, 
	 * et d'échanger avec ce dernier des informations.
	 */
	public void run() 
	{
		try {

			//CREATIONS ET CONNEXION
			clientSocket = new Socket(address,6789);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new DataInputStream(clientSocket.getInputStream());

			long time = System.currentTimeMillis();
			AtomicBoolean spawn = new AtomicBoolean(false);
			AtomicInteger remove = new AtomicInteger(-1);
			outToServer.writeBoolean(true); //Le Client est prêt
			boolean sReady = false;


			while(!game.isOver()) // FAUSSE CONDITION : à changer
			{
				if(!sReady)
				{
					sReady = inFromServer.readBoolean();
					game.enableMultiplayer();

				}
				if(System.currentTimeMillis()-time>50)
				{
					// PRISE D'UN JETON SEMAPHORE
					game.canIterate.acquire();

					// PHASE D'ENVOI DES COORDONNEES PLAYER CLIENT + TIR
					outToServer.writeInt(game.getPlayer1().getX());
					outToServer.writeInt(game.getPlayer1().getY());
					outToServer.writeInt(game.getPlayer1().getLife());

					// RECEPTION COORDONNEES PLAYER SERVEUR + SCORE + LEVEL
					x = inFromServer.readInt();
					y = inFromServer.readInt();
					score = inFromServer.readInt();
					game.setPlayer2XY(x,y);
					game.getPlayer1().setScore(score);

					// PARTIE TIR
					boolean shoot = inFromServer.readBoolean();
					outToServer.writeBoolean(true);
					outToServer.writeBoolean(game.needShooting());
					outToServer.writeBoolean(true);
					game.setNeedShooting(false);
					if(shoot)
					{
						game.getPlayer2().primaryShooting(game.getPlayerBalls());
					}
				}
				else yield();
			}
			
			clientSocket.close();
			time = System.currentTimeMillis();
			while(System.currentTimeMillis()-time<1000){}
			System.out.println(game.getScore());
			game.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
