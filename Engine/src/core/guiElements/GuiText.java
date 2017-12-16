package core.guiElements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;

import core.InputListener;
import utilities.RichText;

public class GuiText 
	extends GuiElement
{		
	private RichText content;
	
	public GuiText(String name, int type, double x, double y, int z, int w, int h) 
	{
		super(name, type, new File("resources\\textures\\debug\\notexture.png"), x, y, z, w, h);
	}

	void onAction(GuiAction a) 
	{
		
	}

	void onAction(GuiAction a, InputListener inp) 
	{
		
	}

	void reset() 
	{
		content.setText("");
		content.setSize(1F);
		content.setColor(Color.BLACK);
	}
	
	public void setImage(Image img) { }

	public void setText(String text)
	{
		content.setText(text);;
	}
	
	public String getText()
	{
		return content.getText();
	}
	
	public void setFont(Font font)
	{
		content.setFont(font);
	}
	
	public Font getFont()
	{
		return content.getFont();
	}
	
	public void setSize(float size)
	{
		content.setSize(size);
	}
	
	public float getSize()
	{
		return content.getSize();
	}
	
	public void setColor(Color c)
	{
		content.setColor(c);
	}
	
	public Color getColor()
	{
		return content.getColor();
	}
}
