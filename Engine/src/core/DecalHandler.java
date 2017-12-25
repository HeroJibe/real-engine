package core;

/**
 * The <code>DecalHandler</code> class handles
 * all of the <code>Decal</code> objects in
 * the map
 * 
 * @author Ethan Vrhel
 * @see Decal
 */
public class DecalHandler 
{
	private Decal[] decals;
	private int maxDecals;
	
	public DecalHandler(int maxDecals)
	{
		decals = new Decal[maxDecals];
		this.maxDecals = maxDecals;
	}
	
	/**
	 * Applies all decals
	 */
	public void applyDecals()
	{
		for (int i = 0; i < maxDecals; i++)
		{
			if (decals[i] != null)
			{
				decals[i].update();
			}
		}
	}
	
	/**
	 * Adds a decal
	 * @param decal A decal
	 */
	public void addDecal(Decal decal)
	{
		for (int i = 0; i < maxDecals; i++)
		{
			if (decals[i] == null)
			{
				decals[i] = decal;
				return;
			}
		}
	}
}
