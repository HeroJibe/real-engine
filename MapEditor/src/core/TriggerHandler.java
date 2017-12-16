package core;

import java.awt.Color;

import main.Main;

public class TriggerHandler
	implements Runnable
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
	
	public void update()
	{
		for (int i = 0; i < triggersDyn; i++)
		{
			if (triggers[i] != null)
			{
				triggers[i].update();
			}
			System.out.print("");
		}
	}
	
	public void run()
	{
		return;
		/*
		Main.println("Trigger handler started");
		try
		{
			while (true)
			{
				//Main.println("the trigger handler is running");
				for (int i = 0; i < triggersDyn; i++)
				{
					if (triggers[i] != null)
					{
						triggers[i].update();
					}
					System.out.print("");
				}
			}
		}
		catch (Exception e)
		{
			Main.println("trigger handler stopped! " + e, Color.RED);
			Main.println("stopping...", Color.RED);
			Main.exit(1);
		}
		*/
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
		int enabled = 0;
		for (int i = 0; i < maxTriggers; i++)
		{
			if (triggers[i] != null)
			{
				triggers[i].setEnabled(true);
				enabled++;
			}
		}
		//Main.println("enabled " + enabled + " triggers");
	}
	
	// Disables all trigger entities
	public void disableAll()
	{
		int disabled = 0;
		for (int i = 0; i < maxTriggers; i++)
		{
			if (triggers[i] != null)
			{
				triggers[i].setEnabled(false);
				disabled++;
			}
		}
		//Main.println("disabled " + disabled + " triggers");
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
