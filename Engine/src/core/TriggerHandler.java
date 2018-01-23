
package core;

import java.awt.Color;

import main.Main;

/**
 * Stores <code>Trigger</code> objects
 * 
 * @author Ethan Vrhel
 * @see Trigger
 */
public final class TriggerHandler
	implements GameRunnable
{
	private Trigger triggers[];
	private int maxTriggers;
	private int triggersDyn;
	private int numTriggers;
	
	/**
	 * Creates a new <code>TriggerHandler</code>
	 * 
	 * @param maxTriggers The maximum of <code>Trigger</code>'s
	 */
	public TriggerHandler(int maxTriggers)
	{
		triggers = new Trigger[maxTriggers];
		this.maxTriggers = maxTriggers;
	}
	
	private synchronized void update()
	{
		for (int i = 0; i < triggersDyn; i++)
		{
			if (triggers[i] != null)
			{
				triggers[i].update();
			}
		}
	}
	
	@Override
	public void onGameInit()
	{
		
	}
	
	@Override
	public void onGameUpdate()
	{
		update();
	}
	
	/**
	 * Adds a <code>Trigger</code> to the cache
	 * 
	 * @param toAdd A <code>Trigger</code>
	 */
	public void addTrigger(Trigger toAdd)
	{	
		for (int i = 0; i < maxTriggers; i++)
		{
			if (triggers[i] == null)
			{
				triggers[i] = toAdd;
				if (i == triggersDyn)
					triggersDyn++;
				numTriggers++;
				return;
			}
		}
		Main.println("Trigger cache not big enough", Color.RED);
	}
	
	/**
	 * Removes each <code>Trigger</code> in
	 * the cache
	 */
	public void clearAllTriggers()
	{
		triggers = new Trigger[maxTriggers];
		System.gc();
		triggersDyn = 0;
		numTriggers = 0;
	}
	
	/**
	 * Enables each <code>Trigger</code>
	 */
	public void enableAll()
	{
		for (int i = 0; i < maxTriggers; i++)
		{
			if (triggers[i] != null)
			{
				triggers[i].setEnabled(true);
				if (Main.DRAW_TRIGGERS)
				{
					triggers[i].getTriggerEntity().setImage(ResourceHandler.debugTextureImage);
					triggers[i].getTriggerEntity().setVisible(true);
				}
			}
		}
		//Main.println("enabled " + enabled + " triggers");
	}
	
	/**
	 * Disables each <code>Trigger</code>
	 */
	public void disableAll()
	{
		for (int i = 0; i < maxTriggers; i++)
		{
			if (triggers[i] != null)
			{
				triggers[i].setEnabled(false);
			}
		}
		//Main.println("disabled " + disabled + " triggers");
	}
	
	protected Trigger[] getTriggers()
	{
		return triggers;
	}
	
	/**
	 * Returns a <code>Trigger</code> by its
	 * name
	 * 
	 * @param name The name of a <code>Trigger</code>
	 * @return A <code>Trigger</code>, or <code>null</code>
	 * if no <code>Trigger</code> is found that matches
	 * the name
	 */
	public Trigger getByName(String name)
	{
		for (int i = 0; i < maxTriggers; i++)
		{
			if (triggers[i] != null)
			{
				if (triggers[i].getName().equals(name + " Trigger"))
					return triggers[i];
			}
		}
		return null;
	}
	
	/**
	 * Returns the number of 
	 * <code>Trigger</code>'s
	 * 
	 * @return The number of
	 * <code>Trigger</code>'s
	 */
	public int getNumTriggers()
	{
		return numTriggers;
	}
}
