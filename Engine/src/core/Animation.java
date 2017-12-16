/**
 * The Animation class provides tools for animating
 * entities and gui elements.
 * 
 * @author	Ethan Vrhel
 */

package core;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import core.guiElements.GuiElement;
import main.Main;
import utilities.AdvancedFilters;

public class Animation
{
	private String name;					// The name of the Animation
	private BufferedImage[] images;			// The frames of the Animation
	private int frame = 0;					// The current frame of the Animation
	private int rate;						// The rate of the Animation
	private boolean runAnimation;			// Whether the Animation should run
	private boolean limitAnimation;			// Whether the Animation should stop of its last frame
	private boolean flipHorz;				// Whether the Animation should flip horizontally
	private boolean flipVert;				// Whether the Animation should flip vertically
	private long tick = 0;					// The current Animation tick
	
	private Entity entity;							// The tied Entity
	private GuiElement element;						// The tied GUI element
	
	public Animation(String name, Entity entity, BufferedImage[] images, int rate)
	{
		this.name = name;
		this.images = images;
		this.rate = rate;
		runAnimation = false;
		this.entity = entity;
	}
	
	public Animation(String name, GuiElement element, BufferedImage[] images, int rate)
	{
		this.name = name;
		this.images = images;
		this.rate = rate;
		runAnimation = false;
		this.element = element;
	}
	
	public Animation(String name, BufferedImage[] images, int rate)
	{
		this.name = name;
		this.images = images;
		this.rate = rate;
		runAnimation = false;
	}
	
	public Animation(Animation old)
	{
		name = old.getName() + " copy";
		images = old.getImages();
		rate = old.getRate();
		entity = old.getEntity();
		element = old.getGuiElement();
	}
	
	/**
	 * Returns the next frame of the Animation
	 * as a BufferedImage
	 * @return
	 */
	public BufferedImage nextFrame()
	{
		frame++;
		if (frame >= images.length)
		{
			if (! limitAnimation)
				frame = 0;
			else
				frame = images.length - 1;
		}
		
		BufferedImage img = images[frame];
		
		return img;
	}
	
	/**
	 * Returns the next frame of the Animation
	 * as a plain Image
	 * @return
	 */
	public Image nextFrameImage()
	{
		frame++;
		if (frame >= images.length)
		{
			if (! limitAnimation)
				frame = 0;
			else
				frame = images.length - 1;
		}
		
		Image img = images[frame];
		
		return img;
	}
	
	/**
	 * Returns the BufferedImage at a frame
	 * @param frame
	 * @return
	 */
	public BufferedImage frame(int frame)
	{
		if (frame >= images.length || frame < 0)
		{
			Main.println("Error: animation frame outside of animation bounds", Color.RED);
			return null;
		}
		else
			return images[frame];
	}
	
	/**
	 * Returns the current frame of the Animation
	 * @return
	 */
	public Image currFrame()
	{
		return images[frame];
	}
	
	/**
	 * Sets the Animation's frame
	 * @param frame
	 */
	public void setFrame(int frame)
	{
		if (frame > images.length || frame < 0)
			Main.println("Error: animation frame outside of animation bounds", Color.RED);
		else
			this.frame = frame;
	}
	
	/**
	 * Updates the tied Entity with the Animation
	 * @param e
	 */
	public void update(Entity e)
	{
		e.setImage(images[frame]);
	}
	
	/**
	 * Updates the tied GuiElement with the Animation
	 * @param g
	 */
	public void update(GuiElement g)
	{
		g.setImage(images[frame]);
	}
	
	/**
	 * Returns the tied Entity
	 * @return
	 */
	public Entity getEntity()
	{
		return entity;
	}
	
	/**
	 * Returns the tied GuiElement
	 * @return
	 */
	public GuiElement getGuiElement()
	{
		return element;
	}
	
	/**
	 * Returns the current frame
	 * @return
	 */
	public int getFrame()
	{
		return frame;
	}
	
	/**
	 * Returns the rate of the Animation
	 * @return
	 */
	public int getRate()
	{
		return rate;
	}
	
	/**
	 * Returns the number of frames in the Animation
	 * @return
	 */
	public int getNumFrames()
	{
		return images.length;
	}
	
	/**
	 * Sets the Animation's rate
	 * @param rate
	 */
	public void setRate(int rate)
	{
		this.rate = rate;
	}
	
	/**
	 * Returns whether the Animation is animating or not
	 * @return
	 */
	public boolean isAnimating()
	{
		return runAnimation;
	}
	
	/**
	 * Sets whether the Animation should run or not
	 * @param runAnimation
	 */
	public void shouldRun(boolean runAnimation)
	{
		this.runAnimation = runAnimation;
	}
	
	/**
	 * Returns the Animation's name
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return name + " frame: " + frame;	
	}
	
	/**
	 * Sets the Animation's tied Entity
	 * @param e
	 */
	public void setEntity(Entity e)
	{
		this.element = null;
		this.entity = e;
	}
	
	/**
	 * Sets the Animation tied GuiElement
	 * @param g
	 */
	public void setGuiElement(GuiElement g)
	{
		this.entity = null;
		this.element = g;
	}
	
	/**
	 * Sets whether the Animation should limit itself
	 * @param limitAnimation
	 */
	public void limitAnimation(boolean limitAnimation)
	{
		this.limitAnimation = limitAnimation;
	}
	
	/**
	 * Sets the orientation of the Animation
	 * @param xOr
	 * @param yOr
	 */
	public void setOrientation(boolean xOr, boolean yOr)
	{
		flipHorz = xOr;
		flipVert = yOr;
	}
	
	/**
	 * Returns if the Animation is flipped horizontally
	 * @return
	 */
	public boolean flipHorizontal()
	{
		return flipHorz;
	}
	
	/**
	 * Returns if the Animation is flipped vertically
	 * @return
	 */
	public boolean flipVertical()
	{
		return flipVert;
	}
	
	/**
	 * Updates the images of the Animation
	 */
	public void updateImages()
	{
		AffineTransform tx = AffineTransform.getScaleInstance(1, 1);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		for (int i = 0; i < images.length; i++)
		{
			tx = AffineTransform.getScaleInstance(1, -1);
			tx.translate(-1 * images[i].getWidth(null), 0);
			op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			images[i] = op.filter(images[i], null);

		}
	}
	
	/**
	 * Increments the Animation's tick
	 */
	public void tick()
	{
		tick++;
	}
	
	/**
	 * Sets the Animation's tick
	 * @param tick
	 */
	public void setTick(long tick)
	{
		this.tick = tick;
	}
	
	/**
	 * Returns the current tick
	 * @return
	 */
	public long getTick()
	{
		return tick;
	}
	
	/**
	 * Returns whether the Animation limits itself
	 * @return
	 */
	public boolean limitingAnimation()
	{
		return limitAnimation;
	}
	
	/**
	 * Returns the image cache
	 * @return
	 */
	public BufferedImage[] getImages()
	{
		return images;
	}
	
	/**
	 * Returns the BufferedImage at a frame
	 * @param frame
	 * @return
	 */
	public BufferedImage getImageAtFrame(int frame)
	{
		return images[frame];
	}
	
	/**
	 * Animates an Entity with an Animation
	 * @param e
	 * @param a
	 */
	public static void animate(Entity e, Animation a)
	{
		BufferedImage img;
		img = a.nextFrame();
		if (a.flipHorz)
			img = AdvancedFilters.flipHorizontal(img);
		if (a.flipVert)
			img = AdvancedFilters.flipVertical(img);

		e.setImage(img);
	}	
	
	/**
	 * Animates a GuiElement with an Animation
	 * @param g
	 * @param a
	 */
	public static void animate(GuiElement g, Animation a)
	{
		g.setImage(a.nextFrame());
	}
}
