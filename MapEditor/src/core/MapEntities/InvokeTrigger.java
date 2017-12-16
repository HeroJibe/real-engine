package core.MapEntities;

import java.awt.Color;

import core.Trigger;
import main.Main;

public class InvokeTrigger
	extends MapEntity
{		
	private Trigger triggerEntity;
	
	public InvokeTrigger(int type, long time, Trigger triggerEntity)
	{
		super(type, time);
		this.triggerEntity = triggerEntity;
		this.time = time;
	}
	
	public InvokeTrigger(int type, Trigger triggerRecieve, Trigger triggerEntity)
	{
		super(type, triggerRecieve);
		this.triggerEntity = triggerEntity;
		this.triggerRecieve = triggerRecieve;
	}
	
	public void function()
	{
		if (triggerEntity != null)
			triggerEntity.overrideUpdate(true);
	}
}
