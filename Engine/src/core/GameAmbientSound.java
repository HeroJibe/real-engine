package core;

import java.awt.Point;
import java.io.File;

import javax.sound.sampled.Clip;

import main.Main;

public class GameAmbientSound 
	extends GameSound
	implements Runnable
{
	private boolean shouldRun;
	private boolean shouldLoop;
	private boolean shouldDie;
	
	public GameAmbientSound(String name, File soundFile, int type)
	{
		super(name, soundFile, type, null, 1);
		shouldRun = true;
		shouldLoop = true;
		
	}
	
	public GameAmbientSound(String name, Clip sound) 
	{
		super(name, sound, null, 1);
		shouldRun = true;
	}

	@Override
	public void run()
	{
		while (shouldRun)
		{
			if (! audioClip.isActive() || audioClip.getFramePosition() >= audioClip.getFrameLength())
			{
				playSound();
			}
			else
			{
				Thread.yield();
			}
		}
	}
}
