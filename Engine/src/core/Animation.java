package core;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import core.guiElements.GuiElement;
import main.Main;
import utilities.AdvancedFilters;

/**
 * The <code>Animation</code> class provides tools for animating
 * entities and GUI elements.
 * 
 * @author	Ethan Vrhel
 * @see AnimationHandler
 * @see AnimationLoader
 * @see Entity
 * @see GuiElement
 */
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
	 * Returns the next frame of the <code>Animation</code>
	 * as a <code>BufferedImage</code>
	 * @return The next frame
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
	 * Returns the next frame of the <code>Animation</code>
	 * as a plain <code>Image</code>
	 * @return The next frame
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
	 * Returns the <code>BufferedImage</code> at a frame
	 * @param frame The frame number
	 * @return The <code>BufferedImage</code> at a frame
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
	 * Returns the current frame of the <code>Animation</code>
	 * @return The current frame
	 */
	public Image currFrame()
	{
		return images[frame];
	}
	
	/**
	 * Sets the frame of an <code>Animation</code>
	 * @param frame The frame number
	 */
	public void setFrame(int frame)
	{
		if (frame > images.length || frame < 0)
			Main.println("Error: animation frame outside of animation bounds", Color.RED);
		else
			this.frame = frame;
	}
	
	/**
	 * Updates the tied <code>Entity</code> 
	 * with the <code>Animation</code>
	 * @param e The <code>Entity</code>
	 */
	public void update(Entity e)
	{
		e.setImage(images[frame]);
	}
	
	/**
	 * Updates the tied <code>GuiElement</code> 
	 * with the <code>Animation</code>
	 * @param g The <code>GuiElement</code>
	 */
	public void update(GuiElement g)
	{
		g.setImage(images[frame]);
	}
	
	/**
	 * Returns the tied <code>Entity</code>
	 * @return the tied <code>Entity</code>
	 */
	public Entity getEntity()
	{
		return entity;
	}
	
	/**
	 * Returns the tied <code>GuiElement</code>
	 * @return the <code>GuiElement</code> tied to the <code>Animation</code>
	 */
	public GuiElement getGuiElement()
	{
		return element;
	}
	
	/**
	 * Returns the current frame
	 * @return The current frame of the <code>Animation</code>
	 */
	public int getFrame()
	{
		return frame;
	}
	
	/**
	 * Returns the rate of the <code>Animation</code>
	 * @return The rate
	 */
	public int getRate()
	{
		return rate;
	}
	
	/**
	 * Returns the number of frames in the <code>Animation</code>
	 * @return The number of frames
	 */
	public int getNumFrames()
	{
		return images.length;
	}
	
	/**
	 * Sets the rate
	 * @param rate The new rate
	 */
	public void setRate(int rate)
	{
		this.rate = rate;
	}
	
	/**
	 * Returns whether the <code>Animation</code> is animating or not
	 * @return Whether the <code>Animation</code> is animating
	 */
	public boolean isAnimating()
	{
		return runAnimation;
	}
	
	/**
	 * Sets whether the <code>Animation</code> should run or not
	 * @param runAnimation Whether the <code>Animation</code> should run or not
	 */
	public void shouldRun(boolean runAnimation)
	{
		this.runAnimation = runAnimation;
	}
	
	/**
	 * Returns the name of the <code>Animation</code>
	 * @return The name of the <code>Animation</code>
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
	 * Sets the tied <code>Entity</code>
	 * @param e The <code>Entity</code>
	 */
	public void setEntity(Entity e)
	{
		this.element = null;
		this.entity = e;
	}
	
	/**
	 * Sets the tied <code>GuiElement</code>
	 * @param g The <code>GuiElement</code>
	 */
	public void setGuiElement(GuiElement g)
	{
		this.entity = null;
		this.element = g;
	}
	
	/**
	 * Sets whether the <code>Animation</code> should limit itself
	 * @param limitAnimation Whether the <code>Animation</code> should
	 * limit itself
	 */
	public void limitAnimation(boolean limitAnimation)
	{
		this.limitAnimation = limitAnimation;
	}
	
	/**
	 * Sets the orientation of the <code>Animation</code>
	 * @param xOr x orientation
	 * @param yOr y orientation
	 */
	public void setOrientation(boolean xOr, boolean yOr)
	{
		flipHorz = xOr;
		flipVert = yOr;
	}
	
	/**
	 * Returns if the <code>Animation</code> is flipped horizontally
	 * @return Whether the <code>Animation</code> is flipped horizontally
	 */
	public boolean flipHorizontal()
	{
		return flipHorz;
	}
	
	/**
	 * Returns if the <code>Animation</code> is flipped vertically
	 * @return Whether the <code>Animation</code> is flipped vertically
	 */
	public boolean flipVertical()
	{
		return flipVert;
	}
	
	/**
	 * Updates the frames of the <code>Animation</code>
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
	 * Increments the tick
	 */
	synchronized void tick()
	{
		tick++;
	}
	
	/**
	 * Sets the tick
	 * @param tick The tick
	 */
	synchronized void setTick(long tick)
	{
		this.tick = tick;
	}
	
	/**
	 * Returns the current tick
	 * @return Current tick
	 */
	synchronized long getTick()
	{
		return tick;
	}
	
	/**
	 * Returns whether the <code>Animation</code> limits itself
	 * @return Whether the <code>Animation</code> limits itself
	 */
	public boolean limitingAnimation()
	{
		return limitAnimation;
	}
	
	/**
	 * Returns the image cache
	 * @return The frames of the <code>Animation</code>
	 */
	public BufferedImage[] getImages()
	{
		return images;
	}
	
	/**
	 * Returns the <code>BufferedImage</code> at a frame
	 * @param frame The frame
	 * @return The <code>BufferedImage</code> at a frame
	 */
	public BufferedImage getImageAtFrame(int frame)
	{
		return images[frame];
	}
	
	/**
	 * Animates an <code>Entity</code> with an <code>Animation</code>
	 * @param e The <code>Entity</code>
	 * @param a The <code>Animation</code>
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
	 * Animates a <code>GuiElement</code> with an <code>Animation</code>
	 * @param g The <code>GuiElement</code>
	 * @param a The <code>Animation</code>
	 */
	public static void animate(GuiElement g, Animation a)
	{
		g.setImage(a.nextFrame());
	}
}
