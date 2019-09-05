import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Configure_Menu extends JPanel implements Menu
{
	public Image crosshair;
	public int selection=0;
	public int min_select=0;
	public int max_select=7;
	public String message="";
	public boolean cantIO=false;
	
	public Image dbImage;
	public Graphics dbg;
	
	public Configure_Menu()
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		crosshair=toolkit.getImage(getClass().getResource("crosshair.png"));
		
		setBackground(Color.black);
	}
	
	public void render(Graphics g,int x,int y,int key,Screen s)
	{	
		if(s.configInput)
		{
			boolean test=false;
			int i=min_select;
			while(i<(max_select-1))
			{
				if(s.keys[i]==s.configKey)
				{
					test=true;
				}
				i++;
			}
			
			if(!test)
			{
				s.keys[selection]=s.configKey;
				message="Select a control to configure.";
			}
			else
			{
				message="Key is in use. Select a control to configure.";
			}
			
			s.configInput=false;
			s.inConfig=false;
			
		}
		else if(!s.inConfig)
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
				//Selected a key to change
				if(selection<(max_select-1) && !cantIO)
				{
					s.inConfig=true;
					message="Input new control";
				}
				else if(selection==(max_select-1))
				{
					s.keys[0]=38;
					s.keys[1]=37;
					s.keys[2]=39;
					s.keys[3]=40;
					s.keys[4]=32;
					s.keys[5]=27;
				}
				else
				{
					saveConfig(s);
					s.changeCurrentMenu(0);
				}
				
			}
		}
		
		//initialize buffer
		if (dbImage == null)
		{
			dbImage = createImage(x,y);
			dbg = dbImage.getGraphics();
			if(cantIO)
			{
				message="You do not have correct IO permissions.";
			}
			else
			{
				message="Select a control to configure.";
			}
		}
		
		//clear screen in background
		dbg.setColor(Color.black);
		dbg.fillRect(0, 0, x, y);
		
		dbg.setColor(Color.white);
		dbg.setFont(dbg.getFont().deriveFont((float)24));
		dbg.drawString("Configure",50,80);
		dbg.setFont(dbg.getFont().deriveFont((float)18));
		
		dbg.drawString("Up",50,120);
		dbg.drawString("Left",50,150);
		dbg.drawString("Right",50,180);
		dbg.drawString("Down",50,210);
		dbg.drawString("Fire",50,240);
		dbg.drawString("Inventory",50,270);
		
		dbg.drawString("Reset to Default",50,300);
		dbg.drawString("Main Menu",50,330);
		
		dbg.drawString(KeyEvent.getKeyText(s.keys[0]),200,120);
		dbg.drawString(KeyEvent.getKeyText(s.keys[1]),200,150);
		dbg.drawString(KeyEvent.getKeyText(s.keys[2]),200,180);
		dbg.drawString(KeyEvent.getKeyText(s.keys[3]),200,210);
		dbg.drawString(KeyEvent.getKeyText(s.keys[4]),200,240);
		dbg.drawString(KeyEvent.getKeyText(s.keys[5]),200,270);
		
		dbg.drawString(message,50,450);
		dbg.setFont(dbg.getFont().deriveFont((float)16));
		dbg.drawString("Info",50,430);
		
		if(selection==0)
		{
			dbg.drawImage(crosshair,20,104,s);
		}
		
		if(selection==1)
		{
			dbg.drawImage(crosshair,20,134,s);
		}
		
		if(selection==2)
		{
			dbg.drawImage(crosshair,20,164,s);
		}
		
		if(selection==3)
		{
			dbg.drawImage(crosshair,20,194,s);
		}
		
		if(selection==4)
		{
			dbg.drawImage(crosshair,20,224,s);
		}
		
		if(selection==5)
		{
			dbg.drawImage(crosshair,20,254,s);
		}
		
		if(selection==6)
		{
			dbg.drawImage(crosshair,20,284,s);
		}
		
		if(selection==7)
		{
			dbg.drawImage(crosshair,20,314,s);
		}

		// draw image on the screen
		g.drawImage (dbImage,0,0,s);
	}
	
	public boolean loadConfig(Screen s)
	{
		try
		{
			FileReader fr = new FileReader("configure.txt");
			BufferedReader br=new BufferedReader(fr);
			s.keys[0]=Integer.parseInt(br.readLine());
			s.keys[1]=Integer.parseInt(br.readLine());
			s.keys[2]=Integer.parseInt(br.readLine());
			s.keys[3]=Integer.parseInt(br.readLine());
			s.keys[4]=Integer.parseInt(br.readLine());
			s.keys[5]=Integer.parseInt(br.readLine());
			return true;
		}
		catch(Exception e)
		{
			//up
			s.keys[0]=38;
			
			//left
			s.keys[1]=37;
			
			//right
			s.keys[2]=39;
			
			//down
			s.keys[3]=40;
			
			//space bar
			s.keys[4]=32;
			
			//esc
			s.keys[5]=27;
			
			cantIO=true;
		}
		return false;
	}
	
	public void setSelection(int s)
	{
		selection=s;
	}
	
	public boolean saveConfig(Screen s)
	{
		try
		{
			File f=new File("configure.txt");
			if(f.exists())
			{
				f.delete();
				f.createNewFile();
			}
			
			FileWriter fw=new FileWriter(f);
			BufferedWriter bw=new BufferedWriter(fw);
			
			String output="";
			int i=0;
			while(i<6)
			{
				output="" + s.keys[i] + "\n";
				bw.write(output,0,output.length());
				i++;
			}
			
			bw.close();
			
			return true;
		}
		catch(IOException e)
		{
			cantIO=true;
		}
		
		return false;
	}
}
