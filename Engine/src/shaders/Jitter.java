package shaders;

import java.awt.image.BufferedImage;
import java.util.Random;

import core.Shader;
import main.Main;

public class Jitter 
	extends Shader
{
	private int mX;
	private int mY;
	private Random r;
	
	public Jitter(String name, int mX, int mY) 
	{
		super(name);
		this.mX = mX;
		this.mY = mY;
		r = new Random();
	}

	public BufferedImage shade(BufferedImage buf) 
	{
		loaded = true;
		int mX = 0;
		if (this.mX != 0)
			mX = r.nextInt(this.mX) * 2 - this.mX;
		int mY = 0;
		if (this.mY != 0)
			mY = r.nextInt(this.mY) * 2 - this.mY;
		Main.getGameWindow().dispOffsetX = mX;
		Main.getGameWindow().dispOffsetY = mY;
		return buf;
	}

	public int getMX()
	{
		return mX;
	}
	
	public int getMY()
	{
		return mY;
	}
	
	public void setMX(int mX)
	{
		this.mX = mX;
	}
	
	public void setMY(int mY)
	{
		this.mY = mY;
	}

	public BufferedImage unload(BufferedImage buf)
	{
		loaded = false;
		Main.getGameWindow().dispOffsetX = 0;
		Main.getGameWindow().dispOffsetY = 0;
		return buf;
	}
}
