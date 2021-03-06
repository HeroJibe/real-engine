package core;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import main.Main;
import utilities.ResourceMonitor;

/**
 * The <code>GameSound</code> class stores information
 * about sound which can be played.
 * 
 * @author Ethan Vrhel
 * @see GameSoundHandler
 * @see GameAmbientSound
 */
public class GameSound
	implements Runnable
{
	/**
	 * The constant for an unsigned sound type
	 */
	public static final int UNSINGED = 0;
	
	/**
	 * The constant for a music sound type
	 */
	public static final int MUSIC = 1;
	
	/**
	 * The constant for an effect sound type
	 */
	public static final int EFFECT = 2;
	
	/**
	 * The master volume
	 */
	public static float masterVolume = 1.0f;
	
	/**
	 * The music volume
	 */
	public static float musicVolume = 1.0f;
	
	/**
	 * The effect volume
	 */
	public static float effectVolume = 1.0f;
	
	private String name;
	Clip audioClip;
	private AudioInputStream audioInputStream;
	private int type;
	private SoundSource src;
	private int volume;

	public GameSound(String name, File soundFile, int type, SoundSource src, int volume)
	{
		this.name = name;
		this.src = src;
		this.volume = volume;
		if (name == null)
		{
			Main.println("failed to create sound object from: " + soundFile.toString() + " (the name is null)", Color.RED);
			return;
		}
		
		if (! soundFile.exists())
		{
			Main.println("failed to create sound object: " + soundFile.toString() + " (the file does not exist)");
		}
		
		if (type == -1)
		{
			if (soundFile.getName().contains("music"))
			{
				type = MUSIC;
			}
			else if (soundFile.getName().contains("sfx"))
			{
				type = EFFECT;
			}
			else
			{
				type = UNSINGED;
			}
		}
		else
		{
			this.type = type;
		}
		
		try
		{
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			audioClip = AudioSystem.getClip();
			audioClip.open(audioInputStream);
		} 
		catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) 
		{
			Main.println("Failed to create material: " + e, Color.RED);
		}
	}
	
	public GameSound(String name, Clip sound, SoundSource src, int volume)
	{
		this.name = name;
		this.volume = volume;
		this.audioClip = sound;
		this.src = src;
	}
	
	/**
	 * Plays the sound
	 */
	public synchronized void playSound()
	{
		long start = System.currentTimeMillis();
		if (audioClip.isRunning())
		{
			if (Main.getCurrentResourceMonitor() != null)
				Main.getCurrentResourceMonitor().increment(ResourceMonitor.Type.SOUND, System.currentTimeMillis() - start);
			return;
		}
		
		try 
		{
			FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain;
			float mult = getDistanceMultiplier();
			
			if (src != null)
			{
				/*
				float dist = -1;
				dist = src.getDistanceToPlayer();
				System.out.println("playing sound = " + name);
				System.out.println("source = " + src);
				System.out.println("distance = " + dist);
				System.out.println("mult = " + mult);
				System.out.println("__________________________");
				*/
			}
			else
			{
				/*
				System.out.println("playing sound = " + name);
				System.out.println("source = (ambient sound)");
				System.out.println("distance = n/a");
				System.out.println("mult = 1.0");
				System.out.println("__________________________");
				*/
			}
			
			if (type ==	MUSIC)
				gain = (range * masterVolume * musicVolume * mult) + gainControl.getMinimum();
			else if (type == EFFECT)
				gain = (range * masterVolume * effectVolume * mult) + gainControl.getMinimum();
			else
				gain = (range * masterVolume * mult) + gainControl.getMinimum();
			gainControl.setValue(gain);
			
			audioClip.setFramePosition(0);		
			audioClip.start();
		} 
		catch (Exception e) 
		{ 
			Main.println("error: " + e, Color.RED);
			e.printStackTrace(System.out);
		}
		if (Main.getCurrentResourceMonitor() != null)
			Main.getCurrentResourceMonitor().increment(ResourceMonitor.Type.SOUND, System.currentTimeMillis() - start);
	}
	
	private float getDistanceMultiplier()
	{
		if (Main.getPlayer() == null)
			return 0;
		else if (Main.getPlayer().getPlayerEntity() == null)
			return 0;
		
		if (src == null)
			return 1;
		
		float dist = src.getDistanceToPlayer();
		if (dist <= 1)
			return 1;
		
		float mult = (float) (volume / (dist));
		
		if (mult <= 0.05)
			return 0;
		
		if (mult > 1)
			return 1;
		
		return mult;
	}
	
	/**
	 * Do NOT invoke directly, use playSound() instead!
	 */
	@Override
	public void run()
	{
		playSound();
	}
	
	/**
	 * Stops the sound
	 */
	public synchronized void stopSound()
	{
		try 
		{
			audioClip.stop();
		} 
		catch (Exception e) 
		{ Main.println("error: " + e, Color.RED);}
	}
	
	/**
	 * Returns the sound's name
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Reinitializes the sound to update for external
	 * changes such as volume
	 */
	public synchronized void reinitialize()
	{
		//audioClip.stop();
		FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain;
		float mult = getDistanceMultiplier();
		
		if (type ==	MUSIC)
			gain = (range * masterVolume * musicVolume * mult) + gainControl.getMinimum();
		else if (type == EFFECT)
			gain = (range * masterVolume * effectVolume * mult) + gainControl.getMinimum();
		else
			gain = (range * masterVolume  * mult ) + gainControl.getMinimum();
		gainControl.setValue(gain);
		/*
		float dist = -1;
		dist = src.getDistanceToPlayer();
		System.out.println("updated.");
		System.out.println("playing sound = " + name);
		System.out.println("source = " + src);
		System.out.println("distance = " + dist);
		System.out.println("mult = " + mult);
		System.out.println("__________________________");
		*/
		//audioClip.start();
	}
	
	/**
	 * Sets the audio's source
	 * @param p
	 */
	public void setSource(SoundSource p)
	{
		src = p;
	}
	
	/**
	 * Gets the sound's source
	 * @return The sound's source, as a SoundSource
	 */
	public SoundSource getSource()
	{
		return src;
	}
	
	/**
	 * Returns whether the sound is playing
	 * @return Whether the sound is playing
	 */
	public boolean playing()
	{
		return audioClip.isActive();
	}
	
	public int getVolume()
	{
		return volume;
	}
	
	@Override
	public String toString()
	{
		return "[GameSound]: " + name + " src = " + src;
	}
}
