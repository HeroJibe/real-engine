package core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Main;

/**
 * The <code>Decal</code> class applies a
 * <code>BufferedImage</code> to an <code>Entity</code>
 * 
 * @author Ethan Vrhel
 * @see DecalHandler
 * @see Entity
 */
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
	
	/**
	 * Applies the <code>Decal</code>
	 * 
	 * @return The return state
	 */
	public int update()
	{
		try
		{
			if (entityFace == null)
			{
				return APPLY_FAIL;
			}
			
			entityFace.getName();
			if (entityFace.getAnimation() == null && entityFace.getBufferedImage() != null)
			{
				Graphics g = entityFace.getBufferedImage().getGraphics();
				g.drawImage(img, offX, offY, null);
				return APPLY_SUCCESS;
			}	

			return APPLY_FAIL;
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			return APPLY_FAIL;
		}
	}
	
	/**
	 * Applies the <code>Decal</code> to an external <code>Entity</code>
	 * 
	 * @param e The <code>Entity</code>
	 * @return The return state
	 */
	public int update(Entity e)
	{
		try
		{
			if (e == null)
			{
				return APPLY_FAIL;
			}
			
			if (e.getAnimation() == null && entityFace.getBufferedImage() != null)
			{
				Graphics g = e.getBufferedImage().getGraphics();
				g.drawImage(img, offX, offY, null);
				return APPLY_SUCCESS;
			}	
		
			return APPLY_FAIL;
		}
		catch (Exception e1)
		{
			return APPLY_FAIL;
		}
	}
}
