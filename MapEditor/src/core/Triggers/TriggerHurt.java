package core.Triggers;

import core.Entity;
import core.Trigger;

public class TriggerHurt 
	extends Trigger
{
	private int damage;
	
	public TriggerHurt(Entity triggerEntity, String name, int damage)
	{
		super(triggerEntity, name);
		this.damage = damage;
	}
	
	public void run() {}
	
	public void onTouch(Entity triggerEntity)
	{
		triggerEntity.health -= damage;
	}
	
	public void onStop()
	{
		
	}
}
