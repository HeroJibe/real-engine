package core.Triggers;

import core.Entity;
import core.Trigger;

public class TriggerStartTrigger 
	extends Trigger
{
	Trigger startTrigger;
	
	public TriggerStartTrigger(Entity te, String name, Trigger startTrigger) 
	{
		super(te, name);
		this.startTrigger = startTrigger;
	}
	
	public void onGameUpdate() 
	{

	}

	public void onGameInit() 
	{

	}

	public void onTouch(Entity triggerEntity) 
	{
		if (! startTrigger.isAlive())
		{
			startTrigger.setEnabled(true);
		}
	}
	
	public void onStop() {}
}
