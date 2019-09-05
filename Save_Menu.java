import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Save_Menu extends JPanel implements Menu
{
	public Image crosshair;
	public int selection=0;
	public int min_select=0;
	public int max_select=5;
	public String[] timestamp;
	
	public Image dbImage;
	public Graphics dbg;
	
	public Save_Menu()
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		crosshair=toolkit.getImage(getClass().getResource("crosshair.png"));
		timestamp=new String[5];
		
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
			//Selected a file to load
			if(selection!=max_select)
			{
				if(selection>=min_select && selection<max_select)
				{
					saveGame("save" + selection + ".sav", s);
					dbImage=null;
				}
			}
			else
			{
				s.changeCurrentMenu(5);
			}
			
		}
		
		//initialize buffer
		if (dbImage == null)
		{
			dbImage = createImage(x,y);
			dbg = dbImage.getGraphics();
			timestamp=new String[5];
			int i=min_select;
			while(i<max_select)
			{
				boolean test=false;
				try
				{
					File f=new File("save" + i + ".sav");
					if(f.exists())
					{
						//FileReader fr=new FileReader(f);
						//BufferedReader br=new BufferedReader(fr);
						//String temp=br.readLine();
						Date d=new Date(f.lastModified());
						String temp=d.toString();
						if(temp!=null)
						{
							timestamp[i]=temp;
						}
						else
						{
							test=true;
						}
					}
					else
					{
						test=true;
					}
				}
				catch(Exception e)
				{
					test=true;
				}
				
				if(test)
				{
					timestamp[i]="<No saved data>";
				}
				i++;
			}
		}
		//clear screen in background
		dbg.setColor(Color.black);
		dbg.fillRect(0, 0, x, y);
		
		dbg.setColor(Color.white);
		dbg.setFont(dbg.getFont().deriveFont((float)24));
		dbg.drawString("Save Game",50,80);
		dbg.setFont(dbg.getFont().deriveFont((float)18));
		
		//Check for files...
		dbg.drawString(timestamp[0],50,120);
		dbg.drawString(timestamp[1],50,150);
		dbg.drawString(timestamp[2],50,180);
		dbg.drawString(timestamp[3],50,210);
		dbg.drawString(timestamp[4],50,240);
		
		
		dbg.drawString("Continue",50,270);
		
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

		// draw image on the screen
		g.drawImage (dbImage,0,0,s);
	}
	
	public void setSelection(int s)
	{
		selection=s;
	}
	
	public boolean saveGame(String input, Screen s)
	{
		GameSave gs = new GameSave(s);
		
		try
		{
			FileOutputStream fos = new FileOutputStream(input);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(gs);
			out.close();
			return true;
		}
		catch(Exception e){}
		
		return false;
	}
}
