package core;

import java.awt.Color;

import main.Main;

public abstract class Trigger
{
	private static int nextID = 0;
	
	private String triggerEntityName;
	private Entity triggerEntity;
	public Entity collisionEntity;
	private boolean isEnabled;
	private boolean isAlive;
	private boolean updated = false;
	private boolean beenTriggered = false;
	private boolean overrideUpdate = false;
	private int uniqueID;
	
	private boolean con = false;
	
	public Trigger(Entity triggerEntity, String name)
	{
		this.triggerEntity = triggerEntity;
		this.triggerEntityName = name;
		isAlive = true;
		uniqueID = nextID;
		nextID++;
	}
	
	public void update()
	{		
		if (isAlive)
		{
			collisionEntity = Main.getPlayer().getPlayerEntity();
			
			if (collisionEntity != null)
			{
				boolean temp = con && beenTriggered;
				if (triggerEntity == null)
				{
					Main.println("error at: \"" + this + "\": null triggerEntity");
					isEnabled = false;
				}
				else
				{
					System.out.print("");
					if (collisionEntity.hasCollidedIgnoreSolid(triggerEntity) || temp || overrideUpdate)
					{
						System.out.print("");
						beenTriggered = true;
						onTouch(collisionEntity);
					}
				}
			}
			else
			{
				Main.println("error at: \"" + this + "\": null collisionEntity");
				isEnabled = false;
			}
			
			updated = true;
		}
	}
	
	public String toString()
	{
		return "Trigger Entity: " + uniqueID;
	}
	
	public void setEnabled(boolean enabled)
	{
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
	
	public abstract void onTouch(Entity triggerEntity);
	
	public static int getNextID()
	{
		return nextID;
	}
	
	public static void resetID()
	{
		nextID = 0;
	}
}
