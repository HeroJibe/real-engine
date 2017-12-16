package core;

import java.awt.Color;

import main.Main;

public class AnimationHandler 
	implements Runnable
{
	private Animation[] anims;
	private int maxAnims;
	private int numAnims;
	private int animationsRunning;
	private long tick = 0;
	
	public AnimationHandler(int max)
	{
		maxAnims = max;
		anims = new Animation[max];
		numAnims = 0;
		animationsRunning = 0;;
	}
	
	public void run()
	{
		while (true)
		{
			for (int i = 0; i < anims.length; i++)
			{
				if (anims[i] != null)
				{
					if (anims[i].isAnimating())
					{
						if (anims[i].getEntity() == null)
						{
							Main.println("animation destroyed.");
							anims[i] = null;
						}
						else
						{
							anims[i].tick();
							if (anims[i].getTick() > anims[i].getRate())
							{
								anims[i].setTick(0);
								if (anims[i].getFrame() == anims[i].getNumFrames() - 1 && anims[i].limitingAnimation()) {}
								else
								{
									if (anims[i].getEntity() != null)
									{
										Animation.animate(anims[i].getEntity(), anims[i]);
									}
								}
							}
						}
					}
				}
			}			
			
			try
			{
				Thread.sleep(1);
			}
			catch (Exception e) {}
		}
	}
	
	public void addAnimation(Animation anim)
	{
		for (int i = 0; i < maxAnims; i++)
		{
			if (anims[i] == null)
			{
				if (anims[i] == anim)
				{
					Main.println("WARNING! Adding duplicate animation", Color.YELLOW);
				}
				anims[i] = anim;
				numAnims++;
				return;
			}
		}
		Main.println("Failed to add animation. Animation cache is not big enough.", Color.RED);
	}
	
	public Animation getByName(String name)
	{
		for (int i = 0; i < maxAnims; i++)
		{
			if (anims[i] != null)
			{
				if (anims[i].getName().equals(name))
				{
					return anims[i];
				}
			}
		}
		
		return null;
	}
	
	public void incrementRunning()
	{
		animationsRunning++;
	}
	
	public void decrementRunning()
	{
		animationsRunning--;
	}
	
	public int getAnimationsRunning()
	{
		return animationsRunning;
	}
	
	public int getNumAnimations()
	{
		return numAnims;
	}
}
