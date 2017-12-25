package utilities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TextBox 
{
	/**
	 * Creates a text box
	 * @param text The text in the box
	 * @param c The color of the box
	 * @param w The width of the box
	 * @param h The height of the box
	 * @param f The format of the box
	 * @param c2 The fill/outline of the box
	 */
	public static BufferedImage create(String text, Color c, int w, int h, TextBoxFormat f, Color c2)
	{
		BufferedImage buff = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buff.getGraphics();
		
		if (f == TextBoxFormat.FILL)
		{
			g.setColor(c2);
			g.fillRect(0, 0, w, h);
		}
		else if (f == TextBoxFormat.OUTLINE)
		{
			g.setColor(c2);
			g.fillRect(0, h, w, h);
		}
		
		g.setColor(c);
		g.drawString(text, 0, 0);
		
		return buff;
	}
}
