package utilities;

import java.io.PrintStream;
import gui.GameWindow;
import main.Main;

public class ResourceMonitor 
{
	private static int currMonitors = 0;
	
	public static enum Type
	{
		RENDER, POST_PROCESS, SOUND, OTHER
	}
	
	private long startTime;
	
	private long renderTime;
	private long pProcessTime;
	private long soundTime;
	private long otherTime;
	
	private int avgFps;
	private int fpsSum;
	
	public ResourceMonitor()
	{
		renderTime = 0;
		pProcessTime = 0;
		soundTime = 0;
		avgFps = 0;
		fpsSum = 0;
		startTime = System.currentTimeMillis();
		currMonitors++;
	}
	
	public void increment(Type type, long val)
	{
		if (type == Type.RENDER)
			renderTime += val;
		else if (type == Type.POST_PROCESS)
			pProcessTime += val;
		else if (type == Type.SOUND)
			soundTime += val;
		else
			otherTime += val;
	}
	
	public double getPrecent(Type type)
	{
		long tot = renderTime + pProcessTime + soundTime + otherTime;
		if (tot == 0)
			return 0;
		if (type == Type.RENDER)
		{
			double prec1 = ((double) renderTime / tot) * 10000.0;
			return Math.round(prec1) / 100.0;
		}
		else if (type == Type.POST_PROCESS)
		{
			double prec1 = ((double) pProcessTime / tot) * 10000.0;
			return Math.round(prec1) / 100.0;
		}
		else if (type == Type.SOUND)
		{
			double prec1 = ((double) soundTime / tot) * 10000.0;
			return Math.round(prec1) / 100.0;
		}
		else if (type == Type.OTHER)
		{
			double prec1 = ((double) otherTime / tot) * 10000.0;
			return Math.round(prec1) / 100.0;
		}
		else
			return 0.0;
	}
	
	public void addToAverage(int fps)
	{
		avgFps += fps;
		fpsSum++;
	}
	
	public void print(PrintStream out)
	{
		out.println("____________________________________");
		out.println("Resource Monitor " + currMonitors);
		long end = System.currentTimeMillis();
		out.println("monitored for: " + ((end - startTime) / 1000) + " seconds");
		out.println("resolution: " + GameWindow.XRES_GL + "x" + GameWindow.YRES_GL);
		if (fpsSum > 0)
			out.println("average fps: " + (avgFps / fpsSum));
		else
			out.println("average fps: 0");	
		
		double skipped = (Main.getEntityHandler().getSkippedFrames() * 1.0) / Main.getEntityHandler().getCalledFrames();
		double prec = Math.round(skipped * 1000.0);
		out.println("skipped frames: " + (prec / 10.0) + "% (" + Main.getEntityHandler().getSkippedFrames() + 
				"/" + Main.getEntityHandler().getCalledFrames() + ")");
		out.println("____________________________________");
		out.println("rendering: " + getPrecent(Type.RENDER) + "%");
		out.println("post-processing: " + getPrecent(Type.POST_PROCESS) + "%");
		out.println("sound: " + getPrecent(Type.SOUND) + "%");
		out.println("other: " + getPrecent(Type.OTHER) + "%");	
		out.println("____________________________________");
	}
}
