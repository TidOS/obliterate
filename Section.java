import java.awt.*;
import java.awt.Event.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Section
{
	public Image background;
	private Screen s;
	public int sectX;
	public int sectY;
	public Block[][] blocks;
	public ArrayList<Enemy> enemies;
	public ArrayList<Block> traps;
	public int lnum;
	public ArrayList<Projectile> projectiles;
	public Toolkit toolkit;
	
    public Section(Screen sIn, int levelNum, int sectXIn, int sectYIn)
	{
		s = sIn;
		lnum = levelNum;
		Toolkit toolkit=Toolkit.getDefaultToolkit();
        background = toolkit.getImage(getClass().getResource("backgrounds/" + levelNum + sectXIn + sectYIn + ".png"));
      if(background == null)
        background = toolkit.getImage(getClass().getResource("backgrounds/blank.png"));     		
    	sectX = sectXIn;
    	sectY = sectYIn;
    	projectiles = new ArrayList<Projectile>();
    	traps = new ArrayList<Block>();
    	blocks = new Block[40][30];
    	loadBlocks();
			enemies= new ArrayList<Enemy>();
    	loadEnemies();
 
    }
   
  private void loadEnemies()
  {
			//enemies.add(new Enemy(10, "streetthug", s.game.player, 500, 100, 5));
			//enemies.add(new Enemy(20, "bluedemon", s.game.player, 200, 100, 20));
			//public Enemy(int health, String name, Player p, int x, int y, int damage)

			//enemies are defined as so:
			//health name x y damage
						int count = 1;

		try
		{
			FileReader fr = new FileReader("./" + lnum + sectX + sectY + ".enemies");//Reads the file name
			BufferedReader br = new BufferedReader(fr);//Checks the file.
			String line = br.readLine();//Reads first line of text.
			StringTokenizer st;
			String movements;
			Integer intTmp;
			String tmpName;
			int tmpx, tmpy, tmpHealth, tmpDamage;
			Enemy tmpEnemy;
			while(line != null)//while there is something on the line.
			{
				if(line == "")
				{
					line = null; // we are done.
				}
				else
				{
					st = new StringTokenizer(line);
					intTmp = Integer.parseInt(st.nextToken());
					tmpHealth = intTmp.intValue();
					tmpName = st.nextToken();
					intTmp = Integer.parseInt(st.nextToken());
					tmpx = intTmp.intValue();
					intTmp = Integer.parseInt(st.nextToken());
					tmpy = intTmp.intValue();
					intTmp = Integer.parseInt(st.nextToken());
					tmpDamage = intTmp.intValue();
					movements = st.nextToken();
					tmpEnemy = new Enemy(tmpHealth, tmpName, s.game.player, tmpx, tmpy, tmpDamage);
					while(!(movements.equals("|")))
					{
						tmpEnemy.moves.add(movements);
						movements = st.nextToken() ;
					}
					line = br.readLine();
//					System.out.println("block [" +x +"][" + y+ "] goin in as type" + type);
					enemies.add(tmpEnemy);
					count++;
				}
			}
			
			br.close();//Closes the buffer.
		}
		catch(FileNotFoundException fnfe)
		{
		//	System.out.println("bad file name: " + lnum + sectX +  sectY + ".enemies");
		}
		catch(IOException ioe)
		{
			//System.out.println("Input / Output Exception found:\n\r"+ioe.getMessage());
		}
		catch(NoSuchElementException nse)
		{
			//System.out.println("Malformed enemy in: " + lnum + sectX + sectY + ".enemies line " + count);
		}			
  }
    
	private void loadBlocks()
	{
		try
		{
			FileReader fr = new FileReader("./" + lnum + sectX + sectY + ".section");//Reads the file name
			BufferedReader br = new BufferedReader(fr);//Checks the file.
			String line = br.readLine();//Reads first line of text.
			StringTokenizer st;
			Integer intTmp;
			int x, y, type;
	
			while(line != null)//while there is something on the line.
			{
				if(line == "")
				{
					line = null; // we are done.
				}
				else
				{
					st = new StringTokenizer(line);
					intTmp = Integer.parseInt(st.nextToken());
					x = intTmp.intValue();
					intTmp = Integer.parseInt(st.nextToken());
					y = intTmp.intValue();
					intTmp = Integer.parseInt(st.nextToken());
					type = intTmp.intValue();
					line = br.readLine();
//					System.out.println("block [" +x +"][" + y+ "] goin in as type" + type);
					setBlock(x,y,type);
				}
			}
			
			br.close();//Closes the buffer.
		}
		catch(FileNotFoundException fnfe)
		{

			//System.out.println("bad file name: " + lnum + sectX +  sectY + ".section");
		}
		catch(IOException ioe)
		{
			//System.out.println("Input / Output Exception found:\n\r"+ioe.getMessage());
		}
	}
    
    public void setBlock(int x, int y, int type)
    {
    	blocks[x][y] = new Block(type,x,y,s);
    	blocks[x][y].type = type;
    	if(type >= 4)
    	{
    		traps.add(blocks[x][y]);
    	}
    }
}
