package game;

import graphics.Bullet;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class BallManagement {

	public static volatile ArrayList<Bullet> playerBalls;
	public Semaphore playerSemaphore = new Semaphore(200);
	
	public static volatile ArrayList<Bullet> enemyBalls;
	public Semaphore enemySemaphore = new Semaphore(200);
	
	public BallManagement()
	{
		playerBalls = new ArrayList<Bullet>(200);
		for(Bullet b : playerBalls)
		{
			b = new Bullet(0,0,0,0,0);
		}
		enemyBalls = new ArrayList<Bullet>(200);
		for(Bullet b : enemyBalls)
		{
			b = new Bullet(0,0,0,0,0);
		}
	}
	
	public void playerAcquire()
	{
		
	}
	
	public void playerRelease()
	{
		playerSemaphore.release();
	}
	
	public synchronized void enemyAcquire(int id,int x, int y, int dx, int dy)
	{

	}
	
	public void enemyRelease()
	{
		
		enemySemaphore.release();
	}
	
}
