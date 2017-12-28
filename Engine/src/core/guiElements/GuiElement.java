package core.guiElements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import javax.imageio.ImageIO;

import core.Entity;
import core.InputListener;
import core.MouseInputListener;
import core.MouseInputListener.Action;
import main.Main;
import utilities.AdvancedFilters;

/**
 * A <code>GuiElement</code> is almost the
 * same as an <code>Entity</code>, except its
 * position is static, has no player interaction,
 * and is always on top.
 * 
 * @author Ethan Vrhel
 * @see GuiElementHandler
 * @see Entity
 */
public abstract class GuiElement 
{
	protected String name;
	protected double x;
	protected double y;
	protected int z;
	protected double width;
	protected double height;
	protected Image image;
	protected Entity guiEntity;
	protected boolean isAlive;
	protected boolean isVisible;
	protected boolean hovered;
	protected boolean isText;
	
	private PointerInfo mouseInfo = MouseInfo.getPointerInfo();
	private Point mousePos = mouseInfo.getLocation();
	protected String keyPressed;
	
	/**
	 * Actions passed through <code>onAction(GuiAction a)</code>
	 * and <code>onAction(GuiAction a, InputListener inp)</code>
	 * to specify the action that has happened to the
	 * <code>GuiElement</code>
	 * 
	 * @author Ethan Vrhel
	 */
	public enum GuiAction
	{
		MOUSE_HOVER, MOUSE_DOWN, MOUSE_UP, MOUSE_CLICK, MOUSE_ENTERED, MOUSE_EXITED, KEYPRESS
	};
	
	/**
	 * The comparator for the <code>GuiElement</code>
	 */
	public static final Comparator<GuiElement> GuiElementZComparator = new Comparator<GuiElement>()
	{
		@Override
		public int compare(GuiElement entity1, GuiElement entity2)
		{
			if (entity1 != null && entity2 != null)
				return entity1.compareTo(entity2);
			else
				return 0;
		}
	};
	
	public GuiElement(String name, int type, File imageFile, double x, double y, int z, int w, int h)
	{
		isAlive = true;
		isVisible = true;
		this.name = name;
		this.x = x * Main.resolutionScaleX;
		this.y = y * Main.resolutionScaleY;
		this.width = w * Main.resolutionScaleX;
		this.height = h * Main.resolutionScaleY;
		try 
		{
			image = ImageIO.read(imageFile).getScaledInstance(w, h, Image.SCALE_DEFAULT);
			BufferedImage buff = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics g = buff.getGraphics();
			g.drawImage(image, 0, 0, null);
			image = AdvancedFilters.toCompatibleImage(buff);
		}
		catch (IOException e) 
		{
			Main.println("Error creating gui element: cannot read image file.", Color.RED);
		}
	}
	
	public GuiElement(String name, int type, BufferedImage image, double x, double y, int z, int w, int h)
	{
		isAlive = true;
		isVisible = true;
		this.name = name;
		this.x = x * Main.resolutionScaleX;
		this.y = y * Main.resolutionScaleY;
		this.width = w * Main.resolutionScaleX;
		this.height = h * Main.resolutionScaleY;
		this.image = image;
	}
	
	/**
	 * Updates the <code>GuiElement</code>
	 */
	public final void update()
	{
		MouseInputListener mouseListener = Main.getMouseListener();
		mouseInfo = MouseInfo.getPointerInfo();
		mousePos = mouseInfo.getLocation();
		mousePos.setLocation(mousePos.getX() - Main.getGameWindow().getX(), 
				mousePos.getY() - Main.getGameWindow().getY() - Main.getGameWindow().getInsets().top);
		
		if (hasMouseCollided())
		{
			onAction(GuiAction.MOUSE_HOVER);
			hovered = true;
			if (mouseListener.isMouse(Action.MOUSE_CLICKED))
				onAction(GuiAction.MOUSE_CLICK);
		}
		else
			reset();
		
		if (Main.getInputListener().isAKeyPressed())
			onAction(GuiAction.KEYPRESS, Main.getInputListener());
	}
	
	/**
	 * Returns the name
	 * @return The name
	 */
	public final String getName()
	{
		return name;
	}
	
	/**
	 * Translates the <code>GuiElement</code>
	 * @param x The x translation
	 * @param y The y translation
	 */
	public final void translate(int x, int y)
	{
		translateX(x);
		translateY(y);
	}
	
	/**
	 * Translates the x position
	 * @param x The x translation
	 */
	public final void translateX(int x)
	{
		this.x = x * Main.resolutionScaleX;
	}
	
	/**
	 * Translates the y position
	 * @param y The y translation
	 */
	public final void translateY(int y)
	{
		this.y = y * Main.resolutionScaleY;
	}
	
	/**
	 * Returns the x position
	 * @return The x position
	 */
	public final double getX()
	{
		return x;
	}
	
	/**
	 * Returns the y position
	 * @return The y position
	 */
	public final double getY()
	{
		return y;
	}
	
	/**
	 * Returns the secondary x position <code>(x + width)</code>
	 * @return The secondary x position
	 */
	public final double getX2()
	{
		return x + width;
	}
	
	/**
	 * Returns the secondary y position <code>(y + height)</code>
	 * @return The secondary y position
	 */
	public final double getY2()
	{
		return y + height;
	}
	
	/**
	 * Returns the <code>Image</code>
	 * @return The <code>Image</code>
	 */
	public final Image getImage()
	{
		return image.getScaledInstance((int) width, (int) height, Image.SCALE_DEFAULT);
	}
	
	/**
	 * Returns the width
	 * @return The width
	 */
	public final double getWidth()
	{
		return width;
	}
	
	/**
	 * Returns the height
	 * @return The height
	 */
	public final double getHeight()
	{
		return height;
	}
	
	/**
	 * Returns the actual width
	 * @return The actual width
	 */
	public final double getActualWidth()
	{
		return width / Main.resolutionScaleX;
	}
	
	/**
	 * Returns the actual height
	 * @return The actual height
	 */
	public final double getActualHeight()
	{
		return height / Main.resolutionScaleY;
	}
	
	/**
	 * Whether the mouse collided
	 * @return Whether the mouse has collided
	 */
	private boolean hasMouseCollided()
	{
		if (this.getY2() < mousePos.getY() || y > mousePos.getY())
			return false;
		else if (this.getX2() < mousePos.getX() || x > mousePos.getX())
			return false;
		else
			return true;
	}
	
	/**
	 * Returns the z-buffer
	 * @return The z-buffer
	 */
	public final int getZBuffer()
	{
		return z;
	}
	
	/**
	 * Compares this <code>GuiElement</code> to another
	 * along its z-buffer.
	 * @param compareElement The <code>GuiElement</code> to be
	 * compared
	 * @return The difference between the two elements
	 */
	public int compareTo(GuiElement compareElement) 
	{
		int compareZVal = compareElement.getZBuffer();
		return  this.z - compareZVal;
	}
	
	/**
	 * Whether the <code>GuiElement</code> is visible
	 * @return Whether the <code>GuiElement</code> is visible
	 */
	public boolean isVisible()
	{
		return isVisible;
	}
	
	/**
	 * Sets whether the <code>GuiElement</code> is visible
	 * @param isVisible The visibility
	 */
	public void setVisible(boolean isVisible)
	{
		this.isVisible = isVisible;
	}
	
	/**
	 * Returns whether the <code>GuiElement</code> is alive
	 * @return Whether the <code>GuiElement</code> is alive
	 */
	public boolean isAlive()
	{
		return isAlive;
	}
	
	/**
	 * Sets the <code>Image</code>
	 * @param img The <code>Image</code>
	 */
	public void setImage(Image img)
	{
		image = img;
	}
	
	/**
	 * Whether the <code>GuiElement</code> is only
	 * text
	 * @return Whether the <code>GuiElement</code>
	 * is only text
	 */
	public boolean isText()
	{
		return isText;
	}
	
	/**
	 * Called on an an action
	 * @param a the <code>GuiAction</code> that
	 * happened causing this method to be called.
	 */
	abstract void onAction(GuiAction a);
	
	/**
	 * The same as <code>onAction(GuiAction a)</code>, but
	 * includes an <code>InputListener</code> to view
	 * keys pressed.
	 * @param a The <code>GuiAction</code> that happened
	 * causing this method to be called
	 * @param inp The <code>InputListener</code>
	 */
	abstract void onAction(GuiAction a, InputListener inp);
	
	/**
	 * Resets the <code>GuiElement</code>
	 */
	abstract void reset();
}
