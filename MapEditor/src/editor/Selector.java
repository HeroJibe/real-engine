package editor;

import java.awt.Color;

import core.Entity;
import core.EntityHandler;
import core.MouseInputListener;
import core.MouseInputListener.Action;
import main.Main;

public class Selector
	implements Runnable
{
	private EntityHandler entityHandler;
	private MouseInputListener mouseListener;
	
	public Selector()
	{
		entityHandler = Main.getEntityHandler();
		mouseListener = Main.getMouseListener();
	}

	public void run()
	{
		while (true)
		{
			if (mouseListener.isMouse(Action.MOUSE_PRESSED))
			{
				Main.println("clicked", Color.CYAN);
				Entity entities[] = entityHandler.getEntities();
				for (int i = 0; i < entities.length; i++)
				{
					if (entities[i] != null)
					{
						if (entities[i].hasPointCollided(mouseListener.getMouseX(), mouseListener.getMouseY()))
						{
							entities[i].setSelected(true);
						}
						else
						{
							entities[i].setSelected(false);
						}
					}
				}
			}
		}
	}
}
