package core;

import java.awt.Color;

import core.guiElements.GuiElement;
import main.Main;

/**
 * The GuiElementHandler class handles all 
 * GuiElements.
 * 
 * @author Ethan Vrhel
 * @see GuiElement
 */
public class GuiElementHandler 
{
	private GuiElement elements[];
	private int numElements = 0;
	private int elementsDyn = 0;
	private int maxElements;
	
	public GuiElementHandler(int maxElements)
	{
		this.maxElements = maxElements;
		elements = new GuiElement[maxElements];
	}
	
	/**
	 * Updates each GuiElements
	 */
	public synchronized void update()
	{
		for (int i = 0; i < maxElements; i++)
		{
			if (elements[i] != null)
				elements[i].update();
		}
	}
	
	/**
	 * Adds a GuiElement to the cache
	 * @param elem
	 */
	public void add(GuiElement elem)
	{
		for (int i = 0; i < maxElements; i++)
		{
			if (elements[i] == null)
			{
				elements[i] = elem;
				numElements++;
				if (i == elementsDyn)
					elementsDyn++;
				return;
			}
			else if (! elements[i].isAlive())
			{
				elements[i] = elem;
				numElements++;
				if (i == elementsDyn)
					elementsDyn++;
				return;
			}
		}
		Main.println("Gui cache not big enough!", Color.RED);
	}
	
	/**
	 * Removes an <code>GuiElement</code> by its name
	 * 
	 * @param name The name
	 */
	public void remove(String name)
	{
		for (int i = 0; i < maxElements; i++)
		{
			if (elements[i] != null)
			{
				if (elements[i].getName().equals(name))
				{
					elements[i] = null;
					return;
				}
			}
		}
	}
	
	/**
	 * Returns a <code>GuiElement</code> by its name
	 * 
	 * @param name
	 * @return A <code>GuiElement</code> and <code>null</code>
	 * if it doesn't exist
	 */
	public GuiElement getElementByName(String name)
	{
		for (int i = 0; i < elements.length; i++)
		{
			if (elements[i] != null)
			{
				if (elements[i].getName().equals(name))
				{
					return elements[i];
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the number of elements
	 * 
	 * @return The number of elements
	 */
	public int getNumElements()
	{
		return numElements;
	}
	
	/**
	 * Returns the dynamic size of the
	 * <code>GuiElement</code> cache
	 * 
	 * @return The dynamic size
	 */
	public int getElementsDyn()
	{
		return elementsDyn;
	}
	
	/**
	 * Returns all of the <code>GuiElement</code>'s
	 * 
	 * @return All of the <code>GuiElement</code>'s
	 */
	public GuiElement[] getGuiElements()
	{
		return elements;
	}
}
