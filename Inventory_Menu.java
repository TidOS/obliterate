import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Inventory_Menu extends JPanel implements Menu
{
	public Image crosshair;
	public Image[] weapons;
	public int selectionX=0;
	public int min_selectX=0;
	public int max_selectX=2;
	public int selectionY=0;
	public int min_selectY=0;
	public int max_selectY=3;
	
	public Image dbImage;
	public Graphics dbg;
	
	public Inventory_Menu()
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		crosshair=toolkit.getImage(getClass().getResource("crosshair.png"));
		
		//Load weapon images here
		weapons = new Image[9];
		weapons[0] = toolkit.getImage(getClass().getResource("weapons/pistol/inventory.png"));
		weapons[1] = toolkit.getImage(getClass().getResource("weapons/MachineGun/inventory.png"));
		weapons[2] = toolkit.getImage(getClass().getResource("weapons/Shotgun/inventory.png"));
		weapons[3] = toolkit.getImage(getClass().getResource("weapons/Rocket_Launcher/inventory.png"));
		weapons[4] = toolkit.getImage(getClass().getResource("weapons/Fire_Gun/inventory.png"));
		weapons[5] = toolkit.getImage(getClass().getResource("weapons/KnifeGun/inventory.png"));
		setBackground(Color.black);
	}
	
	public void render(Graphics g,int x,int y,int key,Screen s)
	{
		if(key==38)
		{
			selectionY--;
			if(selectionY<min_selectY)
			{
				selectionY=max_selectY;
			}
		}
		if(key==40)
		{
			selectionY++;
			if(selectionY>max_selectY)
			{
				selectionY=min_selectY;
			}
		}
		if(key==37)
		{
			selectionX--;
			if(selectionX<min_selectX)
			{
				selectionX=max_selectX;
			}
		}
		if(key==39)
		{
			selectionX++;
			if(selectionX>max_selectX)
			{
				selectionX=min_selectX;
			}
		}
		if(key==32)
		{
			//Selected a weapon
			int index=(selectionY*3)+selectionX;
			if(selectionY==3)
			{
				s.game.resetGame();	
				s.game.player.updateSprite();			
				s.changeCurrentMenu(0);
				return;
			}
			else if(s.game.player.weapons[index].ammo>0)
			{
				s.game.player.currentWeapon=index;
				s.game.updateWeaponIcon();
			}
		}
		
		if (dbImage == null)
		{
			dbImage = createImage(x,y);
			dbg = dbImage.getGraphics ();
		}
		dbg.drawImage(s.game.dbImage,0,0,s);
		
		int x_min=70;
		int x_max=x-70;
		int y_min=90;
		int y_max=y-70;
		
		Graphics2D g2d=(Graphics2D)dbg;
		g2d.setComposite(makeComposite((float)0.4));
		Rectangle r = new Rectangle(x-140,y-160);
		g2d.setPaint(Color.black);
		r.setLocation(70,90);
		g2d.fill(r);
		
		r=new Rectangle((x_max-x_min)/3,30);
		r.setLocation(70,60);
		g2d.fill(r);
		r.setLocation(70,410);
		g2d.fill(r);
		
		g2d.setPaint(Color.white);
		dbg.drawLine(x_min,y_min-30,x_min+((x_max-x_min)/3),y_min-30);
		dbg.drawLine(x_min,y_min-30,x_min,y_min);
		dbg.drawLine(x_min+((x_max-x_min)/3),y_min-30,x_min+((x_max-x_min)/3),y_min);
		
		dbg.drawLine(x_min,y_max+30,x_min+((x_max-x_min)/3),y_max+30);
		dbg.drawLine(x_min,y_max+30,x_min,y_max);
		dbg.drawLine(x_min+((x_max-x_min)/3),y_max+30,x_min+((x_max-x_min)/3),y_max);
		g2d.setPaint(Color.black);
		
		dbg.setColor(Color.white);
		dbg.setFont(dbg.getFont().deriveFont((float)24));
		
		dbg.drawString("Inventory",75,82);
		dbg.drawString("Quit Game",75,434);
		
		dbg.setFont(dbg.getFont().deriveFont((float)12));
		
		//Vertical lines
		dbg.drawLine(x_min,y_min,x_min,y_max);
		dbg.drawLine(x_min+((x_max-x_min)/3),y_min,x_min+((x_max-x_min)/3),y_max);
		dbg.drawLine(x_min+(2*((x_max-x_min)/3)),y_min,x_min+(2*((x_max-x_min)/3)),y_max);
		dbg.drawLine(x_max,y_min,x_max,y_max);
		
		//Horizontal lines
		dbg.drawLine(x_min,y_min,x_max,y_min);
		dbg.drawLine(x_min,y_min+((y_max-y_min)/3),x_max,y_min+((y_max-y_min)/3));
		dbg.drawLine(x_min,y_min+(2*((y_max-y_min)/3)),x_max,y_min+(2*((y_max-y_min)/3)));
		dbg.drawLine(x_min,y_max,x_max,y_max);
		
		if(key==s.keys[5])
		{
			Projectile temp;
			s.game.pastMilli=System.currentTimeMillis();
			s.game.pastMilliRemainder=(long)0;
			for(int q=0; q < s.game.bullets.size(); q++)
			{	
				temp= s.game.bullets.get(q);
				temp.pastMilli=System.currentTimeMillis();
				temp.pastMilliRemainder=(long)0;	
			}
			for(int q=0; q < s.game.Ebullets.size(); q++)
			{
				temp= s.game.Ebullets.get(q);
				temp.pastMilli=System.currentTimeMillis();
				temp.pastMilliRemainder=(long)0;
			}
			s.changeCurrentMenu(5);
		}
		
		g2d.setComposite(makeComposite((float)1.0));
		
		for(int y_pic=0; y_pic<3; y_pic++)
		{
			for(int x_pic=0; x_pic<3; x_pic++)
			{
				int index=(y_pic*3)+x_pic;
				dbg.drawImage(weapons[index],x_min+(x_pic*((x_max-x_min)/3)),y_min+(y_pic*((y_max-y_min)/3)),s);
			}
		}
		
		g2d.setComposite(makeComposite((float)0.4));
		dbg.setFont(dbg.getFont().deriveFont((float)20));
		
		dbg.drawString(Integer.toString(s.game.player.weapons[0].ammo), x_min+5, y_min+((y_max-y_min)/3)-5);
		dbg.drawString(Integer.toString(s.game.player.weapons[1].ammo), x_min+((x_max-x_min)/3)+5, y_min+((y_max-y_min)/3)-5);
		dbg.drawString(Integer.toString(s.game.player.weapons[2].ammo), x_min+(2*((x_max-x_min)/3))+5, y_min+((y_max-y_min)/3)-5);
		
		dbg.drawString(Integer.toString(s.game.player.weapons[3].ammo), x_min+5, y_min+(2*((y_max-y_min)/3))-5);
		dbg.drawString(Integer.toString(s.game.player.weapons[4].ammo), x_min+((x_max-x_min)/3)+5, y_min+(2*((y_max-y_min)/3))-5);
		dbg.drawString(Integer.toString(s.game.player.weapons[5].ammo), x_min+(2*((x_max-x_min)/3))+5, y_min+(2*((y_max-y_min)/3))-5);
		
		dbg.drawString(Integer.toString(s.game.player.weapons[6].ammo), x_min+5, y_max-5);
		dbg.drawString(Integer.toString(s.game.player.weapons[7].ammo), x_min+((x_max-x_min)/3)+5, y_max-5);
		dbg.drawString(Integer.toString(s.game.player.weapons[8].ammo), x_min+(2*((x_max-x_min)/3))+5, y_max-5);
		
		// draw image on the screen
		g2d.setComposite(makeComposite((float)1.0));
		if(selectionY==3)
		{
			r=new Rectangle((x_max-x_min)/3,30);
			r.setLocation(70,410);
			g2d.draw(r);
			r=new Rectangle((x_max-x_min)/3,(y_max-y_min)/3);
		}
		else
		{
			r=new Rectangle((x_max-x_min)/3,(y_max-y_min)/3);
			r.setLocation(x_min+(selectionX*(x_max-x_min)/3),y_min+(selectionY*(y_max-y_min)/3));
			g2d.draw(r);
		}
		
		int selectedX=(s.game.player.currentWeapon)%3;
		int selectedY=(s.game.player.currentWeapon)/3;
		dbg.setColor(Color.red);
		//System.out.println("Selected X: " + selectedX);
		//System.out.println("Selected Y: " + selectedY);

		r.setLocation(x_min+(selectedX*(x_max-x_min)/3),y_min+(selectedY*(y_max-y_min)/3));
		
		g2d.setComposite(makeComposite((float)0.8));
		g2d.draw(r);
		g2d.setComposite(makeComposite((float)1.0));
		
		g.drawImage (dbImage,0,25,s);
	}
	
	private AlphaComposite makeComposite(float alpha)
	{
  		int type = AlphaComposite.SRC_OVER;
  		return(AlphaComposite.getInstance(type, alpha));
 	}
	
	public void setSelection(int sx, int sy)
	{
		selectionX=sx;
		selectionY=sy;
	}
}
