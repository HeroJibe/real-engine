package core;

import java.awt.Color;

import main.Main;

public class MaterialHandler
{
	private Material[] materials;
	private int maxMaterials;
	private int numMaterials = 0;
	
	public MaterialHandler(int cacheSize)
	{
		maxMaterials = cacheSize;
		materials = new Material[cacheSize];
	}
	
	public void addMaterial(Material mat)
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
	
	public Material getByName(String name)
	{
		return null;
	}
}
