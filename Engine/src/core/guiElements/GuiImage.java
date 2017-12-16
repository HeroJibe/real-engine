package core.guiElements;

import java.io.File;

import core.InputListener;

public class GuiImage 
	extends GuiElement
{

	public GuiImage(String name, int type, File imageFile, double x, double y, int z, int w, int h) 
	{
		super(name, type, imageFile, x, y, z, w, h);
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
