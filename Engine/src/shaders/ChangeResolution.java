package shaders;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import core.Shader;
import gui.GameWindow;

public class ChangeResolution 
	extends Shader
{
	private double xMult;
	private double yMult;
	private int scaleType;
	
	public ChangeResolution(String name, double xMult, double yMult, int scaleType) 
	{
		super(name);
		this.xMult = xMult;
		this.yMult = yMult;
		this.scaleType = scaleType;
	}

	public BufferedImage shade(BufferedImage buf) 
	{
		Image nr = buf.getScaledInstance((int) (GameWindow.XRES_GL * xMult), (int) (GameWindow.YRES_GL * yMult), scaleType);
		BufferedImage nbi = new BufferedImage(nr.getWidth(null), nr.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = nbi.getGraphics();
		g.drawImage(nr, 0, 0, null);
		return nbi;
	}

	public BufferedImage unload(BufferedImage buf) 
	{
		return null;
	}

	public double getXMult()
	{
		return xMult;
	}

	public void setXMult(double xMult)
	{
		this.xMult = xMult;
	}
	
	public double getYMult()
	{
		return yMult;
	}
	
	public void setYMult(double yMult)
	{
		this.yMult = yMult;
	}
	
	public int getScaleType()
	{
		return scaleType;
	}
	
	public void setScaleType(int scaleType)
	{
		this.scaleType = scaleType;
	}
}
