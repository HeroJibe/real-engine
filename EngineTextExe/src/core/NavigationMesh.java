package core;

public class NavigationMesh 
{
	public static final int BUILD_SUCCESS = 0;
	public static final int BUILD_ERROR = 1;
	public static final int NAVMESH_SIZE = 16;
	
	private boolean[][] navMesh;
	private String map;
	private int width;
	private int height;
	
	public NavigationMesh(String map)
	{
		this.map = map;
		width = (int) (1920 / NAVMESH_SIZE);
		height = (int) (1080 / NAVMESH_SIZE);
		navMesh = new boolean[width][height];
	}
	
	public int buildNavMesh()
	{
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				Entity e = new Entity(Entity.STATIC, true, x * NAVMESH_SIZE, y * NAVMESH_SIZE, NAVMESH_SIZE, NAVMESH_SIZE, 100);
				if (e.hasCollidedNav())
				{
					navMesh[x][y] = false;
				}
				else
				{
					navMesh[x][y] = true;
				}
			}
		}
		
		return BUILD_SUCCESS;
	}
	
	public boolean isWalkable(double x, double y)
	{
		if (navMesh[(int) (x / NAVMESH_SIZE)][(int) (y / NAVMESH_SIZE)])
		{
			return true;
		}
		
		return false;
	}
}
