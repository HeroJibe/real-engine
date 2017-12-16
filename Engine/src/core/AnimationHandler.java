/**
 * The AnimationHandler class provides refrences to
 * all of the active Animations
 * 
 * @author 	Ethan Vrhel
 */

package core;

import java.awt.Color;

import main.Main;

public class AnimationHandler 
	implements Runnable
{
	/**
	 * Whether the animations should be run
	 */
	public boolean runAnimations = true;
	
	private Animation[] anims;				// The Animations
	private int maxAnims;					// The max Animations
	private int numAnims;					// The number of Animations
	private int animationsRunning;			// The number of Animations running
	
	public AnimationHandler(int max)
	{
		maxAnims = max;
		anims = new Animation[max];
		numAnims = 0;
		animationsRunning = 0;;
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			if (runAnimations)
			{
				for (int i = 0; i < anims.length; i++)
				{
					if (anims[i] != null)
					{
						if (anims[i].isAnimating())
						{
							if (anims[i].getEntity() == null && anims[i].getGuiElement() == null)
							{
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
										else if (anims[i].getGuiElement() != null)
										{
											Animation.animate(anims[i].getGuiElement(), anims[i]);
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
			else
			{
				Thread.yield();
			}
		}
	}
	
	/**
	 * Adds an Animation
	 * 
	 * @param anim
	 */
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
	
	/**
	 * Returns an Animation by its name
	 * 
	 * @param name
	 * @return
	 */
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
	
	/**
	 * Sets all of the Animations to a frame
	 * 
	 * @param frame
	 */
	public void setAllToFrame(int frame)
	{
		for (int i = 0; i < maxAnims; i++)
		{
			if (anims[i] != null)
			{
				anims[i].setFrame(frame);
			}
		}
	}
	
	/**
	 * Increments the amount of Animations running
	 */
	public void incrementRunning()
	{
		animationsRunning++;
	}
	
	/**
	 * Decrements the amount of Animation running
	 */
	public void decrementRunning()
	{
		animationsRunning--;
	}
	
	/**
	 * Returns the amount of Animations running
	 * 
	 * @return
	 */
	public int getAnimationsRunning()
	{
		return animationsRunning;
	}
	
	/**
	 * Returns the number of Animations
	 * 
	 * @return
	 */
	public int getNumAnimations()
	{
		return numAnims;
	}
}
