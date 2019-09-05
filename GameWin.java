import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class GameWin extends JPanel implements Menu
{
	private Image title;
	private Image crosshair;
	private int min_select=0;
	private int max_select=2;
	
	private Image dbImage;
	private Graphics dbg;
	private MediaTracker mt;
	
	public GameWin()
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		title=toolkit.getImage(getClass().getResource("backgrounds/youwin.png"));
		mt=new MediaTracker(this);
		setBackground(Color.black);
		mt.addImage(title,0);
		
		try
    	{
      		mt.waitForAll();
    	} catch(Exception e){};
		
		
		setBackground(Color.black);
	}
	
	public void render(Graphics g,int x,int y,int key,Screen s)
	{
		// initialize buffer
		if (dbImage == null)
		{
			dbImage = createImage(x,y);
			dbg = dbImage.getGraphics ();
		}
		// clear screen in background
		dbg.setColor(Color.black);
		dbg.fillRect(0, 0, 640, 480);
		
		//Create image here...
		dbg.drawImage(title,0,25,s);
		g.drawImage (dbImage,0,0,s);
	}
	
	public void setSelection(int s)
	{
	}
}
