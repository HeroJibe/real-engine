package core;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import gui.GameWindow;

/**
 * The <code>MouseInputListener</code> class
 * listens for mouse input
 * 
 * @author Ethan Vrhel
 * @see InputListener
 */
public class MouseInputListener 
	implements MouseListener
{
	public int mouseX;
	public int mouseY;
	public boolean actions[] = new boolean[5];
	
	/**
	 * Represents actions caused by the mouse
	 * 
	 * @author Ethan Vrhel
	 */
	public enum Action
	{
		MOUSE_CLICKED, MOUSE_ENTERED, MOUSE_EXITED, MOUSE_PRESSED, MOUSE_RELEASED
	};
	
	public MouseInputListener(GameWindow window)
	{
		window.addMouseListener(this);
		actions[0] = false;
		actions[1] = false;
		actions[2] = false;
		actions[3] = false;
		actions[4] = false;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		actions[0] = true;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		actions[1] = true;
		actions[2] = false;
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		actions[1] = false;
		actions[2] = true;
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		actions[3] = true;
		actions[4] = false;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		actions[3] = false;
		actions[4] = true;
	}
	
	/**
	 * Returns whether the mouse is doing an
	 * <code>Action</code>
	 * @param a An <code>Action</code>
	 * @return Whether the mouse is doing the 
	 * respective <code>Action</code>
	 */
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
