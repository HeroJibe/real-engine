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
	
	// Updates each GUI element
	public void update()
	{
		for (int i = 0; i < maxElements; i++)
		{
			if (elements[i] != null)
				elements[i].update();
		}
	}
	
	// Adds a GUI element to the cache
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
	
	// Returns an element based on its name
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
	
	// Returns the number of GUI elements
	public int getNumElements()
	{
		return numElements;
	}
	
	// Gets the dynamic size of the GUI element cache
	public int getElementsDyn()
	{
		return elementsDyn;
	}
	
	// Returns all GUI elements
	public GuiElement[] getGuiElements()
	{
		return elements;
	}
}
