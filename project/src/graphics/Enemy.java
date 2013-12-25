package graphics;

import game.BackgroundDisplay;
import game.BallManagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Enemy implements ActionListener{
	public BulletType bt;
	public Ship ship;
	public int x,y;
	public int life;
	Timer timer;
	int left=20,right=0;
	
	public Enemy(int x,int y,BulletType bt,Ship ship)
	{
		this.x=x;
		this.y=y;
		this.bt=bt;
		this.ship=ship;
		timer = new Timer(BackgroundDisplay.DEFAULT_FPS,this);
		life=ship.getLife();
		timer.start();
	}
	
	public void move(int dx,int dy)
	{
		x+=dx;
		y+=dy;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public Ship getShip()
	{
		return ship;
	}
	
	public BulletType getBulletType()
	{
		return bt;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
			if(left>0)
			{
				this.move(-2,0);
				left--;
				if(left==0){right=20;}
			}
			
			if(right>0)
			{
				this.move(2,0);
				right--;
				if(right==0){left=20;}
			}
		
	}
	
	public static ArrayList<Enemy> spawn(int[] input)
	{
		ArrayList<Enemy> array = new ArrayList<Enemy>(0);
		if(input[0]>0)
		{
			for(int i=1;i<input[0];i++)
			{
				array.add(new Enemy(0,-100,BulletType.BASIC_ENNEMY1,Ship.BASIC_CRUISER));
				//création des ennemis en dehors de la fenêtre de jeu, de sorte à ce que le vaisseau entre en jeu ensuite
			}
		}
		if(input[1]>0)
		{
			for(int i=1;i<input[1];i++)
			{
				array.add(new Enemy(0,-100,BulletType.BASIC_ENNEMY2,Ship.BASIC_CRUISER));
			}
		}
		if(input[2]>0)
		{
			for(int i=1;i<input[2];i++)
			{
				array.add(new Enemy(0,-100,BulletType.BASIC_MEDIUM,Ship.ENNEMY_MEDIUM0));
			}
		}
		
		return array;
	}
	
	public void shoot(BulletType bt,BallManagement pool)
	{
		
	}
	
	public void behaviour(BulletType bt, BallManagement pool)
	{
		
	}

}
