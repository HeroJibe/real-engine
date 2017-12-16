package utilities;

import core.RenderLoop;
import gui.GameWindow;
import main.Main;

public class RenderMonitor 
	implements Runnable
{
	public static final int R_UPDATE_FACTOR = 8;
	public static final double R_STABLIZE_FACTOR = 1.2;
	public static final int FPS_THRESHOLD = 60;
	GameWindow window;
	
	public long rUpdatesPerSecond = 0;
	public int avgRUpdatesPerSecond = 0;
	private int count = 0;
	private int[] store = new int[R_UPDATE_FACTOR];
	public long high = 0;
	public long low = 0;
	private long highpriv = 0;
	private long lowpriv = 0;
	
	public RenderMonitor(GameWindow window)
	{
		this.window = window;
	}
	
	public void run()
	{
		while (true)
		{
			rUpdatesPerSecond = window.renderUpdates * R_UPDATE_FACTOR;
			store[count] = (int) rUpdatesPerSecond;
			window.renderUpdates = 0;
			
			if (rUpdatesPerSecond > highpriv)
				highpriv = rUpdatesPerSecond;
			else if (rUpdatesPerSecond < lowpriv)
				lowpriv = rUpdatesPerSecond;
			
			try 
			{
				Thread.sleep(1000 / R_UPDATE_FACTOR);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
			count++;
			
			if (count >= R_UPDATE_FACTOR)
			{
				count = 0;
				int sum = 0;
				for (int i = 0; i < R_UPDATE_FACTOR; i++)
				{
					sum += store[i];
				}
				avgRUpdatesPerSecond = sum / R_UPDATE_FACTOR;
				
				high = highpriv;
				low = lowpriv;
				
				highpriv = rUpdatesPerSecond;
				lowpriv = rUpdatesPerSecond;
				
				int tempSV = Math.abs(RenderLoop.getSV());
				if (Main.stablizeFPS)
				{
					if (tempSV > 15)
						Main.renderRefreshBias *= R_STABLIZE_FACTOR;
					else if (tempSV > 5 && Main.getFpsCounter().getFps() >= FPS_THRESHOLD)
						Main.renderRefreshBias += 0;
					else if (Main.renderRefreshBias / R_STABLIZE_FACTOR > 0)
					{
						Main.renderRefreshBias /= R_STABLIZE_FACTOR;
					}
				}
			}
		}
	}
}