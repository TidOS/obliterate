import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import java.io.*;
import java.util.*;

public class SectionGUI extends JPanel implements ActionListener, MouseListener
{
	private JPanel options;
	private JPanel sectionImage;
	
	private JComboBox typeBlock;
	private JTextField imageFile;
	private JTextField sectionFile;
	private JButton load;
	private JButton save;
	
	private Image img;
	private int blockSize=16;
	private int width=640/blockSize;
	private int height=452/blockSize; //Why are we using 452 pixel high images?
												//so the whole window will be 640x480 I think -- jordan
	private int[][] blocks=new int[width][height];
	
	public SectionGUI()
	{	
		int i=0;
		int i2=0;
		while(i<height)
		{
			while(i2<width)
			{
				blocks[i2][i]=-1;
				i2++;
			}
			i2=0;
			i++;
		}
		
		setLayout(new BorderLayout());
		
		options = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        
        JLabel blank1 = new JLabel(" ");
        JLabel blank2 = new JLabel(" ");
        JLabel blank3 = new JLabel(" ");
        JLabel blank4 = new JLabel(" ");
        JLabel iText = new JLabel("Image File:");
        JLabel sText = new JLabel("Section File:");
        
        options.setLayout(gridbag);
        
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
		
		JPanel section = new JPanel();
		section.setLayout(gridbag);
		
		//Included 8 ammo types (One gun will have inf. ammo)
		//The ammo shows up as a dark gray box with a number
		//inside corresponding to the ammo type.
		String[] types = { "-1: Normal (Gray)", "0: Winning (Yellow)", 
			"1: Non-traversable (Blue/Purple)", "2: Hole trap (Green)", 
			"3: Spike trap (Red/Orange)", "4: Health (Cyan)",
			"5: Ammo1 (Dark Gray)", "6: Ammo2 (Dark Gray)",
			"7: Ammo3 (Dark Gray)", "8: Ammo4 (Dark Gray)",
			"9: Ammo5 (Dark Gray)", "10: Ammo6 (Dark Gray)",
			"11: Ammo7 (Dark Gray)", "12: Ammo8 (Dark Gray)"   };
		
		typeBlock = new JComboBox(types);
		section.add(typeBlock, c);
		typeBlock.setBackground(Color.white);
		
		section.add(blank1, c);
		
		section.add(iText, c);
		imageFile=new JTextField(20);
		section.add(imageFile, c);
		
		section.add(blank2, c);
		
		section.add(sText, c);
		sectionFile=new JTextField(20);
		section.add(sectionFile, c);
		
		section.add(blank3, c);
		
		load = new JButton("Load");
		section.add(load, c);
		load.addActionListener(this);
		load.setActionCommand("Load");
		
		section.add(blank4, c);
		
		save = new JButton("Save");
		section.add(save, c);
		save.addActionListener(this);
		save.setActionCommand("Save");
		
		section.setBorder(
                BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder("Section Options"),
					BorderFactory.createEmptyBorder(5,5,5,5)
				)
		);
		
		options.add(section, c);
		
		sectionImage = new JPanel();
		sectionImage.setBackground(Color.white);
		sectionImage.setSize(640,480);
		
		add(sectionImage, BorderLayout.CENTER);
		add(options, BorderLayout.LINE_END);
        
        sectionImage.addMouseListener(this);
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Load"))
		{
			reloadBlocks();
			redraw();
		}
		
		if(e.getActionCommand().equals("Save"))
		{
			String sf=sectionFile.getText();
			try
			{
				File f=new File(sf);
				if(f.exists())
				{
					f.delete();
					f.createNewFile();
				}
				
				FileWriter fw=new FileWriter(f);
				BufferedWriter bw=new BufferedWriter(fw);
				
				String output="";
				
				int i=0;
				int i2=0;
				while(i<height)
				{
					while(i2<width)
					{
						output="" + i2 + " " + i + " " + blocks[i2][i] + "\n";
						bw.write(output,0,output.length());
						i2++;
					}
					i2=0;
					i++;
				}
				
				bw.close();
			}
			catch(IOException ioe){}
		}
	}
	
	public void reloadBlocks()
	{
		defaultBlockSettings();
		
		String sf=sectionFile.getText();
		try
		{
			FileReader fr = new FileReader(sf);//Reads the file name
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
					blocks[x][y]=type;
				}
			}
			
			br.close();//Closes the buffer.
			return;
		}
		catch(FileNotFoundException fnfe){}
		catch(IOException ioe){}
		
		defaultBlockSettings();
	}
	
	public void defaultBlockSettings()
	{
		int i=0;
		int i2=0;
		while(i<height)
		{
			while(i2<width)
			{
				if(i<=1)
					blocks[i2][i]=1;
				else
					blocks[i2][i]=-1;
				i2++;
			}
			i2=0;
			i++;
		}
	}
	
	public void drawGrid(Graphics g)
	{
		g.setColor(Color.white);
		int x=-1;
		while(x<640)
		{
			g.drawLine(x,0,x,480);
			x+=blockSize;
		}
		
		int y=-1;
		while(y<480)
		{
			g.drawLine(0,y,640,y);
			y+=blockSize;
		}
	}
	
	public void mouseClicked(MouseEvent e)
	{
		//Check to make sure the click was in the section image
		Point p=this.getMousePosition(true);
		if(p!=null)
		{
			//System.out.println("Cursor at: " + p.x + "," + p.y);
			int blockX=p.x/blockSize;
			int blockY=p.y/blockSize;
			//blockY=height-blockY-1;
			System.out.println("In Block: " + blockX + "," + blockY);
			
			int type = typeBlock.getSelectedIndex()-1;
			blocks[blockX][blockY]=type;
		}
		redraw();
	}
	
	//Not used
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	
		
	public void redraw()
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		img=toolkit.getImage(getClass().getResource(imageFile.getText()));
		Graphics g=sectionImage.getGraphics();
		g.drawImage(img, 0, 0, sectionImage);
		drawGrid(g);
		colorTiles(g);
	}
	
	public void colorTiles(Graphics g)
	{
		Graphics2D g2d=(Graphics2D)g;
		g2d.setComposite(makeComposite((float)0.4));
		Rectangle r = new Rectangle(blockSize,blockSize);
		g2d.setPaint(Color.lightGray);
		
		int i=0;
		int i2=0;
		while(i<height)
		{
			while(i2<width)
			{
				r.setLocation(i2*blockSize,i*blockSize);
				//r.setLocation(i2*blockSize,((height-1-i)*blockSize));
				int type=blocks[i2][i];
				if(type==-1)
					g2d.setPaint(Color.lightGray);
				if(type==0)
					g2d.setPaint(Color.yellow);
				if(type==1)
					g2d.setPaint(Color.blue);
				if(type==2)
					g2d.setPaint(Color.green);
				if(type==3)
					g2d.setPaint(Color.red);
				if(type==4)
					g2d.setPaint(Color.cyan);
				if(type>=5)
					g2d.setPaint(Color.darkGray);
				g2d.fill(r);
				if(type>=5)
				{
					g2d.setComposite(makeComposite((float)0.8));
					g2d.setPaint(Color.white);
					g2d.drawString("" + (type-4),(i2*blockSize)+5,(i*blockSize)+blockSize-4);
					g2d.setComposite(makeComposite((float)0.4));
				}
				i2++;
			}
			i2=0;
			i++;
		}
	}
	
	private AlphaComposite makeComposite(float alpha)
	{
  		int type = AlphaComposite.SRC_OVER;
  		return(AlphaComposite.getInstance(type, alpha));
 	}
	
	public static void main (String[] args)
	{
		//Set the UI look and feel to the systems setting
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {}
		
		//Setup the planner gui
		JFrame frame = new JFrame("Section Maker");
		frame.addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent event) { 
				System.exit(0);
			} 
		});
		frame.setSize(400,300);
		
		//Pack the planner and display
		frame.add(new SectionGUI());
		frame.setSize(853,480);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}