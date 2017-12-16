package core.Triggers;

import core.Entity;
import core.GameThread;
import core.Trigger;
import main.Main;

public class TriggerGravityDecay 
	extends Trigger
{
	private double gravity;
	private double decayFac;
	private int rate;
	
	public TriggerGravityDecay(Entity triggerEntity, String name, double gravity, double decayFac, int rate) 
	{
		super(triggerEntity, name);
	}

	@Override
	public void onGameUpdate() 
	{
		if (gravity != Main.getPhysicsHandler().getGravity())
			Main.getPhysicsHandler().setGravity(Main.getPhysicsHandler().getGravity() / decayFac);
	}

	@Override
	public void onGameInit() 
	{

	}

	@Override
	public void onTouch(Entity triggerEntity) 
	{
		if (myThread == null)
		{
			myThread = new GameThread(this, rate);
			myThread.start();
		}
		else if (! myThread.running() && isAlive())
		{
			myThread.start();
		}
	}

	@Override
	public void onStop() 
	{
		if (myThread != null)
			myThread.stop();
	}
}
