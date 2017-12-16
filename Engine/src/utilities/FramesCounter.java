package utilities;

import main.Main;

public class FramesCounter 
{
	private int currentFPS = 0;
	private int FPS = 0;
	private int average = 0;
	private long sum = 0;
	private long count = 0;
	public int low = Integer.MAX_VALUE;
	public int high = 0;
	private int sv = 0;
	long start = 0;
	
	public FramesCounter()
	{
		average = 0;
	}
	
	public void tick()
	{		
		currentFPS++;
		if (System.nanoTime() - start >= 1000000000 / 2)
		{
			count++;
			FPS = currentFPS * 2;
			currentFPS = 0;
			sum += FPS;
			average = (int) (sum / count);
			if (Main.getCurrentResourceMonitor() != null)
				Main.getCurrentResourceMonitor().addToAverage(FPS);
			sv = FPS - average;
			start = System.nanoTime();
		}
		
		if (FPS < low)
			low = FPS;
		
		if (FPS > high)
			high = FPS;
		
		if (Main.getDebugWindow() != null)
		{
			Main.getDebugWindow().gameMonitor.fps.setText(FPS + "");
			Main.getDebugWindow().gameMonitor.threads.setText(Main.getThreadsRunning() + "");
			Main.getDebugWindow().gameMonitor.mem.setText( Math.round((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / (double) Runtime.getRuntime().maxMemory() * 100) + "%");
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
