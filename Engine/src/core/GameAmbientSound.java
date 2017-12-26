package core;

import java.io.File;

import javax.sound.sampled.Clip;

/**
 * A <code>GameSound</code>, but it has
 * no origin and therefore plays at the same
 * volume regardless of its position.
 * 
 * @author Ethan Vrhel
 * @see GameSound
 * @see GameSoundHandler
 */
public class GameAmbientSound 
	extends GameSound
	implements Runnable
{
	private boolean shouldRun;
	public GameAmbientSound(String name, File soundFile, int type)
	{
		super(name, soundFile, type, null, 1);
		shouldRun = true;
		
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
