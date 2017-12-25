package core;

import main.Main;

public abstract class MapElement
{
	private String name;
	private String editorName;
	private String[] args;
	private String[] argsName;
	
	/**
	 * Creates a prototype of the element
	 * 
	 * @param name The name of the map element
	 * @param args The names of the arguments of the map element
	 */
	public MapElement(String name, String[] args)
	{
		this.name = name;
		this.args = getDefaults();
		argsName = args;
		Main.getMapElementHandler().add(this);
	}
	
	/**
	 * Creates an element that can be placed in a map
	 * 
	 * @param elem The base element
	 * @param editorName The friendly name of the element
	 */
	public MapElement(MapElement elem, String editorName)
		throws IllegalArgumentException
	{
		if (Main.getMapElementHandler().getElement(elem.getName()) == null)
			throw new IllegalArgumentException("Cannot create map element!");
		this.name = elem.getName();
		this.args = elem.getDefaults();
		argsName = elem.getValues();
		this.editorName = editorName;
	}
	
	public final String getName()
	{
		return name;
	}
	
	public final String getEditorName()
	{
		return editorName;
	}
	
	public final void setEditorName(String editorName)
	{
		this.editorName = editorName;
	}
	
	public final void editValue(String name, String newValue)
	{
		if (name == null)
			return;
		
		for (int i = 0; i < args.length; i++)
		{
			if (argsName[i].equalsIgnoreCase(name))
			{
				args[i] = newValue;
				return;
			}
		}
	}
	
	public final void editValue(int index, String newValue)
	{
		args[index] = newValue;
	}
	
	public final String getValue(String name)
	{
		if (name == null)
			return null;
		
		for (int i = 0; i < args.length; i++)
		{
			if (argsName[i].equalsIgnoreCase(name))
			{
				return args[i];
			}
		}
		return null;
	}
	
	public final String[] getValues()
	{
		return argsName;
	}
	
	public final void clearValues()
	{
		args = getDefaults();
	}
	
	public final int getNumValues()
	{
		return args.length;
	}
	
	@Override
	public String toString()
	{
		return "MapElement: " + name;
	}
	
	public abstract String[] getDefaults();
	public abstract String export();
	public abstract void load(String elem) throws IllegalArgumentException;
}
