package core;

import java.awt.image.BufferedImage;

public class ShaderHandler 
{
	private int maxShaders;
	private int numShaders;
	private Shader[] shaders;
	
	public ShaderHandler(int max)
	{
		maxShaders = max;
		shaders = new Shader[max];
		numShaders = 0;
	}
	
	public void addShader(Shader shader)
	{
		for (int i = 0; i < shaders.length; i++)
		{
			if (shaders[i] == null)
			{
				shaders[i] = shader;
				numShaders++;
				return;
			}
		}
	}
	
	public BufferedImage cummulativeShade(BufferedImage disp)
	{
		for (int i = 0; i < numShaders; i++)
		{
			if (shaders[i] != null)
			{
				if (shaders[i].isEnabled())
				{
					disp = shaders[i].shade(disp);
				}
				else if (shaders[i].isLoaded())
				{
					disp = shaders[i].unload(disp);
				}
			}
		}
		return disp;
	}
	
	public int getNumEnabled()
	{
		int enabled = 0;
		for (int i = 0; i < numShaders; i++)
		{
			if (shaders[i] != null)
			{
				if (shaders[i].isEnabled())
				{
					enabled++;
				}
			}
		}
		return enabled;
	}
	
	public Shader getByName(String name)
	{
		for (int i = 0; i < numShaders; i++)
		{
			if (shaders[i] != null)
			{
				if (shaders[i].getName().equals(name))
				{
					return shaders[i];
				}
			}
		}
		return null;
	}
	
	public int getNumShaders()
	{
		return numShaders;
	}
	
	public int getMaxShaders()
	{
		return maxShaders;
	}
}
