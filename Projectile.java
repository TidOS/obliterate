import java.awt.*;
import java.awt.Event.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Projectile
{
	public String dir;
	public int speed;
	public int speedY;
	public int Xpos;
	public int Ypos;
	public int extra;
	public boolean smashed;
	public Image background;
	public String weapon;
	public boolean	explosive;
	public long pastMilli;
	public long pastMilliRemainder;
	public long milliPerPixel;
	public boolean rendering=false;
	public Level level;
	public int XBlock;
	public int YBlock;
	public int damage;
	public boolean exploded;

	public Projectile(String dir, int speed, int Xp, int Yp, String weapon, int damage)
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		if(weapon.equals("Fire_Gun"))
		{
			if(dir.equals("up"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/Fire_GunUp.png"));
			else if(dir.equals("down"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/Fire_GunDown.png"));
			else if(dir.equals("right"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/Fire_GunRight.png"));
			else if(dir.equals("left"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/Fire_GunLeft.png"));
			else if(dir.equals("ne") || dir.equals("se"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/Fire_GunRight.png"));
			else if(dir.equals("nw") || dir.equals("sw"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/Fire_GunLeft.png"));	
			
		}
		else if(weapon.equals("Rocket_Launcher"))
		{
			if(dir.equals("up"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/Rocket_LauncherUp.png"));
			else if(dir.equals("down"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/Rocket_LauncherDown.png"));
			else if(dir.equals("right"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/Rocket_LauncherRight.png"));
			else if(dir.equals("left"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/Rocket_LauncherLeft.png"));
			else if(dir.equals("ne") || dir.equals("se"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/Rocket_LauncherRight.png"));
			else if(dir.equals("nw") || dir.equals("sw"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/Rocket_LauncherLeft.png"));				
		}
		else if(weapon.equals("KnifeGun"))
		{
			if(dir.equals("up"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/KnifeGunUp.png"));
			else if(dir.equals("down"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/KnifeGunDown.png"));
			else if(dir.equals("right"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/KnifeGunRight.png"));
			else if(dir.equals("left"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/KnifeGunLeft.png"));
			else if(dir.equals("ne") || dir.equals("se"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/KnifeGunRight.png"));
			else if(dir.equals("nw") || dir.equals("sw"))
				background = toolkit.getImage(getClass().getResource("bulletIcons/KnifeGunLeft.png"));		
		}
		else
		{
	    	background = toolkit.getImage(getClass().getResource("bulletIcons/" +weapon+".png"));
		}
    	this.weapon= weapon;
		this.dir= dir;
		this.speed= speed;
		speedY= speed;
		exploded =false;
		Xpos= Xp;
		Ypos= Yp+3;
		XBlock = Xpos / 16;
		YBlock = Ypos / 16;
		smashed= false;
		this.damage= damage;
		pastMilli=System.currentTimeMillis();
		pastMilliRemainder=(long)0;
		milliPerPixel=(long)10;
		explosive= false;
		
		fire(dir);
	}
	public Projectile(String dir, int speed,int Xp, int Yp, String weapon, int damage, int extra)
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
    	background = toolkit.getImage(getClass().getResource("bulletIcons/" +weapon+".png"));
    	this.weapon= weapon;
		this.dir= dir;
		this.speed= speed;
		speedY= speed;
		this.extra= extra;
		Xpos= Xp;
		Ypos= Yp+3;
		exploded= false;
		XBlock = Xpos / 16;
		YBlock = Ypos / 16;
		extra=0;
		smashed= false;
		this.damage= damage;
		pastMilli=System.currentTimeMillis();
		pastMilliRemainder=(long)0;
		milliPerPixel=(long)10;
		explosive= false;
		
		fire(dir);
	}
	public void update()
	{	
		long milli=System.currentTimeMillis()-pastMilli;
		pastMilli=System.currentTimeMillis();
		
		milli+=pastMilliRemainder;
		long currentMilli=milli/milliPerPixel;
		pastMilliRemainder=milli%milliPerPixel;
		XBlock = (Xpos+5) / 16;
		YBlock = (Ypos) / 16;
		if(collision())
		{
			smashed = true;
		}
		int x= 0, y= 0;
		if(dir.equals("up")){
			if(weapon.equals("Shotgun"))
			{
				x+=extra;
				speed=1;
			}
			y= -4;
		}
		else if(dir.equals("down")){
			if(weapon.equals("Shotgun"))
			{
				x+=extra;
				speed=1;
			}
			y= 4;
		}
		else if(dir.equals("right")){
			if(weapon.equals("Shotgun"))
			{
				y+=extra;
				speedY=1;
			}
			x= 4;
		}
		else if(dir.equals("left")){
			if(weapon.equals("Shotgun"))
			{
				y+=extra;
				speedY=1;
			}
			x= -4;
		}
		else if(dir.equals("ne")){
			y= -4;
			x= 4;
			if(weapon.equals("Shotgun"))
			{
				y+=extra;
				speedY=1;
			}
		}
		else if(dir.equals("nw")){
			x= -4;
			y= -4;
			if(weapon.equals("Shotgun"))
			{
				y+=extra;
				speedY=1;
			}
		}
		else if(dir.equals("se")){
			x= 4;
			y= 4;
			if(weapon.equals("Shotgun"))
			{
				y+=extra;
				speedY=1;
			}
		}
		else{
			x= -4;
			y= 4;
			if(weapon.equals("Shotgun"))
			{
				y+=extra;
				speedY=1;
			}
		}
		Xpos= Xpos+(int)(currentMilli*(x*speed));
		Ypos= Ypos+(int)(currentMilli*(y*speedY));
		
		 if(Xpos < 5){
			smashed= true;
			Xpos= 0;
		}
		else if(Xpos > 620){
			smashed= true;
			Xpos=620;
		}
		if(Ypos < 0){
			Ypos= 0;
			smashed= true;
		}
		else if(Ypos > 480){
			smashed= true;
			Ypos = 480;
		}
	}
	public void fire(String dir)
	{	
		long milli=System.currentTimeMillis()-pastMilli;
		pastMilli=System.currentTimeMillis();
		
		milli+=pastMilliRemainder;
		long currentMilli=milli/milliPerPixel;
		pastMilliRemainder=milli%milliPerPixel;
		
		int x= 0, y= 0;
		if(dir.equals("up"))
			y= 1;
		else if(dir.equals("down"))
			y= -1;
		else if(dir.equals("right"))
			x= 1;
		else if(dir.equals("left"))
			x= -1;
		else if(dir.equals("ne")){
			y= 1;
			x= 1;
		}
		else if(dir.equals("nw")){
			x= -1;
			y= 1;
		}
		else if(dir.equals("se")){
			x= 1;
			y= -1;
		}
		else{
			x= -1;
			y= -1;
		}
		Xpos= Xpos+(int)(currentMilli*(x*speed));
		Ypos= Ypos+(int)(currentMilli*(y*speed));
		
		if(Xpos < 0){
			smashed= true;
			Xpos= 0;
		}
		else if(Xpos > 640){
			smashed= true;
			Xpos=5;
		}
		if(Ypos < 0){
			Ypos= 0;
			smashed= true;
		}
		else if(Ypos > 480){
			smashed= true;
			Ypos = 480;
		}
	}
	public boolean collision()
	{
		if(YBlock <= 0)
			return true;
		if(YBlock >= 28)
			return true;
		if(XBlock <= 0)
			return true;
		if(XBlock > 40)
			return true;
		if(level.currentSection.blocks[XBlock][YBlock].type == 1)
			return true;
		return false;
	}
	public void explode(ArrayList<Enemy> enemies)
	{
		exploded= true;
		speed= 0;
		Toolkit toolkit=Toolkit.getDefaultToolkit();
    	background = toolkit.getImage(getClass().getResource("bulletIcons/explosion.png")); 
		String bullet;
		ArrayList<String> blocks= new ArrayList<String>();
		for(int x=-8; x < 9; x++)
		{
			for(int y=-8; y < 9; y++)
			{
				int xNum= XBlock+x;
				int yNum= YBlock+y;
				blocks.add("("+xNum+","+yNum+")");
			}
		}
		System.out.println("Enemies Hit");
		for(int y=0; y < enemies.size(); y++)
		{
			Enemy target= enemies.get(y);
			bullet="("+target.positionXInBlocks+","+target.positionYInBlocks+")";
			for(int x=0; x < blocks.size(); x++)
			{
				if(bullet.equals(blocks.get(x)))
				{
					System.out.println(" "+target.name+" "+damage);
					target.wound(damage);
					x= blocks.size();
				}
			}
		}
	}
}
