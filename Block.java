import java.awt.*;
import java.awt.Event.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Block
{
	//types are as follows:
	// -1.  normal
	// 0.  winning block
	// 1.  non-traversable
	// 2.  a hole trap
	// 3.  a spike trap
	// 4.  health
	// 5.  ammo
	// 6.  ammo (why?)
	public int type;
	public Screen s;
	public Trap trap;
	public Image image;
	public Toolkit toolkit;
	public int x;
	public int y;
	public Block(int typeIn, int xIn, int yIn, Screen sIn)
	{	
		s = sIn;
		x = xIn;
		y = yIn;
		type = typeIn;
		if(typeIn != -1)
		{
			//System.out.println("just made a " + typeIn + " trap");
			trap = new Trap(typeIn, sIn);
			if(type == 4)
			{
				toolkit = Toolkit.getDefaultToolkit();
				image = toolkit.getImage(getClass().getResource("traps/health.gif"));
			}
			else if(type == 5)
			{
				toolkit = Toolkit.getDefaultToolkit();
				image = toolkit.getImage(getClass().getResource("traps/MachineGun.gif"));
			}
			else if(type == 6)
			{
				toolkit = Toolkit.getDefaultToolkit();
				image = toolkit.getImage(getClass().getResource("traps/shotgun.gif"));
			}
			else if(type == 7)
			{
				toolkit = Toolkit.getDefaultToolkit();
				image = toolkit.getImage(getClass().getResource("traps/Rocket.png"));
			}
			else if(type == 8)
			{
				toolkit = Toolkit.getDefaultToolkit();
				image = toolkit.getImage(getClass().getResource("traps/Flame.png"));
			}
			else if(type == 9)
			{
				toolkit = Toolkit.getDefaultToolkit();
				image = toolkit.getImage(getClass().getResource("traps/KnifeGun.png"));
			}
			
			
		}
	}
}
