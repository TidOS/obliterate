import java.awt.*;
import java.awt.Event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class Player extends Character
{
	String name;
	int maxHealth;
	Weapon[] weapons= new Weapon[10];
	int currentWeapon;
	public int lives;
	public Image image;
	public Image currentSprite;
	public Image[][] sprite;
	public Image weapon;
	private int spriteFrameCount;
	private int switcher;
	private Toolkit toolkit;
	private MediaTracker m;
	private Screen scr;
	public int gunX;
	public int gunY;
	//we also have positionX,Y from Character class
	
	public long pastMilliChange;
	public long milliPerChange;

	public Player(String name, Screen s)
	{
		super();
		this.name= name;
		maxHealth= 100;
		health = 100;
		lives = 5;
		scr = s;
		currentWeapon= 0;
		toolkit=Toolkit.getDefaultToolkit();
		spriteFrameCount = 0;
		switcher = 0;
		m = new MediaTracker(s);
		createSpriteArray();
		weapons[0] = new Weapon("Pistol", 2, 5, 400);
		weapons[1] = new Weapon("MachineGun", 3, 5, 200);
		weapons[2] = new Weapon("Shotgun", 2, 10, 1000);
		weapons[3] = new Weapon("Rocket_Launcher", 1, 20, 1300);
		weapons[4] = new Weapon("Fire_Gun", 2, 30, 400);
		weapons[5] = new Weapon("KnifeGun", 2, 500, 200);
		weapons[6] = new Weapon("none", 2, 5, 400);
		weapons[7] = new Weapon("none", 2, 5, 400);
		weapons[8] = new Weapon("none", 2, 5, 400);
		pastMilliChange=System.currentTimeMillis();
		milliPerChange=(long)50;
		gunX= positionX+8;
		gunY= positionY+12;
	}
	public void reset()
	{
		health = 100;
		lives = 5;
		currentWeapon= 0;
		weapons[0] = new Weapon("Pistol", 2, 5, 400);
		weapons[1] = new Weapon("MachineGun", 3, 5, 200);
		weapons[2] = new Weapon("Shotgun", 2, 10, 1000);
		weapons[3] = new Weapon("Rocket_Launcher", 1, 20, 1300);
		weapons[4] = new Weapon("Fire_Gun", 2, 30, 400);
		weapons[5] = new Weapon("KnifeGun", 2, 500, 200);
		weapons[6] = new Weapon("None", 2, 5, 400);
		weapons[7] = new Weapon("None", 2, 5, 400);
		weapons[8] = new Weapon("None", 2, 5, 400);
		gunX= positionX+8;
		gunY= positionY+12;
	}
	public boolean collide(Projectile temp2)
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
					wound(temp2.damage);
					//System.out.println(temp2.damage);
				}
			}
		}
		return temp2.smashed;
	}
	public void addAmmo(int x, int amount)
	{
		weapons[x].addAmmo(amount);
	}
	public int getAmmo()
	{
		return weapons[currentWeapon].ammo;
	}
	public String toString()
	{
		return name;
	}
	public void wound(int num) //throws PlayerDeath
	{
		health= health- num;
	}
	public void updatePos(int x, int y)
	{
		positionX=x;
		positionY=y;
		gunX= positionX+8;
		gunY= positionY+12;
	}
	
	private void createSpriteArray()
	{
		
		sprite = new Image[4][4];
		//direction down
		int i;
		try
		{
			for(i = 0; i < 4; i++)
			{
				sprite[0][i] = toolkit.getImage(getClass().getResource("sprites/up" + i + ".png"));
				sprite[1][i] = toolkit.getImage(getClass().getResource("sprites/left" + i + ".png"));
				sprite[2][i] = toolkit.getImage(getClass().getResource("sprites/right" + i + ".png"));
				sprite[3][i] = toolkit.getImage(getClass().getResource("sprites/down" + i + ".png"));
			}
		}
		catch(Exception e) {}
	}
	
	public void move(int lon, int lat)
	{
		positionX= positionX+lon;
		positionY= positionY+lat;
		if(positionX < 0)
			positionX= 0;
		else if(positionX > 39)
			positionX= 39;
		if(positionY < 0)
			positionY= 0;
		else if(positionY > 29)
			positionY = 29;
	}
	
	 public void fire(ArrayList<Projectile> bullets, Level lvl)
	 {
	 	if(direction.equals("up"))
		{
			currentSprite = sprite[0][spriteFrameCount];
			weapon= toolkit.getImage(getClass().getResource("weapons/"+weapons[currentWeapon].name+"/fireUp.png"));
		}
	 	if(direction.equals("left") || direction.equals("nw") || direction.equals("sw"))
		{
			weapon= toolkit.getImage(getClass().getResource("weapons/"+weapons[currentWeapon].name+"/fireLeft.png"));
		}			
		else if(direction.equals("right") || direction.equals("ne") || direction.equals("se"))
		{
			weapon= toolkit.getImage(getClass().getResource("weapons/"+weapons[currentWeapon].name+"/fireRight.png"));
		}
		else if(direction.equals("down"))
		{
			weapon= toolkit.getImage(getClass().getResource("weapons/"+weapons[currentWeapon].name+"/fireDown.png"));
			currentSprite = sprite[3][spriteFrameCount];
		}
	 	weapons[currentWeapon].fire(direction,gunX,gunY,bullets,lvl);
	 }
	public void updateSprite()
	{
		gunX= positionX+(8+spriteFrameCount);
		gunY= positionY+20+(spriteFrameCount/2);
		if(currentWeapon == 3)
			gunY -= 10;
		long milli=System.currentTimeMillis()-pastMilliChange;
		
		if(milli>=milliPerChange)
		{
			spriteFrameCount++;
			pastMilliChange=System.currentTimeMillis();
		}
		
		//spriteFrameCount++;
		if(spriteFrameCount >= 4)
			spriteFrameCount = 0;

		if(direction.equals("up"))
		{
			//if(currentWeapon == 0)
			//{
				gunX= positionX+17;
				gunY= positionY+9+spriteFrameCount;
			//}
			if(currentWeapon == 1)
			{
				gunX -= 1;
				gunY -= 10;
			}
			else if(currentWeapon == 2)
			{
				gunY -=10;
				gunX -=1;
			}
			else if(currentWeapon == 3)
			{
				gunY -= 16;
			}
			else if(currentWeapon ==4)
			{
				gunY -= 16;
			}
			else if(currentWeapon == 5)
			{
				gunY -= 5;
			}
			currentSprite = sprite[0][spriteFrameCount];
			weapon= toolkit.getImage(getClass().getResource("weapons/"+weapons[currentWeapon].name+"/up.png"));
		}
		else if(direction.equals("left") || direction.equals("nw") || direction.equals("sw"))
		{
			if(currentWeapon == 0)
			{	
				gunX-=24;
			}
			else if(currentWeapon == 1)
			{
				gunX -= 40;
			}
			else if(currentWeapon == 2)
			{
				gunX -= 35;
			}
			else if(currentWeapon == 3)
			{
				gunX -= 35;
			}
			else if(currentWeapon == 4)
			{
				gunX -= 40;
			}
			else if(currentWeapon == 5)
			{
				gunX -= 55;
				gunY -= 5;
			}
			weapon= toolkit.getImage(getClass().getResource("weapons/"+weapons[currentWeapon].name+"/left.png"));
			currentSprite = sprite[1][spriteFrameCount];
		}			
		else if(direction.equals("right") || direction.equals("ne") || direction.equals("se"))
		{
			weapon= toolkit.getImage(getClass().getResource("weapons/"+weapons[currentWeapon].name+"/right.png"));
			currentSprite = sprite[2][spriteFrameCount];
			if(currentWeapon == 2)
				gunX-=3;
		}
		else if(direction.equals("down"))
		{
			gunX= positionX;
			gunY= positionY+29+(spriteFrameCount);
			if(currentWeapon == 4)
			{
				gunY -= 16;
			}
			if(currentWeapon >= 1)
			{
				gunX-=5;
			}
			if(currentWeapon >= 2)
				gunX -= 3;
			
			

			weapon= toolkit.getImage(getClass().getResource("weapons/"+weapons[currentWeapon].name+"/down.png"));
			currentSprite = sprite[3][spriteFrameCount];
		}
					
		if(positionX < 0)
		{
			scr.game.currentLevel.switchSection(--scr.game.currentLevel.currentSectionX,scr.game.currentLevel.currentSectionY);
			positionX = 600;
			setBlocks();
		}
		else if(positionX > 615)
		{
			scr.game.currentLevel.switchSection(++scr.game.currentLevel.currentSectionX,scr.game.currentLevel.currentSectionY);
			//need to load next section
			positionX = 30;
			setBlocks();
		}
		if(positionY < 0)
		{
			scr.game.currentLevel.switchSection(scr.game.currentLevel.currentSectionX,++scr.game.currentLevel.currentSectionY);
			System.out.println(scr.game.currentLevel.currentSectionY);
			positionY = 380;
			setBlocks();
		}
		else if (positionY > 400)
		{
			//need to load next section
			scr.game.currentLevel.switchSection(scr.game.currentLevel.currentSectionX,--scr.game.currentLevel.currentSectionY);
			positionY = 20;
			setBlocks();
		}
// 		System.out.println("now in block [" + positionXInBlocks + "][" + positionYInBlocks + "] and x,y is (" + positionX + ","+positionY +")" );
	}
	public void	updateGun()
	{
		if(direction.equals("up"))
		{
			weapon= toolkit.getImage(getClass().getResource("weapons/"+weapons[currentWeapon].name+"/up.png"));
		}
		else if(direction.equals("left") || direction.equals("nw") || direction.equals("sw"))
		{
			weapon= toolkit.getImage(getClass().getResource("weapons/"+weapons[currentWeapon].name+"/left.png"));
		}			
		else if(direction.equals("right") || direction.equals("ne") || direction.equals("se"))
		{
			weapon= toolkit.getImage(getClass().getResource("weapons/"+weapons[currentWeapon].name+"/right.png"));
		}
		else if(direction.equals("down"))
		{
			weapon= toolkit.getImage(getClass().getResource("weapons/"+weapons[currentWeapon].name+"/down.png"));
		}
	}
	public void setBlocks()
	{
		positionXInBlocks = (positionX+15) / 16;
		positionYInBlocks = (positionY+42) / 16;
	}

}
	

