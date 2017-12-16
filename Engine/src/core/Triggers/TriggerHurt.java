package core.Triggers;

import core.Entity;
import core.Trigger;

public class TriggerHurt 
	extends Trigger
{
	public static final int GENERIC = 0;
	public static final int SPIKE = 1;
	public static final int SAW = 2;
	public static final int DROWN = 3;
	
	private int damage;
	public TriggerHurt(Entity triggerEntity, String name, int damage, int damageType)
	{
		super(triggerEntity, name);
		this.damage = damage;
	}
	
	public void onGameUpdate() 
	{

	}

	public void onGameInit() 
	{

	}
	
	public void onTouch(Entity triggerEntity)
	{
		triggerEntity.health -= damage;
	}
	
	public void onStop()
	{
		
	}
}
