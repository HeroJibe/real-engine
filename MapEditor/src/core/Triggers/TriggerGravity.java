package core.Triggers;

import core.Entity;
import core.Trigger;
import main.Main;

public class TriggerGravity 
	extends Trigger
{
	private double gravity;
	
	public TriggerGravity(Entity triggerEntity, String name, double gravity)
	{
		super(triggerEntity, name);
		this.gravity = gravity;
	}

	public void run() {}
	
	public void onTouch(Entity triggerEntity)
	{
		Main.getPhysicsHandler().gravity = gravity;
	}

	public void onStop() 
	{
		
	}
	
}
