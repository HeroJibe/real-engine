package core;

import java.awt.Color;

import main.Main;

public class AnimationHandler 
{
	private Animation[] anims;
	private int maxAnims;
	private int numAnims;
	private int animationsRunning;
	
	public AnimationHandler(int max)
	{
		maxAnims = max;
		anims = new Animation[max];
		numAnims = 0;
		animationsRunning = 0;;
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
