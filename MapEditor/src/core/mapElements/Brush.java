package core.mapElements;

import core.MapElement;
import main.Main;

public class Brush 
	extends MapElement
{
	public static final String[] args = {"type", "texture", "x", "y", "z", 
			"width", "height", "solid", "mass", "kinetic", "name"};
	private static final String name = "Brush";
	
	public Brush()
	{
		super(name, args);
	}
	
	public Brush(String name)
	{
		super(Main.getMapElementHandler().getElement(Brush.name), name);
	}

	@Override
	public String[] getDefaults() 
	{
		String[] temp = {"STATIC", "defaultTexture.png", "0", "0", "0", 
				"1", "1", "true", "1", "false", "brush"};
		return temp;
	}
	
	@Override
	public String export()
	{
		String ret =
				"Brush = " + getValue("type") + " " + getValue("texture") + " " +
				getValue("x") + " " + getValue("y") + " " + getValue("z") + " " +
				getValue("width") + " " + getValue("height") + " "  + getValue("solid") +
				" " + getValue("mass") + " " + getValue("kinetic") + " " + getValue("name");
		return ret;
	}
	
	@Override
	public void load(String elem)
		throws IllegalArgumentException
	{
		if (elem == null)
			throw new IllegalArgumentException("No arguments!");
		
		String[] sub = elem.split(" = ");
		if (sub.length != 2)
			throw new IllegalArgumentException("Invalid arguments!");
		
		if (! sub[0].equals(getName()))
		{
			throw new IllegalArgumentException("Mismatched types!");
		}
		
		String[] tokens = sub[1].split(" ");
		
		for (int i = 0; i < getNumValues(); i++)
		{
			editValue(i, tokens[i]);
		}
	}
}
