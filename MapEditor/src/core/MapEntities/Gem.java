package core.MapEntities;

public class Gem
	extends MapEntity
{	
	public static final int SCALE_X = 16;
	public static final int SCALE_Y = 16;
	
	public static final int GREEN_GEM = 0;
	public static final int BLUE_GEM = 1;
	public static final int YELLOW_GEM = 2;
	
	private int gemType;
	private String name;

	public Gem(int gemType, int x, int y, String uniqueName)
	{
		super(ON_PLAYER_COLLIDE, x, y, SCALE_X, SCALE_Y);
		this.name = uniqueName;
	}

	public void function() 
	{

	}

	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return "Gem Entity type"  + gemType + " (" + x + ", " + y + ")";
	}
}
