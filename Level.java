import java.awt.*;
import java.awt.Event.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
public class Level
{

	public boolean isComplete;
	public Section[][] sections;
	public int num;
	private Screen s;
	public Section currentSection;
	public int currentSectionX;
	public int currentSectionY;
	public int size;

	public Level(int levelNum, Screen sIn, int size)
	{
			this.size = size;
    	isComplete = false;
    	s = sIn;
    	sections = new Section[size][size];
    	//switchSection(0,0);
    	num = levelNum;
    	for(int i = 0; i < size; i++)
			{
				for(int j = 0; j < size; j++)
				{
					sections[i][j] = new Section(sIn,num,i+1,j+1);
				}
			}
    	
//    	sections[0][0] = new Section(sIn,num,1,1);
//    	sections[1][0] = new Section(sIn,num,2,1);
//    	sections[2][0] = new Section(sIn,num,3,1);
    	
//    	sections[0][1] = new Section(sIn,num,1,2);
//    	sections[1][1] = new Section(sIn,num,2,2);
//			sections[2][1] = new Section(sIn,num,3,2);
			
//			sections[0][2] = new Section(sIn,num,1,3);
//			sections[1][2] = new Section(sIn,num,2,3);
//			sections[2][2] = new Section(sIn,num,3,3);
    	
    	currentSectionX = 0;
    	currentSectionY = 0;
    	//switchSection(0,0);
	}    
	
	public void switchSection(int x, int y)
	{
		currentSection = sections[x][y];
		currentSectionX=x;
		currentSectionY=y;
		s.game.player.setBlocks();
	}
}
