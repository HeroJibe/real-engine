package core;

public abstract class GameEvent
{
	private String name;
	private boolean run;
	
	public GameEvent(String name)
	{
		this.name = name;
		run = false;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void enable()
	{
		run = true;
	}
	
	public void disable()
	{
		run = false;
	}
	
	public boolean shouldRun()
	{
		return run;
	}
	
	public abstract void update();
}
