package server;

import game.BackgroundDisplay;
import graphics.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;

/**
 * Class Server qui permet au J1 d'hoster la partie sur laquelle il attend le J2.
 * Les deux joueurs s'échangent à intervalle réguliers les informations permettant
 * de reconstruire l'environnement de l'un chez l'autre.
 * @author Florian
 *
 */
public class Server {

	private Player player;
	private int x,y;
	private int size=2;

	public Server(Player p) throws IOException
	{
		player = p;
	}

	/**
	 * Constructeur par défaut de la classe Server
	 */
	public Server()
	{
		
	}
	
	/**
	 * Méthode appelée dans le run.
	 * Elle attribue une socket entre le client du J2 et le Server sur un port à préciser.
	 */
	public void run()
	{
		ServerSocket connectionSocket;
		int clientSentence1, clientSentence2,size;
		try {
			connectionSocket = new ServerSocket(6789);
			Socket socket = connectionSocket.accept();
			DataInputStream inFromClient = new	DataInputStream(socket.getInputStream());	

			while(true)
			{
				size = inFromClient.readInt();
				clientSentence1	=	inFromClient.readInt();	//	lecture	depuis	le	client	
				clientSentence2 = inFromClient.readInt();	
				System.out.println(size + " ; " + clientSentence1 + " ; " + clientSentence2);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main...
	 * @param args
	 */
	public static void main(String args[])
	{
		new Server().run();
	}
}
