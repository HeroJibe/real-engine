package core;

import java.awt.Color;

import main.Main;

public class NodeTranslator 
	implements Runnable
{
	public static final int X_PLANE = 0;
	public static final int Y_PLANE = -1;
	
	public static final int NO_MOVE = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int UP = 3;
	public static final int DOWN = 4;
	
	private Entity e;
	private double move;
	private int type;
	private Node node;
	private Node startNode;
	private boolean updated;
	private Thread myThread;
	private boolean started;
	private int initmove;
	private int rate;
	private boolean reachedDestination;
	private NodeTranslator next;
	
	public NodeTranslator(Entity e, double move, int type, int delay, Node startNode, Node node, NodeTranslator next)
	{
		this.e = e;
		this.move = move;
		this.next = next;
		this.node = node;
		this.startNode = startNode;
		this.type = type;
		this.rate = delay;
		updated = false;
		initmove = NO_MOVE;
		started = false;
		reachedDestination = false;
		myThread = new Thread(this);
		if (e == null)
		{
			Main.println("WARNING: translation entity is null", Color.YELLOW);
			System.out.println("WARNING: translation entity is null");
		}
		if (node == null)
		{
			Main.println("WARNING: Node is null", Color.YELLOW);
			System.out.println("WARNING: Node is null");
		}
	}

	@Override
	public void run() 
	{
		while (true)
		{
			if (! updated)
			{			
				updated = true;
				if (type == X_PLANE)
				{
					e.setY(startNode.getY());
					if (e.getX() < node.getX())
						initmove = RIGHT;
					else if (e.getX() > node.getX())
						initmove = LEFT;
				}
				else if (type == Y_PLANE)
				{
					e.setX(startNode.getX());
					if (e.getY() < node.getY())
						initmove = DOWN;
					else if (e.getY() > node.getY())
						initmove = UP;
				}
			}
			
			e.setXVel(0);
			e.setYVel(0);
			
			if (type == X_PLANE)
			{
				if (initmove == RIGHT)
				{
					e.translateX(move);
					e.setY(startNode.getY());
					if (e.getActualX() > node.getX())
						initmove = NO_MOVE;
				}
				else if (initmove == LEFT)
				{
					e.translateX(-1 * move);
					e.setY(startNode.getY());
					if (e.getActualX() < node.getX())
						initmove = NO_MOVE;
				}
			}
			else if (type == Y_PLANE)
			{
				if (initmove == UP)
				{
					e.translateY(-1 * move);
					e.setX(startNode.getX());
					if (e.getActualY() < node.getY())
						initmove = NO_MOVE;
				}
				else if (initmove == DOWN)
				{
					e.translateY(move);
					e.setX(startNode.getX());
					if (e.getActualY() > node.getY())
						initmove = NO_MOVE;
				}
			}
			
			//System.out.println(e.toString());
			//System.out.println("camera " + Main.getGameWindow().cameraX + " " + Main.getGameWindow().cameraY);
			
			if (initmove == NO_MOVE)
			{
				e.setX(node.getX());
				e.setY(node.getY());
				reachedDestination = true;
				if (next != null)
				{
					next.start();
				}
				started = false;
				return;
			}
			try
			{
				Thread.sleep(rate);
			}
			catch (Exception e) {}
		}
	}
	
	public boolean reachedDestination()
	{
		return reachedDestination;
	}
	
	public void start()
	{
		myThread.start();
	}
	
	/**
	 * @deprecated
	 */
	public void stop()
	{
		myThread.stop();
	}
	
	public boolean started()
	{
		return started;
	}
	
	public String toString()
	{
		return "NodeTranslator: " + startNode + " to " + node;
	}
}
