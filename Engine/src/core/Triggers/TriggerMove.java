package core.Triggers;

import core.Entity;
import core.GameThread;
import core.Trigger;

public class TriggerMove
	extends Trigger
{
	private Entity toMove;
	private double moveX;
	private double moveY;
	private boolean once = true;
	private boolean done;
	private int delay;
	
	public TriggerMove(Entity triggerEntity, String name)
	{
		super(triggerEntity, name);
		requiresThread = true;
	}
	
	public TriggerMove(Entity triggerEntity, String name, Entity toMove)
	{
		super(triggerEntity, name);
		this.toMove = toMove;
		requiresThread = true;
	}
	
	@Override
	public void onGameUpdate() 
	{
		if (done && ! once)
		{	
			return;
		}
		
		toMove.translate(moveX, moveY);
		done = true;
	}

	@Override
	public void onGameInit() 
	{

	}
	
	public void onTouch(Entity entity)
	{
		if (myThread == null)
		{
			myThread = new GameThread(this, delay);
			myThread.start();
		}
		else if (! myThread.running() && isAlive())
		{
			myThread.start();
		}
	}
	
	public void onStop() 
	{
		if (myThread != null)
			myThread.stop();
	}
	
	public void setMoveX(double moveX)
	{
		this.moveX = moveX;
	}
	
	public void setMoveY(double moveY)
	{
		this.moveY = moveY;
	}
	
	public void setDelay(int delay)
	{
		this.delay = delay;
	}
}
