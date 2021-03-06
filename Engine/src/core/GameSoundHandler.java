/**
 * The GameSoundHandler stores all
 * GameSounds
 * 
 * @author Ethan Vrhel
 */

package core;

import java.awt.Color;

import main.Main;

public class GameSoundHandler
	implements Runnable
{
	public static final long UPDATE_WAIT = 10;
	
	private GameSound[] materials;
	
	public GameSoundHandler(int cacheSize)
	{
		materials = new GameSound[cacheSize];
	}
	
	/**
	 * Adds a GameSound
	 * 
	 * @param mat
	 */
	public void addMaterial(GameSound mat)
	{
		for (int i = 0; i < materials.length; i++)
		{
			if (materials[i] == null)
			{
				materials[i] = mat;
				return;
			}
		}
		
		Main.println("Failed to add material; cache size not big enough!", Color.RED); 
	}
	
	/**
	 * Stops all sounds
	 */
	public void stopAll()
	{
		for (int i = 0; i < materials.length; i++)
		{
			if (materials[i] != null)
			{
				materials[i].stopSound();
			}
		}
	}
	
	/**
	 * Returns a GameSound by its name
	 * 
	 * @param name
	 * @return
	 */
	public GameSound getByName(String name)
	{
		for (int i = 0; i < materials.length; i++)
		{
			if (materials[i] != null)
			{
				if (materials[i].getName().equals(name))
				{
					return materials[i];
				}
			}
		}
		return null;
	}
	
	@Override
	public void run()
	{
		///if (true)
		// return;
		while (true)
		{
			try
			{
				for (int i = 0; i < materials.length; i++)
				{
					if (materials[i] != null)
					{
						materials[i].reinitialize();
					}
				}
				//System.out.println("re-initialized " + init);
				Thread.sleep(UPDATE_WAIT);
			}
			catch (Exception e)
			{
				Main.println("Sound handler was interrputed", Color.RED);
				e.printStackTrace(System.out);
			}
		}
	}
}
