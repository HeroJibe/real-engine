package core.guiElements;

import java.io.File;

import core.Animation;
import core.InputListener;
import main.Main;

public class GuiAnimatedImage 
	extends GuiElement
	implements Runnable
{
	private Animation anim;
	private boolean running = false;
	
	public GuiAnimatedImage(String name, int type, File imageFile, Animation anim, double x, double y, int z, int w, int h) 
	{
		super(name, type, imageFile, x, y, z, w, h);
		this.anim = anim;
	}
	
	public void run()
	{
		running = true;
		Main.println("" + anim.getRate());
		while (running)
		{
			image = anim.nextFrame();
			try
			{
				Thread.sleep(anim.getRate());
			}
			catch (Exception e)
			{
				running = false;
			}
		}
	}

	void onAction(GuiAction a) 
	{
		
	}

	void onAction(GuiAction a, InputListener inp) 
	{
		
	}

	void reset() 
	{
		
	}

}
