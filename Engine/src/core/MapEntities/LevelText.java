package core.MapEntities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import core.Entity;
import main.Main;

public class LevelText
	extends MapEntity
{	
	public static final int WAIT = 3000;
	
	private String chapterTitle;
	
	public LevelText(int type, String name, String chapterTitle)
	{
		super(name, type, 900, 1000, chapterTitle.length() * 5, 20);
		this.chapterTitle = chapterTitle;
	}

	public void function()
	{
		int length = chapterTitle.length() * 100;
		int height = 40;
		BufferedImage buf = new BufferedImage(length, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buf.getGraphics();
		g.setColor(Color.BLACK);
		Font currentFont = g.getFont();
	    Font newFont = currentFont.deriveFont(currentFont.getSize() * 4F);
	    g.setFont(newFont);
		g.drawString(chapterTitle, 0, 40);
		Main.println("drew " + chapterTitle);
		Entity e = new Entity(Entity.STATIC, buf, 
				length, height, false, 900, 1000, 1000, 100, false);
		Main.getEntityHandler().addStaticEntity(e);
		try 
		{
			Thread.sleep(WAIT);
		}
		catch (InterruptedException e1) 
		{
			e1.printStackTrace();
		}
		Main.getEntityHandler().removeEntity(e);
	}

}
