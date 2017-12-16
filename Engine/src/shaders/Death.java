package shaders;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import core.Shader;
import gui.GameWindow;
import main.Main;

public class Death
	extends Shader
{	
	public Death(String name) 
	{
		super(name);
	}

	public BufferedImage shade(BufferedImage buf)
	{
		BufferedImage nbi = new BufferedImage(buf.getWidth(null), buf.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = nbi.getGraphics();
		g.drawImage(Main.getResourceHandler().getImage(Main.getResourceHandler().getIndexByName("dead.png", true))
				.getScaledInstance(GameWindow.XRES_GL, GameWindow.YRES_GL, Image.SCALE_FAST), 0, 0, null);
		return nbi;
	}

	public BufferedImage unload(BufferedImage buf) 
	{
		return buf;
	}

}
