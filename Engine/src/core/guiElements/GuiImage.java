package core.guiElements;

import java.awt.image.BufferedImage;
import java.io.File;

import core.InputListener;

public class GuiImage 
	extends GuiElement
{
	public GuiImage(String name, int type, File image, double x, double y, int z, int w, int h) 
	{
		super(name, type, image, x, y, z, w, h);
	}

	public GuiImage(String name, int type, BufferedImage image, double x, double y, int z, int w, int h) 
	{
		super(name, type, image, x, y, z, w, h);
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
