package core.Triggers;

import core.Entity;
import core.GameAmbientSound;
import core.GameSound;
import core.Trigger;
import main.Main;

public class TriggerSound 
	extends Trigger
{
	private GameAmbientSound sound;

	public TriggerSound(Entity triggerEntity, String soundName, int soundType, String name) 
	{
		super(triggerEntity, name);
		sound = new GameAmbientSound(name + " GameSound", Main.getResourceHandler().getByName(soundName), soundType);
	}
	
	public void onGameUpdate() 
	{

	}

	public void onGameInit() 
	{

	}

	public void onTouch(Entity triggerEntity)
	{
		sound.playSound();
	}
	
	public void onStop() 
	{
		sound.stopSound();
	}
}
