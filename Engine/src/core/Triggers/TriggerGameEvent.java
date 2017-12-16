package core.Triggers;

import java.awt.Color;

import core.Entity;
import core.GameEvent;
import core.Trigger;
import main.Main;

public class TriggerGameEvent
	extends Trigger
{
	GameEvent event;
	
	public TriggerGameEvent(Entity te, String name, String gameEvent) 
	{
		super(te, name);
		event = Main.getGameEventHandler().getByName(gameEvent);
		if (event == null)
			Main.println("Failed to find game event " + gameEvent, Color.RED);
	}

	public void onTouch(Entity triggerEntity) 
	{
		if (event != null)
			event.enable();
	}

	public void onStop()
	{
		if (event != null)
			event.disable();
	}

	public void onGameUpdate() 
	{

	}

	public void onGameInit() 
	{

	}
}
