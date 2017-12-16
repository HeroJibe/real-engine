package core.Triggers;

import core.Entity;
import core.GameSound;
import core.Trigger;
import main.Main;

public class TriggerSound 
	extends Trigger
{
	private GameSound sound;

	public TriggerSound(Entity triggerEntity, String soundName, String name) 
	{
		super(triggerEntity, name);
		sound = new GameSound(name + " GameSound", Main.getResourceHandler().getByName(soundName));
	}
	
	public void run() {}

	public void onTouch(Entity triggerEntity)
	{
		sound.playSound();
	}
	
	public void onStop() 
	{
		sound.stopSound();
	}
}
