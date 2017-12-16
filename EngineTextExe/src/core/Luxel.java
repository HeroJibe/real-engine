package core;

public class Luxel 
{
	private double x;
	private double y;
	private int size;
	private double brightness;
	
	public Luxel(double x, double y, int size)
	{
		this.x = x;
		this.y = y;
		this.size = size;
		brightness = 0;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getSize()
	{
		return size;
	}
	
	public void setBrightness(double brightness)
	{
		this.brightness = brightness;
	}
	
	public double getBrightness()
	{
		return brightness;
	}
}
