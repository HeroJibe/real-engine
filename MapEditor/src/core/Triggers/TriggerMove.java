package core.Triggers;

import java.awt.Color;

import core.Entity;
import core.Trigger;
import main.Main;

public class TriggerMove
	extends Trigger
{
	private Entity toMove;
	private boolean useTouchedEntity = false;
	private double moveX;
	private double moveY;
	private boolean once = true;
	private boolean done;
	private int delay;
	
	public TriggerMove(Entity triggerEntity, String name)
	{
		super(triggerEntity, name);
		useTouchedEntity = true;
		requiresThread = true;
	}
	
	public TriggerMove(Entity triggerEntity, String name, Entity toMove)
	{
		super(triggerEntity, name);
		this.toMove = toMove;
		requiresThread = true;
	}
	
	public void run()
	{
		onTouch(threadArg);
	}
	
	public void onTouch(Entity entity)
	{
		if (done && ! once)
		{	
			return;
		}
		
		if (useTouchedEntity)
		{
			entity.translate(moveX, moveY);
		}
		else
		{
			toMove.translate(moveX, moveY);
		}
		done = true;
		
		if (delay != 0)
		{
			try
			{
				Thread.sleep(delay);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void onStop() 
	{
		
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
