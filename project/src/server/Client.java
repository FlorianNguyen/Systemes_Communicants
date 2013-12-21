package server;

import game.BackgroundDisplay;
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
public class Client implements ActionListener {

	DataInputStream inFromUser;
	Socket clientSocket;
	DataOutputStream outToServer;
	BufferedReader inFromServer;
	Player p;
	int x,y;
	int size;
	Timer timer = new Timer(25,this);

	/**
	 * Constructeur de Client prenant en argument le Player du joueur souhaitant rejoindre une partie.
	 * @param player Player du joueur souhaitant rejoindre la partie
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Client(Player player) {
		p = player;
		try {
			clientSocket = new Socket("localhost",6789);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			size = 2;
		} catch (IOException e) {
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
			clientSocket = new Socket("localhost",6789);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			int x,y,size;
			size = 2;

			while(true)
			{
				outToServer.writeInt(size);
				x = p.getX();
				y = p.getY();
				outToServer.writeInt(x);
				outToServer.writeInt(y);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main...
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException
	{
		JFrame frame = new JFrame();
		Player p = new Player("player_1",1);
		BackgroundDisplay display = new BackgroundDisplay("Player_2",1,"background2.png");
		Client client = new Client(p);
		client.timer.start();
	}

	/**
	 * Méthode définissant ce qui sera réalisé à chaque incrémentation du Timer.
	 * Toutes les 25ms, le Client prend les positions du Player et les envoie.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		x = p.getX();
		y = p.getY();
		try {
			outToServer.writeInt(size);
			outToServer.writeInt(x);
			outToServer.writeInt(y);
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	}
}
