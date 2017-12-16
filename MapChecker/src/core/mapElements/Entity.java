package core.mapElements;

import core.MapElement;
import core.MapReturn;

public abstract class Entity
	extends MapElement
{
	public static final int MAX_ENTITIES = 32;
	private static Entity[] entities = new Entity[MAX_ENTITIES];
	
	private String entityType;
	
	public Entity(String entityType) 
	{
		super("Entity");
		this.entityType = entityType;
		addEntityType(this);
	}

	@Override
	public MapReturn check(String[] args)
	{
		try
		{
			Entity myEntity = getByName(args[0]);
			if (myEntity == null)
			{
				throw new IllegalArgumentException("Unknown entity type \"" + args[0] + "\"");
			}
			MapReturn ret = myEntity.checkEntity(args);
			return ret;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return new MapReturn("Invalid number of arguments", MapReturn.ERROR);
		}
		catch (NumberFormatException e)
		{
			return new MapReturn("Parsing error", MapReturn.ERROR);
		}
		catch (IllegalArgumentException e)
		{
			return new MapReturn(e.getMessage(), MapReturn.ERROR);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			return new MapReturn("Unhandled exception " + e, MapReturn.ERROR);
		}
	}
	
	public String getEntityType()
	{
		return entityType;
	}
	
	protected abstract MapReturn checkEntity(String[] args);

	private static void addEntityType(Entity entity)
	{
		for (int i = 0; i < entities.length; i++)
		{
			if (entities[i] == null)
			{
				entities[i] = entity;
				return;
			}
		}
	}
	
	private static Entity getByName(String name)
	{
		for (int i = 0; i < entities.length; i++)
		{
			if (entities[i] != null)
				if (entities[i].getName() != null)
					if (entities[i].getEntityType().equals(name))
						return entities[i];
		}
		return null;
	}
}
