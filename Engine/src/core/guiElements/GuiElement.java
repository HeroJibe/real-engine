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
	
	
	public enum GuiAction
	{
		MOUSE_HOVER, MOUSE_DOWN, MOUSE_UP, MOUSE_CLICK, MOUSE_ENTERED, MOUSE_EXITED, KEYPRESS
	};
	
	public static Comparator<GuiElement> GuiElementZComparator = new Comparator<GuiElement>()
	{
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
	
	public void update()
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
	
	public String getName()
	{
		return name;
	}
	
	public void translate(int x, int y)
	{
		translateX(x);
		translateY(y);
	}
	
	public void translateX(int x)
	{
		this.x = x * Main.resolutionScaleX;
	}
	
	public void translateY(int y)
	{
		this.y = y * Main.resolutionScaleY;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getX2()
	{
		return x + width;
	}
	
	public double getY2()
	{
		return y + height;
	}
	
	public Image getImage()
	{
		return image.getScaledInstance((int) width, (int) height, Image.SCALE_DEFAULT);
	}
	
	public double getWidth()
	{
		return width;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public double getActualWidth()
	{
		return width / Main.resolutionScaleX;
	}
	
	public double getActualHeight()
	{
		return height / Main.resolutionScaleY;
	}
	
	private boolean hasMouseCollided()
	{
		if (this.getY2() < mousePos.getY() || y > mousePos.getY())
			return false;
		else if (this.getX2() < mousePos.getX() || x > mousePos.getX())
			return false;
		else
			return true;
	}
	
	public int getZBuffer()
	{
		return z;
	}
	
	public int compareTo(GuiElement compareElement) 
	{
		int compareZVal = compareElement.getZBuffer();
		
		if (this.z == compareZVal)
		{
			if (this.x == compareElement.getX())
			{
				if (this.y == compareElement.getY())
				{
					return 0;
				}
				return (int) (compareElement.getY() - this.y);
			}
			return (int) (this.x - compareElement.getX());
		}
		
		
		return  this.z - compareZVal;
	}
	
	public boolean isVisible()
	{
		return isVisible;
	}
	
	public void setVisible(boolean isVisible)
	{
		this.isVisible = isVisible;
	}
	
	public boolean isAlive()
	{
		return isAlive;
	}
	
	public void setImage(Image img)
	{
		image = img;
	}
	
	public boolean isText()
	{
		return isText;
	}
	
	abstract void onAction(GuiAction a);
	abstract void onAction(GuiAction a, InputListener inp);
	abstract void reset();
}
