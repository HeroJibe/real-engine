package core;

/**
 * The GameEvent abstract class provides a
 * framework for programmable events which 
 * can be called in .map or .prefab files.
 * 
 * @author Ethan Vrhel
 */
public abstract class GameEvent
{
	private String name;		// The GameEvent's name
	private boolean run;		// Whether the GameEvent should execute
	
	public GameEvent(String name)
	{
		this.name = name;
		run = false;
	}
	
	/**
	 * Returns the GameEvent's name
	 * 
	 * @return
	 */
	public final String getName()
	{
		return name;
	}
	
	/**
	 * Enables the GameEvent
	 */
	public final synchronized void enable()
	{
		run = true;
	}
	
	/**
	 * Disables the GameEvent
	 */
	public final synchronized void disable()
	{
		run = false;
	}
	
	/**
	 * Whether the Entity should execute
	 * 
	 * @return
	 */
	public final synchronized boolean shouldRun()
	{
		return run;
	}
	
	/**
	 * Updates the GameEvent
	 */
	public abstract void update();
}
