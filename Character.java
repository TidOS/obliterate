public abstract class Character
{
	public int positionX;
	public int positionY;
	public int positionXInBlocks;
	public int positionYInBlocks;
	public void move(){}
	// public Projectile fire(){return Projectile;	}
	public int health;
	public String direction;
	public int sizeX;
	public int sizeY;
	int frame[];	//j added:  tells which part of picture to
					//use for the animation.  
	
	public Character()
	{
		sizeX= 1;
		sizeY=-3;
		positionX = 40;
		positionY = 40;
		positionXInBlocks = 40 / 16;
		positionYInBlocks = 40 / 16;
		direction = "down";
	}
	
	
}