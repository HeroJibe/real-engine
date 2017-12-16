package utilities;

import main.Main;

public class FramesCounter 
{
	private int currentFPS = 0;
	private int FPS = 0;
	private int[] avgs = new int[5];
	private int average = 0;
	private int count = 0;
	private int sv = 0;
	long start = 0;
	
	public FramesCounter()
	{
		average = (int) (Main.renderRefreshBias / Main.RENDER_REFRESH_RATE);
	}
	
	public void tick()
	{		
		currentFPS++;
		if (System.nanoTime() - start >= 1000000000)
		{
			count++;
			if (count >= 5)
			{
				count = 0;
				int sum = 0;
				for (int i = 0; i < avgs.length; i++)
				{
					sum += avgs[i];
				}
				average = sum / 5;
			}
			FPS = currentFPS;
			currentFPS = 0;
			avgs[count] = FPS;
			sv = FPS - average;
			start = System.nanoTime();
		}
	}
	
	public int getFps()
	{
		return FPS;
	}
	
	public int getAverage()
	{
		return average;
	}
	
	public int getSv()
	{
		return sv;
	}
}
