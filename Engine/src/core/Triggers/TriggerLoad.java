package core.Triggers;

import core.Entity;
import core.Trigger;
import main.Main;

public class TriggerLoad 
	extends Trigger
{
	private String mapToLoad;
	private double xbias;
	private double ybias;
	
	public TriggerLoad(Entity triggerEntity, String name, String map, double xbias, double ybias)
	{
		super(triggerEntity, name);
		mapToLoad = map;
		this.xbias = xbias;
		this.ybias = ybias;
	}
	
	public void onGameUpdate() 
	{

	}

	public void onGameInit() 
	{

	}
	
	public void onTouch(Entity entity)
	{
		int exitCode = Main.loadMap(mapToLoad, xbias, ybias);
		if (exitCode == 1)
		{
			Main.getPlayer().getPlayerEntity().setX(Main.player_x + (xbias * Main.resolutionScaleX));
			Main.getPlayer().getPlayerEntity().setY(Main.player_y + (ybias * Main.resolutionScaleY));
			Main.loadingMap = false;
		}
		if (exitCode == 0)
		{
			Main.loadMessage = ("Error: exception while loading map: no map loaded");
		}
		else if (exitCode == -1)
		{
			Main.loadMessage = ("Error: unknown exception while loading map: check console");
		}
	}
	
	public void onStop() {}
}
