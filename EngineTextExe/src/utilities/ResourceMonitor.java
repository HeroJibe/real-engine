package utilities;

public class ResourceMonitor 
	implements Runnable
{
	private int renderingVal = 0;
	private int precRender = 0;
	
	private int animationVal = 0;
	private int precAnimation = 0;
	
	private int total = 0;
	
	public ResourceMonitor() { }
	
	public void run()
	{
		while (true)
		{
			total = renderingVal + animationVal;
			precRender = (renderingVal / total) * 100;
			precAnimation = (animationVal / total) * 100;
		}
	}
	
	public int getRenderPrec()
	{
		return precRender;
	}
	
	public int getAnimationPrec()
	{
		return precAnimation;
	}
}
