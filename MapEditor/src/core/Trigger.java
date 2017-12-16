package core;

import java.awt.Color;

import main.Main;

public abstract class Trigger
	implements Runnable
{
	private static int nextID = 0;
	
	private String triggerEntityName;
	private Entity triggerEntity;
	private boolean isEnabled;
	private boolean isAlive;
	private boolean updated = false;
	private boolean beenTriggered = false;
	private boolean overrideUpdate = false;
	private int uniqueID;
	protected boolean requiresThread;
	protected boolean myThreadStarted;
	protected Entity threadArg;
	
	private boolean con = false;
	
	public Trigger(Entity triggerEntity, String name)
	{
		this.triggerEntity = triggerEntity;
		this.triggerEntityName = name;
		isAlive = true;
		uniqueID = nextID;
		nextID++;
		requiresThread = false;
		myThreadStarted = false;
	}
	
	public void update()
	{		
		//if (requiresThread && myThreadStarted)
			//return;
		
		if (isAlive)
		{
			updated = true;
		}
		else
		{
		}
	}
	
	public String toString()
	{
		return "Trigger Entity: " + uniqueID;
	}
	
	public void setEnabled(boolean enabled)
	{
		//Main.println("enabled: " + enabled + " " + this);
		isAlive = enabled;
	}
	
	public boolean isAlive()
	{
		return isAlive;
	}
	
	public void setContinue(boolean con)
	{
		this.con = con;
	}
	
	public boolean beenTriggered()
	{
		return beenTriggered;
	}
	
	public String getName()
	{
		return triggerEntityName;
	}
	
	public void overrideUpdate(boolean override)
	{
		overrideUpdate = override;
	}
	
	public static int getNextID()
	{
		return nextID;
	}
	
	public static void resetID()
	{
		nextID = 0;
	}
	
	public abstract void onTouch(Entity triggerEntity);
	public abstract void onStop();
}
