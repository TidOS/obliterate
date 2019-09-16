import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Game extends JPanel implements Menu
{
	public Player player;
	public Level currentLevel;
	public Enemy enemy;
	public Enemy enemy2;

	public Toolkit toolkit;
	Image gameOver;
	private Image weaponIcon;
	public Image dbImage;
	public Graphics dbg;
	public Screen s;
	public ArrayList<Projectile> bullets;
	public ArrayList<Projectile> Ebullets;
	public Projectile temp;
	public Projectile temp2;
	public int count;
	public int rN;
	
	public long pastMilli;
	public long pastMilliRemainder;
	public long milliPerPixel;
	public long milliSinceFire;
	public int milliSinceFire2;
	public boolean rendering=false;
	
	public Game(Screen sIn)
	{
		s = sIn;
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		player = new Player("Charlie",s);
		bullets= new ArrayList<Projectile>();
		Ebullets= new ArrayList<Projectile>();
		count=0;
 		gameOver = toolkit.getImage(getClass().getResource("backgrounds/gameover.png"));
		
		rN= RandomNumbers.randomInt(1400,1800);
		
		//Initializes millisecond parameters
		pastMilli=System.currentTimeMillis();
		pastMilliRemainder=(long)0;
		milliPerPixel=(long)10;
		milliSinceFire=(long)0;
		milliSinceFire2=0;
		player.updateSprite();
		weaponIcon = toolkit.getImage(getClass().getResource("weapons/" + player.weapons[player.currentWeapon].name.toLowerCase() + "/inventorysmall.png"));
		System.out.println("weapons/" + player.weapons[player.currentWeapon].name + "/inventory.png");
	}
	
	public void updateWeaponIcon()
	{
		try{
			Toolkit toolkit= Toolkit.getDefaultToolkit();
		System.out.println("weapons/" + player.weapons[player.currentWeapon].name + "/inventory.png");
		weaponIcon = toolkit.getImage(getClass().getResource("weapons/" + player.weapons[player.currentWeapon].name + "/inventorysmall.png"));
		} catch (Exception e) {}
	}
	
	public void newGame(int num)
	{
		currentLevel = new Level(1,s,num);
		currentLevel.switchSection(0,0);
	}
	
	public void restartLevel()
	{
		currentLevel = new Level(currentLevel.num,s,currentLevel.size);
		if(currentLevel.num==2)
			currentLevel.switchSection(0,2);
		else
			currentLevel.switchSection(0,0);
		player.positionX = 40;
		player.positionY = 40;
		player.positionXInBlocks = 40 / 16;
		player.positionYInBlocks = 40 / 16;
		player.direction = "down";
		player.updateSprite();
		player.updateGun();
		if(dbg!= null)
			dbg.drawImage(player.weapon, player.gunX, player.gunY, s);
	}
	
	public void resetGame()
	{
		count = 0;
		pastMilli=System.currentTimeMillis();
		pastMilliRemainder=(long)0;
		milliPerPixel=(long)10;
		milliSinceFire=(long)0;
		milliSinceFire2=0;
		player.updateSprite();
		player.reset();
		currentLevel.num=1;
		currentLevel.size=4;
		restartLevel();
	}
	
	public void render(Graphics g,int x,int y,int key,Screen s)
	{
		//If already rendering, then don't try to render.
		if(rendering)
			return;
			
		rendering=true;
		if(player.lives < 0)
		{
			dbg.drawImage(gameOver,20,20,s);
			g.drawImage(dbImage,0,25,s);
			resetGame();
			s.changeCurrentMenu(6);
			//rendering = false;
			//return;
		}		
		//Determine number of milliseconds since last render()
		long milli=System.currentTimeMillis()-pastMilli;
		pastMilli=System.currentTimeMillis();
		
		milli+=pastMilliRemainder;
		long currentMilli=milli/milliPerPixel;
		pastMilliRemainder=milli%milliPerPixel;
		
		milliSinceFire+=milli;
		milliSinceFire2+=(int)milli;
		if (dbImage == null)
		{
			dbImage = createImage(x,y);
			dbg = dbImage.getGraphics();
			dbg.setFont(dbg.getFont().deriveFont((float)24));
			dbg.setColor(Color.white);
			temp2= new Projectile("left", 2, 1, 1, "Rocket_Launcher", 20);
			temp2.explode(currentLevel.currentSection.enemies);
			for(int i = 0; i < 4; i++)
			{
				dbg.drawImage(player.sprite[0][i],0,0,s);
				dbg.drawImage(player.sprite[1][i],0,0,s);
				dbg.drawImage(player.sprite[2][i],0,0,s);
				dbg.drawImage(player.sprite[3][i],0,0,s);
				dbg.drawImage(player.weapon, player.gunX, player.gunY, s);
				dbg.drawImage(temp2.background, 0, 0, s);
			}
			for(int i = 0; i < currentLevel.size; i++)
			{
				for(int j = 0; j < currentLevel.size; j++)
				{
					try
					{
						if(currentLevel.sections[i][j].background != null)
						dbg.drawImage(currentLevel.sections[i][j].background,0,0,s);
					}
					catch (NullPointerException e)
					{}
				}
			}
		}
		player.updateGun();
// 		System.out.println("PLAYER IN BLOCK " + player.positionXInBlocks + " " + player.positionYInBlocks);
// 		System.out.println("PLAYER PIXELS " + player.positionX + " " + player.positionY);
/*************************************************************
 *             MOVEMENT SECTION
 ************************************************************/
		if(s.keysHeld[0] == true && s.keysHeld[2] == true)
		{
			boolean collide= false;
			for(int p= 0; p < currentLevel.currentSection.enemies.size(); p++)
			{
				enemy= currentLevel.currentSection.enemies.get(p);
				if((player.positionYInBlocks == enemy.positionYInBlocks+1) && (player.positionXInBlocks == enemy.positionXInBlocks))
				{
					if(enemy.alive == true)
						collide= true;
				}
			}
			if(collide == false)
			{
				player.positionY = player.positionY - (int)(currentMilli);
				player.positionX = player.positionX + (int)(currentMilli);
				player.direction = "ne";
				player.setBlocks();
			}
			

			if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type == 1)
			{
				player.positionY += (int)(currentMilli) ;
				player.positionX = player.positionX - (int)(currentMilli);
				player.setBlocks();
			}
			else if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type != -1)
			{
				currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].trap.action(s);
				player.updateSprite();
			}
			else		
				player.updateSprite();
		}
		//up and left	
		else if(s.keysHeld[0] == true && s.keysHeld[1] == true)
		{
			boolean collide= false;
			for(int p= 0; p < currentLevel.currentSection.enemies.size(); p++)
			{
				enemy= currentLevel.currentSection.enemies.get(p);
				if((player.positionYInBlocks == enemy.positionYInBlocks+1) && (player.positionXInBlocks == enemy.positionXInBlocks))
				{
					if(enemy.alive == true)
						collide= true;
				}
			}
			if(collide == false)
			{
				player.positionY = player.positionY - (int)(currentMilli);
				player.positionX = player.positionX - (int)(currentMilli);
				player.direction = "nw";
				player.setBlocks();
			}
			

			if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type == 1)
			{
				player.positionY += (int)(currentMilli) ;
				player.positionX = player.positionX + (int)(currentMilli);
				player.setBlocks();
			}
			else if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type != -1)
			{
				currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].trap.action(s);
			}
			else		
				player.updateSprite();
		}
		//down and left
		else if(s.keysHeld[3] == true && s.keysHeld[1] == true)
		{
			boolean collide= false;
			for(int p= 0; p < currentLevel.currentSection.enemies.size(); p++)
			{
				enemy= currentLevel.currentSection.enemies.get(p);
				if((player.positionYInBlocks == enemy.positionYInBlocks) && (player.positionXInBlocks == enemy.positionXInBlocks+1))
				{
					if(enemy.alive == true)
						collide= true;
				}
			}
			if(collide == false)
			{
				player.positionX = player.positionX - (int)(currentMilli);
				player.positionY = player.positionY + (int)(currentMilli);
				player.direction = "sw";
				player.setBlocks();
			}
			if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type == 1)
			{
				player.positionX += (int)(currentMilli) ;
				player.positionY = player.positionY - (int)(currentMilli);	
				player.setBlocks();
			}
			else if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type != -1)
			{
				currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].trap.action(s);
			}
			else
			player.updateSprite();
		}
		else if(s.keysHeld[3] == true && s.keysHeld[2] == true)
		{
			boolean collide= false;
			for(int p= 0; p < currentLevel.currentSection.enemies.size(); p++)
			{
				enemy= currentLevel.currentSection.enemies.get(p);
				if((player.positionYInBlocks == enemy.positionYInBlocks) && (player.positionXInBlocks == enemy.positionXInBlocks+1))
				{
					if(enemy.alive == true)
						collide= true;
				}
			}
			if(collide == false)
			{
				player.positionX = player.positionX + (int)(currentMilli);
				player.positionY = player.positionY + (int)(currentMilli);
				player.direction = "se";
				player.setBlocks();
			}
			if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type == 1)
			{
				player.positionX -= (int)(currentMilli) ;
				player.positionY = player.positionY - (int)(currentMilli);	
				player.setBlocks();
			}
			else if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type != -1)
			{
				currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].trap.action(s);
			}
			else
			player.updateSprite();
		}
		else if(s.keysHeld[0] == true)	//up key
		{
			boolean collide= false;
			for(int p= 0; p < currentLevel.currentSection.enemies.size(); p++)
			{
				enemy= currentLevel.currentSection.enemies.get(p);
				if((player.positionYInBlocks == enemy.positionYInBlocks+1) && (player.positionXInBlocks == enemy.positionXInBlocks))
				{
					if(enemy.alive == true)
						collide= true;
				}
			}
			if(collide == false)
			{
				player.positionY = player.positionY - (int)(currentMilli);
				player.direction = "up";
				player.setBlocks();
			}
			

			if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type == 1)
			{
				player.positionY += (int)(currentMilli) ;
				player.setBlocks();
			}
			else if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type != -1)
			{
				currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].trap.action(s);
			}
			else		
				player.updateSprite();
		}
		else if(s.keysHeld[2] == true) //right key
		{
			boolean collide= false;
			for(int p= 0; p < currentLevel.currentSection.enemies.size(); p++)
			{
				enemy= currentLevel.currentSection.enemies.get(p);
				if((player.positionYInBlocks == enemy.positionYInBlocks) && (player.positionXInBlocks == enemy.positionXInBlocks-1))
				{
					if(enemy.alive == true)
						collide= true;
				}
			}
			if(collide == false)
			{
				player.positionX = player.positionX + (int)(currentMilli);
				player.direction = "right";
				player.setBlocks();
			}
			if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type == 1)
			{
				player.positionX -= (int)(currentMilli) ;
				player.setBlocks();
			}
			else if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type != -1)
			{
				currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].trap.action(s);
			}
			else
			player.updateSprite();
		}
		else if(s.keysHeld[1] == true)	//left key
		{
			boolean collide= false;
			for(int p= 0; p < currentLevel.currentSection.enemies.size(); p++)
			{
				enemy= currentLevel.currentSection.enemies.get(p);
				if((player.positionYInBlocks == enemy.positionYInBlocks) && (player.positionXInBlocks == enemy.positionXInBlocks+1))
				{
					if(enemy.alive == true)
						collide= true;
				}
			}
			if(collide == false)
			{
				player.positionX = player.positionX - (int)(currentMilli);
				player.direction = "left";
				player.setBlocks();
			}
			if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type == 1)
			{
				player.positionX += (int)(currentMilli) ;	
				player.setBlocks();
			}
			else if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type != -1)
			{
				currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].trap.action(s);
			}
			else
			player.updateSprite();
		}
		else if(s.keysHeld[3] == true) //down key
		{
			boolean collide= false;
			for(int p= 0; p < currentLevel.currentSection.enemies.size(); p++)
			{
				enemy= currentLevel.currentSection.enemies.get(p);
				if((player.positionYInBlocks == enemy.positionYInBlocks-1) && (player.positionXInBlocks == enemy.positionXInBlocks))
				{
					if(enemy.alive == true)
						collide= true;
				}
			}
			if(collide == false)
			{
				player.positionY = player.positionY + (int)(currentMilli);
				player.direction = "down";
				player.setBlocks();
			}
			if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type == 1)
			{	
				player.positionY -= (int)(currentMilli) ;
				player.setBlocks();
			}
			else if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type != -1)
			{
				currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].trap.action(s);
			}
			else
			player.updateSprite();
		}
/*********************************************************
 *END OF MOVEMENT
 ********************************************************/
		if(key == s.keys[5])
		{
			s.changeCurrentMenu(4);
		}
		
		//next level
		//it works, but level 2 is a little screwy right now.
		if(currentLevel.currentSection.blocks[player.positionXInBlocks][player.positionYInBlocks].type == 0)
		{
			if(currentLevel.num==2)
			{
				currentLevel.num = 1;
				currentLevel.size = 4;
				s.changeCurrentMenu(7);
				currentLevel = new Level(currentLevel.num,s,currentLevel.size);
				return;
			}
			else
			{
				s.changeCurrentMenu(3);
				currentLevel.num++;
				restartLevel();
			}
			
		}
		if(s.keysHeld[4] == true) //fire key
		{
			if(player.getAmmo() > 0 && milliSinceFire>(long)player.weapons[player.currentWeapon].timer)
			{
				milliSinceFire=(long)0;
				player.fire(bullets,currentLevel);
			}
		}
		if(milliSinceFire2 > 1000)
		{
			for(int p= 0; p < currentLevel.currentSection.enemies.size(); p++)
			{
				if((milliSinceFire2%((currentLevel.currentSection.enemies.get(p)).wait) < 35) && ((currentLevel.currentSection.enemies.get(p)).hasAttacked == false))
				{
					if(currentLevel.currentSection.enemies.get(p).alive == true)
					{
					temp2= (currentLevel.currentSection.enemies.get(p)).fire();
					temp2.level = currentLevel;
					Ebullets.add(temp2);
					(currentLevel.currentSection.enemies.get(p)).hasAttacked= true;
					}
				}
			}
			if(milliSinceFire2 > 2300)
			{
				for(int p= 0; p < currentLevel.currentSection.enemies.size(); p++)
				{
					(currentLevel.currentSection.enemies.get(p)).hasAttacked= false;
				}
				milliSinceFire2=0;
			}
		}
		//System.out.println("Player is in block " + player.positionXInBlocks + "," + player.positionYInBlocks);
		dbg.drawImage(currentLevel.sections[currentLevel.currentSectionX][currentLevel.currentSectionY].background,0 ,0, s);
		dbg.drawString("" + player.health,73,27);
		dbg.drawString("" + player.getAmmo(),560,27);
		if(player.lives <0)
			dbg.drawString("" + 0,188,27);
		else
			dbg.drawString("" + player.lives,188,27);
			
		for(int i= 0; i <currentLevel.currentSection.traps.size(); i++)
		{
			dbg.drawImage(currentLevel.currentSection.traps.get(i).image, currentLevel.currentSection.traps.get(i).x*16,currentLevel.currentSection.traps.get(i).y*16,s);
		}
		dbg.drawImage(player.weapon, player.gunX, player.gunY, s);
		for(int p=0; p < currentLevel.currentSection.enemies.size(); p++)
		{
			enemy= currentLevel.currentSection.enemies.get(p);
			//dbg.drawImage(enemy.image,enemy.positionX-4, enemy.positionY+4, s);
		}

		for(int q=0; q < bullets.size(); q++)
		{	
			temp= bullets.get(q);
			if(temp.smashed == false && temp.exploded == false)
			{
				for(int p= 0; p < currentLevel.currentSection.enemies.size(); p++)
				{
					enemy= currentLevel.currentSection.enemies.get(p);
					if(enemy.collide(temp))
					{	
						if(temp.explosive == false)
							bullets.remove(q);	
						p= currentLevel.currentSection.enemies.size();
					}
				}
			}
			if(temp.explosive == true && temp.smashed == true)
			{
				temp.smashed= false;
				if(temp.exploded == true)
				{
					bullets.remove(q);
					temp.smashed =true;
				}
				else
				{
					temp.explode(currentLevel.currentSection.enemies);
					dbg.drawImage(temp.background,temp.Xpos-96 ,temp.Ypos-84, s);
				}
			}
			if(temp.smashed == false && temp.exploded== false)
			{
				dbg.drawImage(temp.background,temp.Xpos ,temp.Ypos, s);
				temp.update();	
			}
		}
		for(int q=0; q < Ebullets.size(); q++)
		{
			temp2= Ebullets.get(q);
			if(player.collide(temp2))
				Ebullets.remove(q);
			else
			{
				dbg.drawImage(temp2.background,temp2.Xpos ,temp2.Ypos, s);
				temp2.update();
			}	
		}
		//drawing the gun
	//	dbg.drawImage(player.weapon, player.gunX, player.gunY, s);
		for(int i=0; i<28; i++)
		{
			for(int p=0; p<currentLevel.currentSection.enemies.size(); p++)
			{
				enemy=currentLevel.currentSection.enemies.get(p);
				if(enemy.positionYInBlocks==i)
				{
					dbg.drawImage(enemy.image, enemy.positionX-4, enemy.positionY+4, s);
					enemy.move();
				}
			}
			if(player.positionYInBlocks==i)
			{
				dbg.drawImage(player.currentSprite,player.positionX - 4,player.positionY + 4,s);
			}
		}

		if(player.health <= 0)
		{
			restartLevel();
			player.lives--;
			player.health  = 100;
			player.updateGun();
		}
				
        dbg.drawImage(weaponIcon,370,0,s);
		g.drawImage (dbImage,0,25,s);
		
		rendering=false;
	}
	public void removeTrap()
	{
		for(int i=0; i <currentLevel.currentSection.traps.size(); i++)
		{
			if(player.positionXInBlocks == currentLevel.currentSection.traps.get(i).x)
			{
				currentLevel.currentSection.traps.remove(i);
				player.updateSprite();
				return;
			}
		}
	}
}
