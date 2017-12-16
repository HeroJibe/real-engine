package core.Triggers;

import core.Entity;
import core.Trigger;

public class TriggerBreak 
	extends Trigger
{
	private Entity toBreak;
	
	public TriggerBreak(Entity triggerEntity, String name, Entity toBreak)
	{
		super(triggerEntity, name);
		this.toBreak = toBreak;
	}

	@Override
	public void onGameUpdate() 
	{
		
	}

	@Override
	public void onGameInit() 
	{
		
	}

	@Override
	public void onTouch(Entity triggerEntity) 
	{
		toBreak.setAlive(false);
		toBreak = null;
		setEnabled(false);
	}

	@Override
	public void onStop() 
	{
		
	}

}
