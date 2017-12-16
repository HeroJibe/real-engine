package core;

import java.awt.image.BufferedImage;

public abstract class Shader 
{
	protected String name;
	protected boolean enabled;
	protected boolean loaded;
	
	public Shader(String name)
	{ 
		this.name = name;
		enabled = false;
		loaded = false;
	}
	
	public void enabled(boolean enabled)
	{
		System.out.print("");
		this.enabled = enabled;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isLoaded()
	{
		return loaded;
	}
	
	public abstract BufferedImage shade(BufferedImage buf);
	public abstract BufferedImage unload(BufferedImage buf);
}
