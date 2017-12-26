package core;

/**
 * The <code>MaterialHandler</code> class
 * stores all of the <code>Material</code>'s
 * in the game.
 * 
 * @author Ethan Vrhel
 * @see Material
 */
public class MaterialHandler 
{
	private static final int MAX_MATERIALS = 8;
	
	private Material[] materials;
	
	public MaterialHandler()
	{
		materials = new Material[MAX_MATERIALS];
	}
	
	/**
	 * Adds a <code>Material</code>
	 * 
	 * @param mat The <code>Material</code>
	 */
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
	
	/**
	 * Returns a <code>Material</code> by
	 * its name
	 * 
	 * @param mat The name
	 * @return The respective <code>Material</code>,
	 * returns <code>null</code> if there is no such
	 * <code>Material</code>
	 */
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
	
	/**
	 * Returns a <code>Material</code> by the
	 * name of a texture
	 * 
	 * @param texture The texture's name
	 * @return The respective <code>Material</code>,
	 * returns <code>null</code> if there is no
	 * <code>Material</code> associated with the texture
	 */
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