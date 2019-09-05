import java.awt.*;
import java.awt.Event.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Screen extends JFrame implements KeyListener, Runnable
{
	public int height=480;
	public int width=640;
	public long loopInterval=0;
	public int[] keys;
	public boolean[] keysHeld;
	public boolean inConfig=false;
	public boolean configInput=false;
	
	public Main_Menu main			= new Main_Menu();
	public Load_Menu load			= new Load_Menu();
	public Configure_Menu config	= new Configure_Menu();
	public Save_Menu save			= new Save_Menu();
	public Inventory_Menu inventory	= new Inventory_Menu();
	public Game game				= new Game(this);
	public GameOver gameOver = new GameOver();
	public GameWin gameWin = new GameWin();
	
	public int currentMenu=0;
	public int pastMenu=0;
	public int pressedKey=0;
	public int configKey=0;
	
	public Thread gameThread;
	public long nextCheck;
	public Image icon;
	
	public BufferedReader br;
	public PrintWriter pw;
	
	public Screen()
	{
		super("Obliterate");
		init();
	}
	
	public void init()
	{
		setSize(width,height);
		setBackground(Color.black);
		
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		icon=toolkit.getImage(getClass().getResource("icon.gif"));
		setIconImage(icon);
		
		Dimension screenSize = getToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		this.setLocation(screenWidth/2-width/2, screenHeight/2-height/2);
		
		add(main);
		add(load);
		add(config);
		add(save);
		add(inventory);
		add(game);
		add(gameOver);
		add(gameWin);
		
		addKeyListener(this);
		
		currentMenu=0;
		keys=new int[6];
		keysHeld=new boolean[6];
		keysHeld[0]=false;
		keysHeld[1]=false;
		keysHeld[2]=false;
		keysHeld[3]=false;
		keysHeld[4]=false;
		keysHeld[5]=false;
		
		config.loadConfig(this);
		
		repaint();
		setResizable(false);
		setVisible(true);
	}
	
	public void keyTyped(KeyEvent evt) {}
	
	public void keyPressed(KeyEvent evt)
	{
		pressedKey=evt.getKeyCode();
		//System.out.println("Key: " + pressedKey);
		if(inConfig)
		{
			configKey=pressedKey;
			configInput=true;
		}
		else
		{
			configKey=0;
		}
		int i=0;
		while(i<6)
		{
			if(pressedKey==keys[i])
			{
				keysHeld[i]=true;
			}
			i++;
		}
		repaint();
	}

	public void keyReleased(KeyEvent evt)
	{
		int key=evt.getKeyCode();
		int i=0;
		while(i<6)
		{
			if(key==keys[i])
			{
				keysHeld[i]=false;
			}
			i++;
		}
		pressedKey=0;
	}
	
	public void run()
	{
		while(game!=null)
		{
			try
			{
 				Thread.sleep(Math.max(25,nextCheck-System.currentTimeMillis()));
 					//Thread.sleep(0);					
			} catch (InterruptedException e) {}
			nextCheck=System.currentTimeMillis()+loopInterval;
			if(currentMenu==5)
			{
				repaint();
			}
		}
	}
	
	public void start()
	{
		if (gameThread==null)
		{
			gameThread=new Thread(this);
			gameThread.start();
			game.newGame(4);
			nextCheck=System.currentTimeMillis()+loopInterval;
		}
	}
 
	public void stop()
	{
		if ((gameThread!=null)&&(gameThread.isAlive()))
		{
			gameThread.stop();
		}
		gameThread=null;
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void paint(Graphics g)
	{	
		Point p = main.getLocationOnScreen();
		Dimension screenSize = getToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		int x=this.getSize().width;
		int y=this.getSize().height;
		
		//Main Menu
		if(currentMenu==0)
		{
			main.render(g, x, y, pressedKey, this);
		}
		
		//Load Menu
		if(currentMenu==1)
		{
			load.render(g, x, y, pressedKey, this);
		}
		
		//Configure Menu
		if(currentMenu==2)
		{
			config.render(g, x, y, pressedKey, this);
		}
		
		//Save Menu
		if(currentMenu==3)
		{
			save.render(g, x, y, pressedKey, this);
		}
		
		//Inventory Menu
		if(currentMenu==4)
		{
			inventory.render(g, x, y, pressedKey, this);
		}
		
		//In-game display
		if(currentMenu==5)
		{
			game.render(g, x, y, pressedKey, this);
		}
		
		if(currentMenu==6)
		{
			gameOver.render(g,x,y,pressedKey, this);
			try{Thread.sleep(5000);} catch(Exception e) {}
			currentMenu = 0;
		}
		
		if(currentMenu==7)
		{
			gameWin.render(g,x,y,pressedKey, this);
			try{Thread.sleep(5000);} catch(Exception e) {}
			System.exit(0);
			currentMenu = 0;
		}
		
		//Change menu transition
		if(currentMenu!=pastMenu && (currentMenu!=4 || currentMenu!=5))
		{
			g.setColor(Color.black);
			g.fillRect(0, 0, x, y);
			
			pastMenu=currentMenu;
			paint(g);
		}
		pastMenu=currentMenu;
	}
	
	public void changeCurrentMenu(int m)
	{
		currentMenu=m;
		inConfig=false;
		configInput=false;
		pressedKey=0;
		configKey=0;
		load.setSelection(0);
		load.dbImage=null;
		save.setSelection(0);
		save.dbImage=null;
		config.setSelection(0);
		inventory.setSelection(0,0);
		//inventory.dbImage = null;
		if(config.cantIO)
		{
			config.message="You do not have correct IO permissions.";
		}
		else
		{
			config.message="Select a control to configure.";
		}
		repaint();
	}
	
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {}
		
		Screen s = new Screen();
		s.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent event) { System.exit(0);} });
		s.start();
	}
}
