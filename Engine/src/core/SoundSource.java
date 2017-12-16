package core;

import main.Main;

public class SoundSource
{
	private double x;
	private double y;
	
	public SoundSource()
	{
		x = 0;
		y = 0;
	}
	
	public SoundSource(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setX(double x)
	{
		this.x = x;
	}
	
	public void setY(double y)
	{
		this.y = y;
	}
	
	public void setLocation(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public float getDistanceToPlayer()
	{
		return (float) Math.sqrt(Math.pow(Main.getPlayer().getPlayerEntity().getActualX() - x, 2) + 
				Math.pow(Main.getPlayer().getPlayerEntity().getActualY() - y, 2));
	}
	
	public String toString()
	{
		return "[SoundSource]: " + Math.round(x) + ", " + Math.round(y);
	}
}
