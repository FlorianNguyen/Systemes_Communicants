package game;

import graphics.Bullet;
import graphics.BulletType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class BallManagement {

	public static int SIZE=100;
	public static volatile int[][] playerBalls;
	public static volatile int[] playerAvailable = new int[SIZE];
	public static volatile int[] playerUsed = new int[0];
	public Semaphore pSem = new Semaphore(1);
	public Semaphore playerSemaphore = new Semaphore(SIZE);

//	public static volatile int[][] enemyBalls;
//	public static volatile int[] enemyAvailable = new int[SIZE];
//	public static volatile int[] enemyUsed = new int[0];
//	public Semaphore eSem = new Semaphore(1);
//	public Semaphore enemySemaphore = new Semaphore(SIZE);

	/**
	 * Construction d'un BallManagement avec des array permettant le stockage des informations relatives aux Bullets.
	 */
	public BallManagement()
	{
		playerBalls = new int[SIZE][5];
//		enemyBalls = new int[SIZE][5];
		for(int i=0;i<SIZE;i++)
		{
			playerBalls[i][0]=0; //X
			playerBalls[i][1]=-100; //Y
			playerBalls[i][2]=0; //DX
			playerBalls[i][3]=0; //DY
			playerBalls[i][4]=0; //ID

//			enemyBalls[i][0]=0;
//			enemyBalls[i][1]=-100;
//			enemyBalls[i][2]=0;
//			enemyBalls[i][3]=0;
//			enemyBalls[i][4]=0;

			playerAvailable[i]=i;
//			enemyAvailable[i]=i;
		}
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
	public void acquirePB(int x,int y, int dx, int dy, int btID)
	{
		try {
			pSem.acquire(); //Droit de modification des array PLAYER
			playerSemaphore.acquire(); //L'un des slots de l'array PLAYER est mobilisé
			int current = playerAvailable[0];
			playerAvailable = remove(playerAvailable,0); //Une ligne non-utilisée devient utilisée
			playerUsed = add(playerUsed,current,0); //Il y a donc une ligne utilisée supplémentaire
			playerBalls[current][0]=x-BulletType.getFromID(btID).getSprite().getWidth()/2; // La ligne est mise à la valeur spécifiée
			playerBalls[current][1]=y-BulletType.getFromID(btID).getSprite().getHeight()/2;
			playerBalls[current][2]=dx;
			playerBalls[current][3]=dy;
			playerBalls[current][4]=btID;
			System.out.println(playerUsed.length);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			pSem.release(); //Droit de modification cédé quoi qu'il arrive sous peine de blocage

		}
	}

//	public void acquireEB(int x,int y, int dx,int dy, int btID)
//	{
//		try {
//			eSem.acquire(); //Droit de modification des array PLAYER
//			enemySemaphore.acquire(); //L'un des slots de l'array PLAYER est mobilisé
//			int current = enemyAvailable[0];
//			enemyAvailable = remove(enemyAvailable,0); //Une ligne non-utilisée devient utilisée
//			enemyUsed = add(enemyUsed,current,0); //Il y a donc une ligne utilisée supplémentaire
//			enemyBalls[current][0]=x-BulletType.getFromID(btID).getSprite().getWidth()/2; // La ligne est mise à la valeur spécifiée
//			enemyBalls[current][1]=y-BulletType.getFromID(btID).getSprite().getHeight()/2;
//			enemyBalls[current][2]=dx;
//			enemyBalls[current][3]=dy;
//			enemyBalls[current][4]=btID;
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally
//		{
//			pSem.release(); //Droit de modification cédé quoi qu'il arrive sous peine de blocage
//
//		}
//	}

	public void releasePB(int indice)
	{
		try {
			pSem.acquire(); // Droit de modification acquis
			int current = playerUsed[indice];
			playerUsed = remove(playerUsed,indice); // Une ligne devient utilisable, et n'est plus utilisée
			playerAvailable = add(playerAvailable,current,0);
			playerSemaphore.release(); // On rend un jeton sur la semaphore Player
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			pSem.release(); // Droit de modification rendu
		}
	}

//	public void releaseEB(int indice)
//	{
//		try {
//			eSem.acquire(); // Droit de modification acquis
//			int current = enemyUsed[indice];
//			enemyUsed = remove(enemyUsed,indice);
//			enemyAvailable = add(enemyAvailable,current,0);
//			enemySemaphore.release();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally
//		{
//			eSem.release();
//		}
//	}

	public int[] remove(int[] current, int k)
	{
		int[] temp = new int[current.length-1];
		for(int i=0;i<k;i++)
		{
			temp[i]=current[i];
		}
		for(int i=k+1;i<current.length;i++)
		{
			temp[i-1]=current[i];
		}
		return temp;
		
	}
	
	public void update(int dx, int dy, int indice)
	{
		playerBalls[indice][0]+=dx;
		playerBalls[indice][1]+=dy;
	}
	
	public int[] add(int[] current, int value, int i)
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
		//System.out.println(current.length-toReturn.length);
		return toReturn;
	}

	public int[][] getBalls()
	{
		int k=0;
		//System.out.println(playerUsed.length+enemyUsed.length);
		int[][] toReturn = new int[playerUsed.length
		                          // +enemyUsed.length
		                           ][5];
		for(int i=0;i<playerUsed.length;i++)
		{
			toReturn[k][0]=playerBalls[playerUsed[i]][0];
			toReturn[k][1]=playerBalls[playerUsed[i]][1];
			toReturn[k][2]=playerBalls[playerUsed[i]][2];
			toReturn[k][3]=playerBalls[playerUsed[i]][3];
			toReturn[k][4]=playerBalls[playerUsed[i]][4];
			k++;
		}
//		for(int i:enemyUsed)
//		{
//			toReturn[k][0]=enemyBalls[i][0];
//			toReturn[k][1]=enemyBalls[i][1];
//			toReturn[k][2]=enemyBalls[i][2];
//			toReturn[k][3]=enemyBalls[i][3];
//			toReturn[k][4]=enemyBalls[i][4];
//			k++;
//		}
		return toReturn;
	}

	public boolean isEmpty()
	{
		if(playerUsed.length
				//+enemyUsed.length
				==0)
		{
			return true;
		}
		else return false;
	}
}
