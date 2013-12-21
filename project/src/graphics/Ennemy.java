package graphics;

import game.BackgroundDisplay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Ennemy implements ActionListener{
	public BulletType bt;
	public Ship ship;
	public int x,y;
	public int life;
	Timer timer;
	int left=20,right=0;
	
	public Ennemy(int x,int y,BulletType bt,Ship ship)
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
	
	

}
