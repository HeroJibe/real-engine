package utilities;

import java.awt.Color;
import java.awt.Font;

public class RichText 
{
	private String text;
	private Font font;
	private float fontSize;
	private Color textColor;
	
	public RichText(String text, Font font, float size, Color c)
	{
		this.text = text;
		this.font = font;
		fontSize = size;
		textColor = c;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setFont(Font font)
	{
		this.font = font;
	}
	
	public Font getFont()
	{
		return font;
	}
	
	public void setSize(float size)
	{
		this.fontSize = size;
	}
	
	public float getSize()
	{
		return fontSize;
	}
	
	public void setColor(Color c)
	{
		textColor = c;
	}
	
	public Color getColor()
	{
		return textColor;
	}
}
