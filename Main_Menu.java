import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Main_Menu extends JPanel implements Menu
{
	private Image title;
	private Image crosshair;
	private int selection=0;
	private int min_select=0;
	private int max_select=2;
	
	private Image dbImage;
	private Graphics dbg;
	private MediaTracker mt;
	
	public Main_Menu()
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		title=toolkit.getImage(getClass().getResource("obliterate_title.png"));
		crosshair=toolkit.getImage(getClass().getResource("crosshair.png"));
		mt=new MediaTracker(this);
		
		mt.addImage(title,0);
		mt.addImage(crosshair,0);
		
		try
    	{
      		mt.waitForAll();
    	} catch(Exception e){};
		
		
		setBackground(Color.black);
	}
	
	public void render(Graphics g,int x,int y,int key,Screen s)
	{
		if(key==38)
		{
			selection--;
			if(selection<min_select)
			{
				selection=max_select;
			}
		}
		if(key==40)
		{
			selection++;
			if(selection>max_select)
			{
				selection=min_select;
			}
		}
		if(key==32)
		{
			//New Game
			if(selection==0)
			{
				s.changeCurrentMenu(5);
			}
			
			//Load Game
			if(selection==1)
			{
				s.changeCurrentMenu(1);
			}
			
			//Configure Menu
			if(selection==2)
			{
				s.changeCurrentMenu(2);
			}
		}
		// initialize buffer
		if (dbImage == null)
		{
			dbImage = createImage(x,y);
			dbg = dbImage.getGraphics ();
		}
		// clear screen in background
		dbg.setColor(Color.black);
		dbg.fillRect(0, 0, x, y);
		
		//Create image here...
		dbg.drawImage(title,90,0,s);
		dbg.setColor(Color.white);
		dbg.setFont(dbg.getFont().deriveFont((float)24));
		dbg.drawString("New Game",90,360);
		dbg.drawString("Load Game",90,390);
		dbg.drawString("Configure",90,420);
		
		if(selection==0)
		{
			dbg.drawImage(crosshair,65,342,s);
		}
		
		if(selection==1)
		{
			dbg.drawImage(crosshair,65,372,s);
		}
		
		if(selection==2)
		{
			dbg.drawImage(crosshair,65,402,s);
		}

		// draw image on the screen
		g.drawImage (dbImage,0,0,s);
	}
	
	public void setSelection(int s)
	{
		selection=s;
	}
}
