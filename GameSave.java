import java.io.Serializable;

public class GameSave implements Serializable
{
	public int level;
	public int max_health;
	public int health;
	public int[] ammo;
	public int lives;
	public int size;
	
	public GameSave(Screen s)
	{
		ammo = new int[9];
		level = s.game.currentLevel.num;
		max_health = s.game.player.maxHealth;
		health = s.game.player.health;
		lives = s.game.player.lives;
		size = s.game.currentLevel.size;
		for(int i=0; i<9; i++)
		{
			ammo[i]=s.game.player.weapons[i].ammo;
		}
	}
}
