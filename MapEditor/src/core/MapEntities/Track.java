package core.MapEntities;

import core.Entity;
import main.Main;

public class Track 
	extends MapEntity
{
	public static final int X_PLANE = 0;
	public static final int Y_PLANE = 1;
	public static final int CONTINUE = 0;
	public static final int REVERSE_ON_COLLIDE = 1;
	
	public static final int SIDE_BIAS = 2;
	
	private int plane;
	private double dest;
	private int delay;
	private Entity entity;
	private int dir;
	private boolean ignorePlayer;
	
	public Track(int plane, double dest, int delay, Entity entity, boolean ignorePlayer) 
	{
		super(CONTINUOUS, 0, 0, 1, 1);
		this.plane = plane;
		this.dest = dest;
		this.delay = delay;
		this.entity = entity;
		this.ignorePlayer = ignorePlayer;
		if (dest < 0)
			dir = 1;
		else
			dir = 0;
	}

	public void function() 
	{ }
}
