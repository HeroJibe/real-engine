package core;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import gui.GameWindow;
import main.Main;

public class MouseInputListener 
	implements MouseListener
{
	public boolean actions[] = {false, false, false, false, false};
	
	public enum Action
	{
		MOUSE_CLICKED, MOUSE_ENTERED, MOUSE_EXITED, MOUSE_PRESSED, MOUSE_RELEASED
	};
	
	public MouseInputListener(GameWindow window)
	{
		window.addMouseListener(this);
	}

	public void mouseClicked(MouseEvent arg0) 
	{
		actions[0] = true;
	}

	public void mouseEntered(MouseEvent arg0) 
	{
		actions[1] = true;
		actions[2] = false;
	}

	public void mouseExited(MouseEvent arg0) 
	{
		actions[1] = false;
		actions[2] = true;
	}

	public void mousePressed(MouseEvent arg0)
	{
		actions[3] = true;
		actions[4] = false;
	}

	public void mouseReleased(MouseEvent arg0) 
	{
		actions[3] = false;
		actions[4] = true;
	}
	
	public boolean isMouse(Action a)
	{
		if (a == Action.MOUSE_CLICKED)
		{
			if (actions[0])
			{
				actions[0] = false;
				return true;
			}
			return actions[0];
		}
		
		if (a == Action.MOUSE_ENTERED)
			return actions[1];
		
		if (a == Action.MOUSE_EXITED)
			return actions[2];
		
		if (a == Action.MOUSE_PRESSED)
			return actions[3];
		
		if (a == Action.MOUSE_RELEASED)
			return actions[4];
		
		return false;
	}
}
