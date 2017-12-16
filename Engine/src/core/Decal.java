package core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Main;

public class Decal 
{
	public static final int APPLY_SUCCESS = 0;
	public static final int APPLY_FAIL = 1;
	
	private BufferedImage img;
	private Entity entityFace;
	private int offX;
	private int offY;
	
	public Decal(BufferedImage img, String entityFace, int offX, int offY)
	{
		this.img = img;
		this.entityFace = Main.getEntityHandler().getEntityByName(entityFace);
		this.offX = offX;
		this.offY = offY;
	}
	
	public int update()
	{
		System.out.println("updating bound entity...");
		try
		{
			if (entityFace == null)
			{
				System.out.println("failed (entity is null)");
				return APPLY_FAIL;
			}
			
			entityFace.getName();
			if (entityFace.getAnimation() == null && entityFace.getBufferedImage() != null)
			{
				Graphics g = entityFace.getBufferedImage().getGraphics();
				g.drawImage(img, offX, offY, null);
				System.out.println("success");
				return APPLY_SUCCESS;
			}	
			if (entityFace.getBufferedImage() == null)
				System.out.println("failed (entity is missing an image)");
			else
				System.out.println("failed (entity uses an animation)");
			return APPLY_FAIL;
		}
		catch (Exception e)
		{
			System.out.println("failed (unhandled exception)");
			e.printStackTrace(System.out);
			return APPLY_FAIL;
		}
	}
	
	public int update(Entity e)
	{
		System.out.println("updating external entity...");
		
		try
		{
			if (e == null)
			{
				System.out.println("failed (entity is null)");
				return APPLY_FAIL;
			}
			
			if (e.getAnimation() == null && entityFace.getBufferedImage() != null)
			{
				Graphics g = e.getBufferedImage().getGraphics();
				g.drawImage(img, offX, offY, null);
				System.out.println("success");
				return APPLY_SUCCESS;
			}	
			if (entityFace.getBufferedImage() == null)
				System.out.println("failed (entity is missing an image)");
			else
				System.out.println("failed (entity uses an animation)");		
			return APPLY_FAIL;
		}
		catch (Exception e1)
		{
			System.out.println("failed (unhandled exception)");
			return APPLY_FAIL;
		}
	}
}
