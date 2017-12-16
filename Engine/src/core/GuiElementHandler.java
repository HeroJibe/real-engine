/**
 * The GuiElementHandler class handles all 
 * GuiElements.
 * 
 * @author Ethan Vrhel
 */

package core;

import java.awt.Color;

import core.guiElements.GuiElement;
import main.Main;

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
	 * Returns a GuiElement by its name
	 * 
	 * @param name
	 * @return
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
	 * Returns the number of GuiElements
	 * 
	 * @return
	 */
	public int getNumElements()
	{
		return numElements;
	}
	
	/**
	 * Returns the dynamic size of the
	 * GuiElement cache
	 * 
	 * @return
	 */
	public int getElementsDyn()
	{
		return elementsDyn;
	}
	
	/**
	 * Returns all of the GuiElements
	 * 
	 * @return
	 */
	public GuiElement[] getGuiElements()
	{
		return elements;
	}
}
