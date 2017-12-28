package game;

public class Chapter 
{
	private String name;
	private String map;
	
	public Chapter(String name, String map)
	{
		this.name = name;
		this.map = map;
	}
	
	/**
	 * Returns the name of the <code>Chapter</code>
	 * 
	 * @return The name
	 */
	public final String getName()
	{
		return name;
	}
	
	/**
	 * Returns the associated map name of the <code>Chapter</code>
	 * @return
	 */
	public final String getMap()
	{
		return map;
	}
}
