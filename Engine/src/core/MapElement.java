package core;

import java.awt.Color;

import main.Main;

public abstract class MapElement 
{
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;
	
	protected String name;
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
	
	public String getName()
	{
		return name;
	}
	
	public void setOffset(int x, int y)
	{
		
	}
	
	public int load(String[] args)
	{
		int succ = loadMe(args);
		prefX = 0;
		prefY = 0;
		return succ;
	}
	
	protected abstract int loadMe(String[] args);
}
