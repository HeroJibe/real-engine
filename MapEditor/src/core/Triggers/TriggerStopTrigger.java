package core.Triggers;

import java.awt.Color;

import core.Entity;
import core.Trigger;
import main.Main;

public class TriggerStopTrigger 
	extends Trigger
{
	Trigger stopTrigger;
	
	public TriggerStopTrigger(Entity te, String name, Trigger stopTrigger) 
	{
		super(te, name);
		this.stopTrigger = stopTrigger;
	}
	
	public void run() {}

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
