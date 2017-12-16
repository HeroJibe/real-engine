package core.MapEntities;

import core.Trigger;

public class InvokeTrigger
	extends MapEntity
{		
	private Trigger triggerEntity;
	
	public InvokeTrigger(int type, String name, long time, Trigger triggerEntity)
	{
		super(name, type, time);
		this.triggerEntity = triggerEntity;
		this.time = time;
	}
	
	public InvokeTrigger(int type, String name, Trigger triggerRecieve, Trigger triggerEntity)
	{
		super(name, type, triggerRecieve);
		this.triggerEntity = triggerEntity;
		this.triggerRecieve = triggerRecieve;
	}
	
	public void function()
	{
		if (triggerEntity != null)
			triggerEntity.overrideUpdate(true);
	}
}
