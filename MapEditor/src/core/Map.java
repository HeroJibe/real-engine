package core;

import core.MapEntities.Gem;
import core.MapEntities.InvokeTrigger;
import core.MapEntities.LevelText;
import core.MapEntities.MapEntity;
import core.MapEntities.Track;
import core.Triggers.*;
import main.Main;

public class Map 
{
	public static final int MAX_ELEMENTS = 256;
	
	String name;
	int[] backgroundRGB;
	TriggerGameEvent[] triggerGEs;
	TriggerGravity[] triggerGs;
	TriggerHurt[] triggerHs;
	TriggerLoad[] triggerLs;
	TriggerMove[] triggerMs;
	TriggerShader[] triggerSHs;
	TriggerSound[] triggerSOs;
	TriggerStartTrigger[] triggerSTAs;
	TriggerStopTrigger[] triggerSTOs;
	Entity[] entities;
	Gem[] gems;
	Track[] tracks;
	InvokeTrigger[] invkTriggers;
	LevelText levelTxt;
	
	public Map(String name)
	{
		this.name = name;
		backgroundRGB = new int[3];
		triggerGEs = new TriggerGameEvent[MAX_ELEMENTS];
		triggerGs = new TriggerGravity[MAX_ELEMENTS];
		triggerHs = new TriggerHurt[MAX_ELEMENTS];
		triggerLs = new TriggerLoad[MAX_ELEMENTS];
		triggerMs = new TriggerMove[MAX_ELEMENTS];
		triggerSHs = new TriggerShader[MAX_ELEMENTS];
		triggerSOs = new TriggerSound[MAX_ELEMENTS];
		triggerSTAs = new TriggerStartTrigger[MAX_ELEMENTS];
		triggerSTOs = new TriggerStopTrigger[MAX_ELEMENTS];
		entities = new Entity[MAX_ELEMENTS];
		gems = new Gem[MAX_ELEMENTS];
		tracks = new Track[MAX_ELEMENTS];
		invkTriggers = new InvokeTrigger[MAX_ELEMENTS];
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setLevelText(String text)
	{
		this.levelTxt = new LevelText(MapEntity.ON_MAP_LOAD, text);
	}
	
	public void setBackground(int r, int g, int b)
	{
		backgroundRGB[0] = r;
		backgroundRGB[1] = g;
		backgroundRGB[2] = b;
	}
	
	public void add(TriggerGameEvent ge)
	{
		for (int i = 0; i < triggerGEs.length; i++)
		{
			if (triggerGEs[i] == null)
			{
				triggerGEs[i] = ge;
				return;
			}
		}
	}
	
	public void add(TriggerGravity ge)
	{
		for (int i = 0; i < triggerGEs.length; i++)
		{
			if (triggerGs[i] == null)
			{
				triggerGs[i] = ge;
				return;
			}
		}
	}
	
	public void add(TriggerHurt ge)
	{
		for (int i = 0; i < triggerGEs.length; i++)
		{
			if (triggerHs[i] == null)
			{
				triggerHs[i] = ge;
				Main.println("Added game event trigger.");
				return;
			}
		}
	}
	
	public void add(TriggerLoad ge)
	{
		for (int i = 0; i < triggerGEs.length; i++)
		{
			if (triggerLs[i] == null)
			{
				triggerLs[i] = ge;
				return;
			}
		}
	}
	
	public void add(TriggerMove ge)
	{
		for (int i = 0; i < triggerGEs.length; i++)
		{
			if (triggerMs[i] == null)
			{
				triggerMs[i] = ge;
				return;
			}
		}
	}
	
	public void add(TriggerShader ge)
	{
		for (int i = 0; i < triggerGEs.length; i++)
		{
			if (triggerSHs[i] == null)
			{
				triggerSHs[i] = ge;
				return;
			}
		}
	}
	
	public void add(TriggerSound ge)
	{
		for (int i = 0; i < triggerGEs.length; i++)
		{
			if (triggerSOs[i] == null)
			{
				triggerSOs[i] = ge;
				return;
			}
		}
	}
	
	public void add(TriggerStartTrigger ge)
	{
		for (int i = 0; i < triggerGEs.length; i++)
		{
			if (triggerSTAs[i] == null)
			{
				triggerSTAs[i] = ge;
				return;
			}
		}
	}
	
	public void add(TriggerStopTrigger ge)
	{
		for (int i = 0; i < triggerGEs.length; i++)
		{
			if (triggerSTOs[i] == null)
			{
				triggerSTOs[i] = ge;
				return;
			}
		}
	}
	
	public void add(Entity ge)
	{
		for (int i = 0; i < triggerGEs.length; i++)
		{
			if (entities[i] == null)
			{
				entities[i] = ge;
				return;
			}
		}
	}
}
