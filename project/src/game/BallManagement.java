package game;

import graphics.Bullet;
import graphics.BulletType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BallManagement {

	private ArrayList<Bullet> bullets;
	//private ArrayList<Bullet> tempBullets;

	/**
	 * Construction d'un BallManagement avec des array permettant le stockage des informations relatives aux Bullets.
	 */
	public BallManagement()
	{
		bullets = new ArrayList<Bullet>();
		//tempBullets = new ArrayList<Bullet>();
	}

	/**
	 * Méthode de sollicitation d'une des lignes de l'array playerBalls.
	 * Cette ligne est mise aux valeurs spécifiées. Les modifications ne peuvent se faire qu'une par une.
	 * @param x Abscisse du Bullet
	 * @param y Ordonnée du Bullet
	 * @param dx Déplacement selon X
	 * @param dy Déplacement selon Y
	 * @param btID ID du BulletType
	 */
	public void addBall(int x,int y, double dx, double dy,int n)
	{
		bullets.add(new Bullet(n,x,y,dx,dy));
	}

	public void update()
	{
		//for(Bullet b : tempBullets) {
		for(int i=0;i<bullets.size();i++) {
			Bullet b = bullets.get(i);
			b.update();
			if(b.getX()<-30||b.getY()<-30||b.getX()>400||b.getY()>750) {
				bullets.remove(b);
				this.remove(b);
			}
		}
		//tempBullets = new ArrayList<Bullet>(bullets);
	}

	public synchronized void remove(Bullet b)
	{
		bullets.remove(b);
	}
	public ArrayList<Bullet> getBalls()
	{
		//return tempBullets;
		return bullets;
	}

	public boolean isEmpty()
	{
			return (bullets.size()==0);
	}


	//	public static int SIZE=100;
	//	public static volatile int[][] playerBalls;
	//	//	public static volatile ArrayList<Integer> playerAvailable = new ArrayList<Integer>();
	//	//	public static volatile ArrayList<Integer> playerUsed = new ArrayList<Integer>(0);
	//	public static volatile int[] playerAvailable = new int[SIZE];
	//	public static volatile int[] playerUsed = new int[0];
	//	public Semaphore pSem = new Semaphore(1);
	//	public Semaphore playerSemaphore = new Semaphore(SIZE);
	//
	//	/**
	//	 * Construction d'un BallManagement avec des array permettant le stockage des informations relatives aux Bullets.
	//	 */
	//	public BallManagement()
	//	{
	//		playerBalls = new int[SIZE][5];
	//		for(int i=0;i<SIZE;i++)
	//		{
	//			playerBalls[i][0]=0; //X
	//			playerBalls[i][1]=-100; //Y
	//			playerBalls[i][2]=0; //DX
	//			playerBalls[i][3]=0; //DY
	//			playerBalls[i][4]=0; //ID
	//
	//			playerAvailable[i]=i;
	//			//	playerAvailable.add(i);
	//		}
	//	}
	//
	//	/**
	//	 * Méthode de sollicitation d'une des lignes de l'array playerBalls.
	//	 * Cette ligne est mise aux valeurs spécifiées. Les modifications ne peuvent se faire qu'une par une.
	//	 * @param x Abscisse du Bullet
	//	 * @param y Ordonnée du Bullet
	//	 * @param dx Déplacement selon X
	//	 * @param dy Déplacement selon Y
	//	 * @param btID ID du BulletType
	//	 */
	//	public synchronized void acquirePB(int x,int y, int dx, int dy, int btID)
	//	{
	//		try {
	//			pSem.acquire(); //Droit de modification des array PLAYER
	//			playerSemaphore.acquire(); //L'un des slots de l'array PLAYER est mobilisé
	//			int current = playerAvailable[0];
	//			//			int current = playerAvailable.get(0);
	//			//			playerAvailable.remove(0);
	//			//			playerUsed.add(current,0);
	//			playerAvailable = remove(playerAvailable,0); //Une ligne non-utilisée devient utilisée
	//			playerUsed = add(playerUsed,current,0); //Il y a donc une ligne utilisée supplémentaire
	//			playerBalls[current][0]=x-BulletType.getFromID(btID).getSprite().getWidth()/2; // La ligne est mise à la valeur spécifiée
	//			playerBalls[current][1]=y-BulletType.getFromID(btID).getSprite().getHeight()/2;
	//			playerBalls[current][2]=dx;
	//			playerBalls[current][3]=dy;
	//			playerBalls[current][4]=btID;
	//			//System.out.println(playerUsed.length);
	//		} catch (InterruptedException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		finally
	//		{
	//			pSem.release(); //Droit de modification cédé quoi qu'il arrive sous peine de blocage
	//		}
	//	}
	//
	//	public synchronized void releasePB(int indice)
	//	{
	//		System.out.println(" releasePB : ind = "+indice);
	//		try {
	//			pSem.acquire(); // Droit de modification acquis
	//			//			int current = playerUsed.get(indice);
	//			//			playerUsed.remove(indice);
	//			//			playerAvailable.add(current);
	//			int current = playerUsed[indice];
	//			playerUsed = remove(playerUsed,indice); // Une ligne devient utilisable, et n'est plus utilisée
	//			playerAvailable = add(playerAvailable,current,0);
	//			playerSemaphore.release(); // On rend un jeton sur la semaphore Player
	//		} catch (InterruptedException e) {
	//			e.printStackTrace();
	//		} 
	//		finally{
	//			pSem.release(); // Droit de modification rendu
	//		}
	//	}
	//
	//	public synchronized int[] remove(int[] current, int k)
	//	{
	//		System.out.println(" size = "+current.length);
	//		System.out.println(" remove : ind = "+k);
	//		int[] temp = new int[current.length-1];
	//		for(int i=0;i<k;i++)
	//		{
	//			temp[i]=current[i];
	//		}
	//		for(int i=k+1;i<current.length;i++)
	//		{
	//			temp[i-1]=current[i];
	//		}
	//		System.out.println(" size = "+temp.length);
	//		return temp;
	//	}
	//
	//	public void update(int dx, int dy, int indice) //playerBalls
	//	{
	//		playerBalls[indice][0]+=dx;
	//		playerBalls[indice][1]+=dy;
	//		if(playerBalls[indice][0]<0 || playerBalls[indice][1]<0 || playerBalls[indice][0]>400 || playerBalls[indice][1]>750) //380 738
	//		{
	//			//			playerUsed = remove(playerUsed,indice);
	//			//			int toRemove=0;
	//			//			for(int i=0;i<playerUsed.length;i++)
	//			//			{
	//			//				if(i==indice)
	//			//				{
	//			//					toRemove=i;
	//			//				}
	//			//			}
	//			//			playerUsed = remove(playerUsed,toRemove);
	//			//			playerAvailable = add(playerAvailable,toRemove,0);
	//
	//			//			playerUsed = remove(playerUsed,indice);
	//			//			playerAvailable = add(playerAvailable,indice,0);
	//			releasePB(indice);
	//		}
	//	}
	//
	//	public synchronized int[] add(int[] current, int value, int i)
	//	{
	//		int[] toReturn = new int[current.length+1];
	//		for(int k=0;k<i;k++)
	//		{
	//			toReturn[k]=current[k];
	//		}
	//		toReturn[i]=value;
	//		for(int k=i+1;k<toReturn.length;k++)
	//		{
	//			toReturn[k]=current[k-1];
	//		}
	//		return toReturn;
	//	}
	//
	//	public int[][] getBalls()
	//	{
	//		int k=0;
	//		int[][] toReturn = new int[playerUsed.length][5];
	//		for(int i=0;i<playerUsed.length;i++)
	//		{
	//			toReturn[k][0]=playerBalls[playerUsed[i]][0];
	//			toReturn[k][1]=playerBalls[playerUsed[i]][1];
	//			toReturn[k][2]=playerBalls[playerUsed[i]][2];
	//			toReturn[k][3]=playerBalls[playerUsed[i]][3];
	//			toReturn[k][4]=playerBalls[playerUsed[i]][4];
	//			k++;
	//		}
	//		return toReturn;
	//
	//		//		int k=0;
	//		//		int[][] toReturn = new int[playerUsed.size()][5];
	//		//		for(int i=0;i<playerUsed.size();i++)
	//		//		{
	//		//			toReturn[k][0] = playerBalls[playerUsed.get(i)][0];
	//		//			toReturn[k][1] = playerBalls[playerUsed.get(i)][1];
	//		//			toReturn[k][2] = playerBalls[playerUsed.get(i)][2];
	//		//			toReturn[k][3] = playerBalls[playerUsed.get(i)][3];
	//		//			toReturn[k][4] = playerBalls[playerUsed.get(i)][4];
	//		//			k++;
	//		//		}
	//		//		return toReturn;
	//	}
	//
	//	public boolean isEmpty()
	//	{
	//		if(playerUsed.length==0)
	//			//		if(playerUsed.size()==0)
	//		{
	//			return true;
	//		}
	//		else return false;
	//	}
}
