package core.guiElements;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.InputListener;
import main.Main;

public class GuiButton extends GuiElement
{
	private boolean clicked = false;
	private boolean hovered = false;
	
	public GuiButton(String name, int type, File imageFile, double x, double y, int z, int w, int h) 
	{
		super(name, type, imageFile, x, y, z, w, h);
	}

	void onAction(GuiAction a) 
	{
		if (a.equals(GuiAction.MOUSE_CLICK) && hovered)
		{
			clicked = true;
		}
		else if (a.equals(GuiAction.MOUSE_EXITED))
		{
			hovered = false;
			clicked = false;
		}
		else if (a.equals(GuiAction.MOUSE_HOVER))
		{
			hovered = true;
			clicked = false;
		}
		else
		{
			clicked = false;
			hovered = false;
		}
	}

	void onAction(GuiAction a, InputListener inp) 
	{
		
	}
	
	void reset()
	{
		clicked = false;
		hovered = false;
	}
	
	public void resetClicked()
	{
		clicked = false;
	}
	
	public boolean isClicked()
	{
		return clicked;
	}
	
	public boolean isHovered()
	{
		return hovered;
	}
}
