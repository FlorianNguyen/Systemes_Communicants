package game;

import java.util.ArrayList;

import graphics.Bullet;
import graphics.FictiveEnemy;

public class ProcessingThread extends Thread {

	private ArrayList<Bullet> balls;
	private ArrayList<FictiveEnemy> fEnemies;
	
	public ProcessingThread()
	{
		balls = new ArrayList();
		fEnemies = new ArrayList<FictiveEnemy>();
	}
	
	public void process(int a, int b, double dx, double dy, int id)
	{
		balls.add(new Bullet(id,a,b,dx,dy));
	}
	
	public void process(int a, int b, int id)
	{
		fEnemies.add(new FictiveEnemy(a,b,id));
	}
	
	public ArrayList<Bullet> getResult()
	{
		return new ArrayList(balls);
	}
	
	public void reset()
	{
		balls = new ArrayList();
	}
}
