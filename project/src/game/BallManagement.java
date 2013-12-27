package game;

import graphics.Bullet;
import graphics.BulletType;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class BallManagement {

	public static int SIZE=200;
	public static volatile ArrayList<Bullet> playerBalls;
	public static volatile int[] playerAvailable = new int[200];
	public static volatile int[] playerUsed = new int[0];
	public Semaphore pSem = new Semaphore(1);
	public Semaphore playerSemaphore = new Semaphore(SIZE);

	public static volatile ArrayList<Bullet> enemyBalls;
	public static volatile int[] enemyAvailable = new int[200];
	public static volatile int[] enemyUsed = new int[0];
	public Semaphore eSem = new Semaphore(1);
	public Semaphore enemySemaphore = new Semaphore(SIZE);

	public BallManagement()
	{
		playerBalls = new ArrayList<Bullet>();
		enemyBalls = new ArrayList<Bullet>();
		for(int i=0;i<SIZE;i++)
		{
			playerBalls.add(new Bullet(0,-100,0,0,0));
			enemyBalls.add(new Bullet(0,-100,0,0,0));
			playerAvailable[i]=i;
			enemyAvailable[i]=i;
		}
	}

	public void acquirePB(int x,int y, int dx, int dy, int btID)
	{
			try {
				pSem.acquire(); //Droit de modification des array PLAYER
				playerSemaphore.acquire(); //l'un des slots de l'array PLAYER est mobilisé
				int current = remove(playerAvailable,0);
				add(playerUsed,current,0);
				playerBalls.get(current).set(x,y,dx,dy,btID);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				pSem.release(); //Droit de modification cédé quoi qu'il arrive sous peine de blocage
			}
		
	}
	
	public void acquireEB(int x,int y, int dx,int dy, int btID)
	{
		try {
			eSem.acquire();
			enemySemaphore.acquire();
			int current = remove(enemyAvailable,0);
			add(enemyUsed,current,0);
			playerBalls.get(current).set(x, y, dx, dy, btID);			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			eSem.release();
		}
	}

	public void releasePB(int indice)
	{
		try {
			pSem.acquire();
			int current = remove(playerUsed,indice);
			add(playerAvailable,current,0);
			playerSemaphore.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			pSem.release();
		}
	}
	
	public void releaseEB(int indice)
	{
		try {
			eSem.acquire();
			int current =  remove(enemyUsed,indice);
			add(enemyAvailable,current,0);
			enemySemaphore.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			eSem.release();
		}
	}
	
	public int remove(int[] current, int k)
	{
		int[] temp = new int[current.length-1];
		int toReturn = current[k];
		for(int i=0;i<k;i++)
		{
			temp[i]=current[i];
		}
		for(int i=k;k<current.length-1;i++)
		{
			temp[k-1]=current[k];
		}
		current=temp;
		return toReturn;
	}

	public void add(int[] current, int value, int i)
	{
		int[] toReturn = new int[current.length+1];
		for(int k=0;k<i;k++)
		{
			toReturn[k]=current[k];
		}
		toReturn[i]=value;
		for(int k=i+1;k<toReturn.length;k++)
		{
			toReturn[k]=current[k-1];
		}
		current = toReturn;
	}
	
	public ArrayList<Bullet> getBalls()
	{
		ArrayList<Bullet> toReturn = new ArrayList<Bullet>();
		for(int i:playerUsed)
		{
			toReturn.add(playerBalls.get(i));
		}
		for(int i:enemyUsed)
		{
			toReturn.add(enemyBalls.get(i));
		}
		return toReturn;
	}
}
