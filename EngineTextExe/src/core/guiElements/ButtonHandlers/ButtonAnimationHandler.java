package core.guiElements.ButtonHandlers;

import java.awt.Color;

import core.Animation;
import core.guiElements.GuiButton;
import main.Main;

public class ButtonAnimationHandler 
{
	private GuiButton[] buttons;
	private Animation[] animations;
	private boolean[] wasClicked;
	private boolean[] wasHovered;
	private int maxButtons;
	private int numButtons = 0;
	
	public ButtonAnimationHandler(int max)
	{
		buttons = new GuiButton[max];
		animations = new Animation[max];
		wasClicked = new boolean[max];
		wasHovered = new boolean[max];
		maxButtons = max;
	}
	
	public void update()
	{
		{
			for (int i = 0; i < maxButtons; i++)
			{
				if (buttons[i] != null && animations[i] != null)
				{
					if (buttons[i].isHovered())
					{
						if (buttons[i].isClicked())
						{
							if (animations[i].getNumFrames() == 3)
								animations[i].setFrame(2);
						}
						else
							animations[i].setFrame(1);
					}
					else
						animations[i].setFrame(0);
				}
			}
		}
	}
	
	public void addAnimatedButton(GuiButton button, Animation anim)
	{
		for (int i = 0; i < maxButtons; i++)
		{
			if (buttons[i] == null && animations[i] == null)
			{
				if (anim.getNumFrames() == 3 || anim.getNumFrames() == 2)
				{
					buttons[i] = button;
					animations[i] = anim;
					return;
				}
				else
				{
					Main.println("Failed to add animated button!  Animation must have 2 or 3 frames.  It has " + anim.getNumFrames(), Color.RED);
					return;
				}
			}
		}
		
		Main.println("Failed to add animated button!  Cache is not big enough.", Color.RED);
	}
	
	public boolean isButtonHovered(GuiButton button)
	{
		for (int i = 0; i < maxButtons; i++)
		{
			if (buttons[i] == button)
				return buttons[i].isHovered();
		}
		return false;
	}
	
	public boolean isButtonClicked(GuiButton button)
	{
		for (int i = 0; i < maxButtons; i++)
		{
			if (buttons[i] == button)
				return buttons[i].isClicked();
		}
		return false;
	}
}
