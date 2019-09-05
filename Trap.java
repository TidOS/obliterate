import java.awt.*;
import java.awt.Event.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Trap
{
	public int type;
	public Screen s;
	public boolean used;
	public Image image;
	public Trap(int typeIn, Screen sIn)
	{
		type = typeIn;
		s = sIn;
		used = false;
	}

	public void action(Screen sIn)
	{
		if(type == 2)
		{
			//hole
				sIn.game.player.lives--;
				sIn.game.player.health  = 100;
				sIn.game.restartLevel();
		}
		else if(type == 3)
		{
			//spike
			sIn.game.player.health -= 1;
		}
		else if(type == 4)
		{
			if(used == false)
			{
				sIn.game.player.health += 10;
				used = true;
				s.game.removeTrap();
			}
		}
		//machinegun ammo?
		else if(type == 5)
		{
			if(used == false)
			{
				sIn.game.player.weapons[1].ammo += 20;
				used = true;
				s.game.removeTrap();
			}
		}
		
		//shotgun
		else if(type == 6)
		{
			if(used == false)
			{
				sIn.game.player.weapons[2].ammo += 10;
				used = true;
				s.game.removeTrap();
			}
		}
		
		//rocket
		else if(type == 7)
		{
			if(used == false)
			{
				sIn.game.player.weapons[3].ammo += 5;
				used = true;
				s.game.removeTrap();
			}
		}

		//fire
		else if(type == 8)
		{
			if(used == false)
			{
				sIn.game.player.weapons[4].ammo += 10;
				used = true;
				s.game.removeTrap();
			}
		}
		
		//KNIFE GUN!!!!eleven!1
		else if(type == 9)
		{
			if(used == false)
			{
				sIn.game.player.weapons[5].ammo += 400;
				used = true;
				s.game.removeTrap();
			}
		}

	}

}
