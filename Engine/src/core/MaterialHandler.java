package core;

public class MaterialHandler 
{
	public static final int MAX_MATERIALS = 8;
	
	private Material[] materials;
	
	public MaterialHandler()
	{
		materials = new Material[MAX_MATERIALS];
	}
	
	public void addMaterial(Material mat)
	{
		for (int i = 0; i < MAX_MATERIALS; i++)
		{
			if (materials[i] == null)
			{
				materials[i] = mat;
				return;
			}
		}
	}
	
	public Material getMaterial(String mat)
	{
		for (int i = 0; i < MAX_MATERIALS; i++)
		{
			if (materials[i] != null)
			{
				if (materials[i].getName() != null)
				{
					if (materials[i].getName().equals(mat))
					{
						return materials[i];
					}
				}
			}
		}
		return null;
	}
	
	public Material getMaterialByTexture(String texture)
	{
		for (int i = 0; i < MAX_MATERIALS; i++)
		{
			if (materials[i] != null)
			{
				if (materials[i].getTextures() != null)
				{
					for (int j = 0; j < materials[i].getTextures().length; j++)
					{
						if (materials[i].getTextures()[j].equals(texture))
						{
							return materials[i];
						}
					}
				}
			}
		}
		return null;
	}
}