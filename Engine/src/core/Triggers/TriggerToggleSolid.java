package core.Triggers;

import core.Entity;
import core.Trigger;
import main.Main;

public class TriggerToggleSolid
	extends Trigger
{
	private Entity ent;
	private boolean toggled;
	
	public TriggerToggleSolid(Entity triggerEntity, String name, String entityName)
	{
		super(triggerEntity, name);
		ent = Main.getEntityHandler().getEntityByName(entityName);
		toggled = false;
	}

	@Override
	public void onGameUpdate()
	{ }

	@Override
	public void onGameInit()
	{ }

	@Override
	public void onTouch(Entity triggerEntity) 
	{
		if (toggled)
			return;
		ent.setSolid(! ent.isSolid());
		toggled = true;
	}

	@Override
	public void onStop() 
	{ 
		ent.setSolid(! ent.isSolid());
	}

}
