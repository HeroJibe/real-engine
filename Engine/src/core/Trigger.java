package core;

import main.Main;

public abstract class Trigger
	implements GameRunnable
{
	private static int nextID = 0;
	
	private String triggerEntityName;
	private Entity triggerEntity;
	public Entity collisionEntity;
	private boolean isAlive;
	private boolean beenTriggered = false;
	private boolean overrideUpdate = false;
	private int uniqueID;
	protected boolean requiresThread;
	protected boolean myThreadStarted;
	protected Entity threadArg;
	protected GameThread myThread;
	
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
		if (triggerEntity != null)
			triggerEntity.setKinetic(false);
	}
	
	public synchronized void update()
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
				}
				else
				{
					if (hasCollidedWith(triggerEntity) || temp || overrideUpdate)
					{
						beenTriggered = true;
						onTouch(collisionEntity);
					}
				}
			}
			else
			{
				Main.println("error at: \"" + this + "\": null collisionEntity");
			}
		}
		else
		{
		}
	}
	
	private boolean hasCollidedWith(Entity e)
	{
		Entity[] entities = collisionEntity.getCollisions(true);
		if (entities == null)
			return false;
		
		for (int i = 0; i < entities.length; i++)
		{
			if (entities[i] != null)
			{
				if (entities[i] == e)
				{
					return true;
				}
			}
		}
		return false;
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
	
	public void startThread()
	{ }
	
	public Entity getTriggerEntity()
	{
		return triggerEntity;
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
