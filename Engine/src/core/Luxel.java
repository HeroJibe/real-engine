package core;

/**
 * The <code>Luxel</code> class was intended for having
 * certain areas lit up, but lighting in
 * the engine has been deprecated.  Along with that,
 * the <code>Luxel</code> class has been replaced in
 * <code>LightHandler</code>
 * 
 * @author Ethan Vrhel
 * @see Light
 * @see LightHandler
 */
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
	
	/**
	 * Gets the x position
	 * @return The x position
	 */
	public double getX()
	{
		return x;
	}
	
	/**
	 * Gets the y position
	 * @return The y position
	 */
	public double getY()
	{
		return y;
	}
	
	/**
	 * Gets the size
	 * @return The size
	 */
	public double getSize()
	{
		return size;
	}
	
	/**
	 * Sets the brightness
	 * @param brightness The brightness
	 */
	public void setBrightness(double brightness)
	{
		this.brightness = brightness;
	}
	
	/**
	 * Returns the brightness
	 * @return The brightness
	 */
	public double getBrightness()
	{
		return brightness;
	}
}
