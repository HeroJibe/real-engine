package core.MapElements;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import core.Entity;
import core.MapElement;
import main.Main;

public class Brush 
	extends MapElement
{

	public Brush() 
	{
		super("Brush");
	}

	@Override
	protected int loadMe(String[] args) 
	{		
		int type = -1;
		String texture;
		String name = "Brush Face";
		double x;
		double y;
		int width;
		int height;
		int z;
		boolean solid;
		boolean renderInReflections;
		int mass = 1;
		boolean kinetic = true;
	
	
		try
		{
			if (args[0].equals("STATIC"))
			{
				type = Entity.STATIC;
			} 
			else if (args[0].equals("DYNAMIC"))
			{
				type = Entity.DYNAMIC;
			}
		
			texture = args[1];
		
			x = Double.parseDouble(args[2] + prefX);
			y = Double.parseDouble(args[3] + prefY);
			z = Integer.parseInt(args[4]);
			width = Integer.parseInt(args[5]);
			height = Integer.parseInt(args[6]);
			solid = Boolean.parseBoolean(args[7]);
			if (type == Entity.DYNAMIC)
			{
				mass = Integer.parseInt(args[8]);
				kinetic = Boolean.parseBoolean(args[9]);
				name = args[10];
			}
			else
			{
				try
				{
					name = args[8];
				}
				catch (Exception e) {}
			}
		
			//(Entity.DYNAMIC, resourceHandler.getImage(resourceHandler.getIndexByName("player.png")), true, "Player Entity", 640, 360, 10, 64, 64, 100, false);
			Entity se = new Entity(type, Main.getResourceHandler().getByName(texture), width, height,
					solid, name, x, y, z, width, height, 100, false);
			se.setKinetic(kinetic);
			
			se.setMass(mass);
			se.setImageFile(Main.getResourceHandler().getByName(texture));
			
			try
			{
				BufferedImage buffImg = ImageIO.read(Main.getResourceHandler().getByName(texture));
				if (buffImg.getColorModel().hasAlpha())
					se.setTransparancy(true);
				else
					se.setTransparancy(false);
			}
			catch (Exception e)
			{
				se.setTransparancy(true);
			}
	
			if (se.isStatic())
				Main.getEntityHandler().addStaticEntity(se);
			else
				Main.getEntityHandler().addDynamicEntity(se);
		}
		catch (Exception e)
		{
			Main.println("Error: " + e, Color.RED);
			return FAIL;
		}
		
		return SUCCESS;
	}

}
