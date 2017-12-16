package core.Triggers;

import core.Entity;
import core.Trigger;
import core.MapEntities.MapEntity;
import main.Main;

public class TriggerToggleMapEntity 
	extends Trigger
{
	private MapEntity me;
	private boolean state;
	
	public TriggerToggleMapEntity(Entity triggerEntity, String name, String mapEntityName, boolean state)
	{
		super(triggerEntity, name);
		me = Main.getMapEntityHandler().getByName(mapEntityName);
		this.state = state;
	}

	public void onGameUpdate() 
	{
		
	}

	public void onGameInit() 
	{
		
	}

	public void onTouch(Entity triggerEntity)
	{
		if (me != null)
		{
			me.setEnabled(state);
		}
	}

	public void onStop() 
	{
		if (me != null)
		{
			me.setEnabled(! state);
		}
	}

}
