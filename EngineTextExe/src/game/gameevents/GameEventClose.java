package game.gameevents;

import core.GameEvent;
import main.Main;

public class GameEventClose 
	extends GameEvent
{

	public GameEventClose(String name) 
	{
		super(name);
	}

	public void update() 
	{
		System.exit(0);
	}
}