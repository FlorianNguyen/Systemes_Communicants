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

	/**
	 * Constructeur de Client prenant en argument le Player du joueur souhaitant rejoindre une partie.
	 * @param player Player du joueur souhaitant rejoindre la partie
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Client(BackgroundDisplayClient game) 
	{
		toSend = new ArrayList<Bullet>();
		nextToSend = new ArrayList<Bullet>();
		this.game = game;
	}	

	/**
	 * Méthode permettant de se connecter à un Server préexistant, 
	 * et d'échanger avec ce dernier des informations.
	 */
	public void run() 
	{
		try {
			clientSocket = new Socket(InetAddress.getLocalHost(),6789);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new DataInputStream(clientSocket.getInputStream());
			int NUMBER;
			int tempX,tempY,tempID;
			double tempDX,tempDY;
			Bullet tempBullet;
			ArrayList<Bullet> ballArray = new ArrayList<Bullet>();
			long time = System.currentTimeMillis();
			AtomicBoolean spawn = new AtomicBoolean(false);
			AtomicInteger remove = new AtomicInteger(-1);
			outToServer.writeBoolean(true); //Le Client est prêt
			boolean sReady = false;


			while(true) // FAUSSE CONDITION : à changer
			{
				if(!sReady)
				{
					sReady = inFromServer.readBoolean();
					game.enableMultiplayer();

				}
				if(System.currentTimeMillis()-time>50)
				{
					game.canIterate.acquire();
					// PHASE D'ENVOI DES COORDONNEES PLAYER CLIENT + TIR
					outToServer.writeInt(game.getPlayer1().getX());
					//System.out.println(game.getPlayer1().getX());
					outToServer.writeInt(game.getPlayer1().getY());
					//System.out.println(game.getPlayer1().getY());
					//					outToServer.writeBoolean(game.needShooting());

					// RECEPTION COORDONNEES PLAYER SERVEUR + SCORE + LEVEL
					x = inFromServer.readInt();
					y = inFromServer.readInt();
					score = inFromServer.readInt();
					level = inFromServer.readInt();
					game.setPlayer2XY(x,y);

					// PARTIE TIR
					boolean shoot = inFromServer.readBoolean();
					outToServer.writeBoolean(game.needShooting());
					game.setNeedShooting(false);
					if(shoot)
					{
						game.getPlayer2().primaryShooting(game.getPlayerBalls());
					}
					//					game.canIterate.release();
					//					game.getPlayer1().setScore(score);
					//					game.setLevel(level);

					// RECEPTION DES ENNEMIS
					//					spawn.set(inFromServer.readBoolean());
					//					remove.set(inFromServer.readInt());
					//					if(spawn.get())
					//					{
					//						System.out.println(spawn);
					//						spawn.set(false);
					//						int index = inFromServer.readInt();
					//						synchronized(game.getEnemies())
					//						{
					//							if(level%BackgroundDisplayHost.BOSSFREQUENCY==0)
					//							{
					//								game.getEnemies().add(new Enemy(game.background.getWidth()/2,0,BulletType.BASIC_1,Ship.ENEMY_1,level,index));
					//							}
					//							else if(level%BackgroundDisplayHost.BOSSFREQUENCY==1)
					//							{
					//								game.getEnemies().add(new Enemy(game.background.getWidth()/2,0,BulletType.BASIC_1,Ship.ENEMY_1,level,index));
					//							}
					//							else if(level%BackgroundDisplayHost.BOSSFREQUENCY==2)
					//							{
					//								game.getEnemies().add(new Enemy(game.background.getWidth()/2,0,BulletType.BASIC_1,Ship.ENEMY_2,level,index));
					//							}
					//							else if(level%BackgroundDisplayHost.BOSSFREQUENCY==3)
					//							{
					//								game.getEnemies().add(new Enemy(game.background.getWidth()/2,0,BulletType.BASIC_1,Ship.ENEMY_2,level,index));
					//							}
					//							else if(level%BackgroundDisplayHost.BOSSFREQUENCY==4)
					//							{
					//								game.getEnemies().add(new Enemy(game.background.getWidth()/2,0,BulletType.BOSS,Ship.BOSS,level,index));
					//							}
					//						}
					//					}
					//					if(remove.get()!=-1)
					//					{
					//						synchronized(game.getEnemies())
					//						{
					//							for(int i=0;i<game.getEnemies().size();i++)
					//							{
					//								Enemy e = game.getEnemies().get(i);
					//								if(e.getLevelIndex()==remove.get())
					//								{
					//									game.getEnemies().remove(e);
					//									remove.set(-1);
					//								}
					//							}
					//						}
					//					}

					//					// RECEPTION DES BALLES
					//					synchronized(ballArray)
					//					{
					//						ballArray.removeAll(ballArray);
					//						game.resetBalls();
					//						NUMBER = inFromServer.readInt();
					//						for(int i=0;i<NUMBER;i++)
					//						{
					//							tempX = inFromServer.readInt();
					//							tempY = inFromServer.readInt();
					//							tempDX = inFromServer.readDouble();
					//							tempDY = inFromServer.readDouble();
					//							tempID = inFromServer.readInt();
					//							ballArray.add(new Bullet(tempID,tempX,tempY,tempDX,tempDX));
					//						}
					//					}
					//					synchronized(game.getPlayerBalls())
					//					{
					//						game.addBalls(ballArray);
					//					}

				}
				else yield();
			}
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
