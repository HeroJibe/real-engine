package core;

public class ReflectiveEntityHandler 
	implements Runnable 
{
	public static final int NO_REFLECTIONS = 0;
	public static final int STATIC_REFLECTIONS = 1;
	public static final int FAST_DYNAMIC_REFLECTIONS = 2;
	public static final int FULL_DYNAMIC_REFLECTIONS = 3;
	
	public int blurAmount = 1;
	public boolean updating = false;
	public int reflectionDelay = 0;
	
	private ReflectiveEntity[] entities;
	private int maxEntities;
	private int reflectionType;
	private boolean updateReflections;
	
	public ReflectiveEntityHandler(int maxEntities)
	{
		entities = new ReflectiveEntity[maxEntities];
		this.maxEntities = maxEntities;
		reflectionType = STATIC_REFLECTIONS;
		updateReflections = true;
	}
	
	public void initReflections()
	{
		if (reflectionType != STATIC_REFLECTIONS)
			updateImages();
	}
	
	public synchronized void update(boolean force)
	{		
		updating = true;
		if (reflectionType == FAST_DYNAMIC_REFLECTIONS || reflectionType == FULL_DYNAMIC_REFLECTIONS)
			updateReflections = true;
		else if (reflectionType == NO_REFLECTIONS)
			updateReflections = false;
		else if (reflectionType == STATIC_REFLECTIONS)
			updateReflections = false;
		
		if (force)
			updateReflections = true;
		
		if (updateReflections)
		{
			updateImages();
			updateReflections = false;
		}
		updating = false;
	}
	
	public synchronized void updateImages()
	{
		for (int i = 0; i < maxEntities; i++)
		{
			if (entities[i] != null)
			{
				entities[i].updateImage(reflectionType, false);
			}
		}
	}

	public void run() 
	{
		/*
		Main.println("Reflection handler started");
		while (true)
		{
			//if (reflectionType == NO_REFLECTIONS || reflectionType == STATIC_REFLECTIONS)
				//Thread.yield();
			update(false);
		}
		*/
		
	}

	public void addReflectiveEntity(ReflectiveEntity refl)
	{
		for (int i = 0; i < maxEntities; i++)
		{
			if (entities[i] == null)
			{
				//Main.println("added refl");
				entities[i] = refl;
				return;
			}
		}
	}
	
	public void addAll()
	{
		for (int i = 0; i < maxEntities; i++)
		{
			if (entities[i] != null)
			{
				entities[i].updateImage(reflectionType, true);
			}
		}
	}
	
	public void removeAll()
	{
		for (int i = 0; i < maxEntities; i++)
		{
			entities[i] = null;
		}
	}
	
	public void setReflections(int reflectionType)
	{
		this.reflectionType = reflectionType;
	}
	
	public int getReflectionType()
	{
		return reflectionType;
	}
}
