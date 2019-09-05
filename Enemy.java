import java.awt.*;
import java.awt.Event.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Enemy extends Character
{
	String name;
	int MAX= 10;
	public Image image;
	public Image[][] images;
	public Projectile bullet;
	public String direction;
	public Player player;
	public int wait;
	public int wait2;
	public boolean hasAttacked;
	public int damage;
	private Toolkit toolkit;
	ArrayList<String> moves;
	public int curMove;
	public boolean alive;
	
	public long pastMilli;
	public long pastMilliRemainder;
	public long milliSinceMove;

	public Enemy(int health, String name, Player p, int x, int y, int damage)
	{
		super();
		moves= new ArrayList<String>();
		pastMilli=System.currentTimeMillis();
		pastMilliRemainder=(long)0;
		milliSinceMove= (long)0;
		alive= true;
		curMove=0;
		this.damage = damage;
		this.health = health;
		wait= RandomNumbers.randomInt(1200,2200);
		wait2= wait+30;
		player=p;
		hasAttacked= false;
		direction= "left";
		this.name= name;
		toolkit=Toolkit.getDefaultToolkit();
		positionX= x;
	  	positionY= y;
	  	image = toolkit.getImage(getClass().getResource("enemies/" + name + "/left0.png"));
	  	createSpriteArray();
	  	setBlocks();
	}

	private void createSpriteArray()
	{
		images = new Image[4][4];
		//direction down
		int i;
		try
		{
			for(i = 0; i < 3; i++)
			{
				images[0][i] = toolkit.getImage(getClass().getResource("enemies/" + name + "/up" + i + ".png"));
				images[1][i] = toolkit.getImage(getClass().getResource("enemies/" + name + "/right" + i + ".png"));
				images[2][i] = toolkit.getImage(getClass().getResource("enemies/" + name + "/left" + i + ".png"));
				images[3][i] = toolkit.getImage(getClass().getResource("enemies/" + name + "/down" + i + ".png"));
			}
		}
		catch(Exception e) {System.out.println(e);}
	}


	public void setBlocks()
	{
		positionXInBlocks = (positionX+15) / 16;
		positionYInBlocks = (positionY+42) / 16;
	}
	public void wound(int num)
	{
		health= health-num;
		if(health <= 0)
		{
			if(alive)
			{
				positionX= positionX+5;
				positionY= positionY+10;
			}
			alive= false;
			dead();
		}
	}
	public void dead()
	{
		image= toolkit.getImage(getClass().getResource("enemies/" + name + "/death.png"));
		for(int i = 0; i < 3; i++)
		{
			images[0][i] = toolkit.getImage(getClass().getResource("enemies/" + name + "/dead.png"));
			images[1][i] = toolkit.getImage(getClass().getResource("enemies/" + name + "/dead.png"));
			images[2][i] = toolkit.getImage(getClass().getResource("enemies/" + name + "/dead.png"));
			images[3][i] = toolkit.getImage(getClass().getResource("enemies/" + name + "/dead.png"));
		}
	}
	public boolean collide(Projectile temp2)
	{
		if(alive)
		{
		String bullet="("+temp2.XBlock+","+temp2.YBlock+")";
		ArrayList<String> blocks= new ArrayList<String>();
		for(int x=0; x <= sizeX; x++)
		{
			for(int y=0; y > sizeY; y--)
			{
				int xNum= positionXInBlocks+x;
				int yNum= positionYInBlocks+y;
				blocks.add("("+xNum+","+yNum+")");
			}
		}
		if(temp2.smashed == false)
		{
				//changed to +2 and +1 because player is now counted by bottom left block
				//it's pretty good now.  We may not have to go with 8x8 blocks
			for(int x=0; x < blocks.size(); x++)
			{
				if(bullet.equals(blocks.get(x)))
				{
					temp2.smashed= true;
					if(temp2.explosive == false)
						wound(temp2.damage);
					//System.out.println(temp2.damage);
					return temp2.smashed;
				}
			}
		}
		}
		return temp2.smashed;
	}
	public Projectile fire()
	{
//		System.out.println("ENEMY CLASS: "+damage);
		String pDirect= player.direction;
		int x= player.positionX;
		int y= player.positionY;
		int degreeX= Math.abs(x- positionX);
		int degreeY= Math.abs(y- positionY);
		//This puts player bottom left of the enemy
		if(x <= positionX && y >= positionY)
		{
			if(Math.abs(x-positionX) <= 35)
				direction= "down";
			else if(Math.abs(y-positionY) <=35)
				direction= "left";
			else
			{
				if(Math.abs(degreeX-degreeY) < 35)
					direction= "sw";
				else if(degreeX < degreeY)
				{
					if( pDirect.equals("up") || pDirect.equals("left") || pDirect.equals("nw") || pDirect.equals("sw"))
					{
						direction= "sw";
					}
					else
						direction= "down";
				}
				else
				{
					if(pDirect.equals("up") || pDirect.equals("left") || pDirect.equals("ne") || pDirect.equals("nw"))
					{
						direction= "left";
					}
					else
						direction= "sw";
				}
				
			}
		}
		//top left
		else if(x < positionX && y < positionY)
		{
			if(Math.abs(x-positionX) <= 35)
				direction= "up";
			else if(Math.abs(y-positionY) <=35)
				direction= "left";
			else
			{
				if(Math.abs(degreeX-degreeY) < 35)
					direction= "nw";
				else if(degreeX < degreeY)
				{
					if( pDirect.equals("up") || pDirect.equals("right") || pDirect.equals("ne") || pDirect.equals("se"))
					{
						direction= "up";
					}
					else
						direction= "nw";
				}
				else
				{
					if(pDirect.equals("up") || pDirect.equals("right") || pDirect.equals("ne") || pDirect.equals("nw"))
					{
						direction= "nw";
					}
					else
						direction= "left";
				}
				
			}
		}
		//bottom right
		else if(x > positionX && y > positionY)
		{
			if(Math.abs(x-positionX) <= 35)
				direction= "down";
			else if(Math.abs(y-positionY) <=35)
				direction= "right";
			else
			{
				if(Math.abs(degreeX-degreeY) < 35)
					direction= "se";
				else if(degreeX < degreeY)
				{
					if( pDirect.equals("up") || pDirect.equals("right") || pDirect.equals("ne") || pDirect.equals("se"))
					{
						direction= "se";
					}
					else
						direction= "down";
				}
				else
				{
					if(pDirect.equals("up") || pDirect.equals("right") || pDirect.equals("ne") || pDirect.equals("nw"))
					{
						direction= "right";
					}
					else
						direction= "se";
				}
				
			}
		}
		//top right
		else
		{
			if(Math.abs(x-positionX) <= 35)
				direction= "up";
			else if(Math.abs(y-positionY) <=35)
				direction= "right";
			else
			{
				if(Math.abs(degreeX-degreeY) < 35)
					direction= "ne";
				else if(degreeX < degreeY)
				{
					if( pDirect.equals("up") || pDirect.equals("left") || pDirect.equals("nw") || pDirect.equals("sw"))
					{
						direction= "up";
					}
					else
						direction= "ne";
				}
				else
				{
					if(pDirect.equals("up") || pDirect.equals("left") || pDirect.equals("nw") || pDirect.equals("ne"))
					{
						direction= "ne";
					}
					else
						direction= "right";
				}
				
			}
		}
// 		System.out.println("dir is " + direction);
// 		move(direction);
		if(direction.equals("up") || direction.equals("ne") || direction.equals("nw"))
		{
			image = images[0][0];
		}
		else if(direction.equals("right"))
		{
			image = images[1][0];
		}
		else if(direction.equals("left"))
		{
			image = images[2][0];
		}
		else
			image = images[3][0];
			
		bullet= new Projectile(direction, 1, positionX, positionY+20, "Pistol", damage);
		return bullet;
	}
	
	public void move()
	{
		if(alive)
		{
		long milli=System.currentTimeMillis()-pastMilli;
		pastMilli=System.currentTimeMillis();
		
		milli+=pastMilliRemainder;
		pastMilliRemainder=milli%(long)10;
		
		milliSinceMove+=milli;
		
		if(milliSinceMove > 400)
		{
			milliSinceMove =0;
		if(curMove+1 > moves.size())
			curMove= 0;
		String dir= moves.get(curMove);
		curMove++;
		if(dir.equals("right"))
			positionX+=4;
		else if(dir.equals("left"))
			positionX-=4;
		else if (dir.equals("up"))
			positionY-=4;
		else if (dir.equals("down"))
			positionY+=4;
		else if (dir.equals("ne"))
		{
			positionX+=4;
			positionY+=4;
		}
		else if (dir.equals("nw"))
		{
			positionX-=4;
			positionY+=4;
		}
		else if (dir.equals("se"))
		{
			positionX+=4;
			positionY-=4;
		}
		else if (dir.equals("sw"))
		{
			positionX-=4;
			positionY-=4;
		}
		positionXInBlocks = (positionX+15) / 16;
		positionYInBlocks = (positionY+42) / 16;
		}
		}
	}
}
