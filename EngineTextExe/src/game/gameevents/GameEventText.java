package game.gameevents;

import core.GameEvent;

public class GameEventText 
	extends GameEvent
{
	private String text = "";
	private int x;
	private int y;

	public GameEventText(String name, String text, int x, int y) 
	{
		super(name);
		this.text = text;
		this.x = x;
		this.y = y;
	}

	public void update()
	{
		
	}

}
