package core;

import java.awt.Color;

import main.Main;

/**
 * The <code>MapElement</code> class was intended
 * to make the <code>MapLoader</code> class more
 * readable and reduce messiness of the map loading.
 * However, it never really got implemented so this
 * class is not very useful.
 * 
 * @author Ethan Vrhel
 * @see MapLoader
 */
public abstract class MapElement 
{
	/**
	 * The <code>MapElement</code> successfully loaded
	 */
	public static final int SUCCESS = 0;
	
	/**
	 * The <code>MapElement</code> failed to load
	 */
	public static final int FAIL = 1;
	
	private String name;
	protected int prefX;
	protected int prefY;
	
	public MapElement(String name)
	{
		this.name = name;
		if (Main.getMapElementHandler() != null)
			Main.getMapElementHandler().add(this);
		else
			Main.println("Failed to add MapElement: " + name, Color.RED);
	}
	
	/**
	 * Gets the name of the <code>MapElement</code>
	 * 
	 * @return The name
	 */
	public final String getName()
	{
		return name;
	}
	
	/**
	 * Sets the offset
	 * 
	 * @param x The x position
	 * @param y The y position
	 */
	public final void setOffset(int x, int y)
	{
		
	}
	
	/**
	 * Loads the <code>MapElement</code>
	 * 
	 * @param args The arguments of the <code>MapElement</code>
	 * @return The return state (specified in <code>MapElement</code>)
	 */
	protected abstract int loadMe(String[] args);
}
