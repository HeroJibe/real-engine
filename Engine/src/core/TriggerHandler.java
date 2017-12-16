
package core;

import main.Main;

public class TriggerHandler
	implements GameRunnable
{
	private Trigger triggers[];
	private int maxTriggers;
	private int triggersDyn;
	private int numTriggers;
	
	public TriggerHandler(int maxTriggers)
	{
		triggers = new Trigger[maxTriggers];
		this.maxTriggers = maxTriggers;
	}
	
	public synchronized void update()
	{
		for (int i = 0; i < triggersDyn; i++)
		{
			if (triggers[i] != null)
			{
				triggers[i].update();
			}
		}
	}
	
	public void onGameInit()
	{
		
	}
	
	public void onGameUpdate()
	{
		update();
	}
	
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
		Main.println("Trigger cache not big enough");
	}
	
	public void clearAllTriggers()
	{
		for (int i = 0; i < maxTriggers; i++)
		{
			triggers[i] = null;
		}
		triggersDyn = 0;
		numTriggers = 0;
	}
	
	// Enables all trigger entities
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
	
	// Disables all trigger entities
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
	
	public int getNumTriggers()
	{
		return numTriggers;
	}
}
