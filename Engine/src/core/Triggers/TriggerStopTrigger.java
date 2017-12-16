package core.Triggers;

import core.Entity;
import core.Trigger;

public class TriggerStopTrigger 
	extends Trigger
{
	Trigger stopTrigger;
	
	public TriggerStopTrigger(Entity te, String name, Trigger stopTrigger) 
	{
		super(te, name);
		this.stopTrigger = stopTrigger;
	}
	
	public void onGameUpdate() 
	{

	}

	public void onGameInit() 
	{

	}

	public void onTouch(Entity triggerEntity) 
	{
		if (triggerEntity.isAlive())
		{
			stopTrigger.setEnabled(false);
			stopTrigger.onStop();
		}
	}
	
	public void onStop() {}
}
