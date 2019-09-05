import java.awt.*;
import java.awt.Event.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

public class Weapon
{
	public String name;
	public int ammo;
	public int speed;
	public Image image;
	public Toolkit toolkit;
	public Image[][] states;
	public int damage;
	public int timer;

	public Weapon(String s, int speed, int damage, int timer)
	{
		if(s.equals("Pistol"))
		ammo=300;
		else
		ammo = 0;
		name= s;
		this.timer= timer;
		this.speed = speed;
		toolkit=Toolkit.getDefaultToolkit();
		image = toolkit.getImage(getClass().getResource("weapons/" + name + "/right.png"));
		this.damage = damage;
	}
	public void addAmmo(int x)
	{
		ammo+=x;
	}
	public void fillStates()
	{
		//load up all the possible weapon images
		//this will be leftFire, left, rightFire, right, etc
	}
	
	//overload this in weapon children
	public void fire(String dir, int x, int y, ArrayList<Projectile> bullets, Level lvl)
	{
		Projectile bullet;
		bullet= new Projectile(dir, speed, x, y, name, damage);
		bullet.level= lvl;
		if(name.equals("Rocket_Launcher"))
		{
			bullet.explosive= true;
		}
		bullets.add(bullet);
		if(name.equals("Shotgun"))
		{
			bullet= new Projectile(dir, speed, x, y, name, damage, 1);
			bullet.level= lvl;
			bullets.add(bullet);
			bullet= new Projectile(dir, speed, x, y, name, damage, -1);
			bullet.level= lvl;
			bullets.add(bullet);
		}
		ammo--;
	}
}
