package core;

public class DecalHandler 
{
	private Decal[] decals;
	private int maxDecals;
	
	public DecalHandler(int maxDecals)
	{
		decals = new Decal[maxDecals];
		this.maxDecals = maxDecals;
	}
	
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
