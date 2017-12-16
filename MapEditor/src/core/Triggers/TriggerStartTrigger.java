package core.Triggers;

import java.awt.Color;

import core.Entity;
import core.Trigger;
import main.Main;

public class TriggerStartTrigger 
	extends Trigger
{
	Trigger startTrigger;
	
	public TriggerStartTrigger(Entity te, String name, Trigger startTrigger) 
	{
		super(te, name);
		this.startTrigger = startTrigger;
	}
	
	public void run() {}

	public void onTouch(Entity triggerEntity) 
	{
		if (! startTrigger.isAlive())
		{
			startTrigger.setEnabled(true);
		}
	}
	
	public void onStop() {}
}
