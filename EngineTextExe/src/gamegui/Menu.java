package gamegui;

import java.awt.Color;

import core.guiElements.GuiButton;
import main.Main;
import utilities.KeyBinds;

public class Menu 
	implements Runnable
{
	private GuiButton buttons[];
	private boolean enabled;
	private boolean visible;
	private boolean needUpdate = true;
	private boolean check = true;
	private boolean last = false;
	private boolean shouldToggle = false;
	
	public Menu(GuiButton[] buttons, boolean enabled)
	{
		this.buttons = buttons;
		this.enabled = enabled;
		visible = false;
	}
	
	public void run()
	{		
		while (true)
		{
			if (enabled && visible)
			{
				for (int i = 0; i < buttons.length; i++)
				{
					buttons[i].setVisible(true);
				}
			}
			else
			{
				for (int i = 0; i < buttons.length; i++)
				{
					buttons[i].setVisible(false);
				}
			}
			
			needUpdate = false;
			
			while (! Main.getInputListener().isKeyPressed(KeyBinds.PAUSE_MENU) && ! shouldToggle) {System.out.print("");}
						
				check = false;
				needUpdate = true;
				
				visible = ! visible;
				Main.gamePaused = ! Main.gamePaused;

			while (Main.getInputListener().isKeyPressed(KeyBinds.PAUSE_MENU) && ! shouldToggle) {System.out.print("");}
			
			shouldToggle = false;
		}
	}
	
	public void triggerToggle()
	{
		shouldToggle = true;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public boolean isVisible()
	{
		return visible;
	}
}
