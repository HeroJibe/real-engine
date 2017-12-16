/**
 * The GameEvent abstract class provides a
 * framework for programmable events which 
 * can be called in .map or .prefab files.
 * 
 * @author Ethan Vrhel
 * @version 1.0
 * @since 2017-09-01
 */

package core;

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
	public String getName()
	{
		return name;
	}
	
	/**
	 * Enables the GameEvent
	 */
	public void enable()
	{
		run = true;
	}
	
	/**
	 * Disables the GameEvent
	 */
	public void disable()
	{
		run = false;
	}
	
	/**
	 * Whether the Entity should execute
	 * 
	 * @return
	 */
	public boolean shouldRun()
	{
		return run;
	}
	
	/**
	 * Updates the GameEvent
	 */
	public abstract void update();
}
