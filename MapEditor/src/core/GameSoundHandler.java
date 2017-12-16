package core;

import java.awt.Color;

import main.Main;

public class GameSoundHandler
{
	private GameSound[] materials;
	private int maxMaterials;
	private int numMaterials = 0;
	
	public GameSoundHandler(int cacheSize)
	{
		maxMaterials = cacheSize;
		materials = new GameSound[cacheSize];
	}
	
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
	
	public GameSound getByName(String name)
	{
		return null;
	}
}
