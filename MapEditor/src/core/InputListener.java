package core;

import java.util.Scanner;

import main.Main;

public class InputListener
	implements Runnable
{
	private Scanner in;
	private boolean wait;
	private boolean waiting;
	private String inp;
	
	public InputListener()
	{
		in = new Scanner(System.in);
		inp = "";
		wait = true;
	}
	
	public void run()
	{
		waiting = false;
		while (true)
		{
			if (! wait)
			{
				waiting = true;
				System.out.print("> ");
				inp = in.nextLine();
				try
				{
					Main.getCommandHandler().execute(inp);
				}
				catch (IllegalArgumentException e)
				{
					System.out.println(e.getMessage());
				}
				waiting = false;
				
			}
			else
				Thread.yield();
		}
	}
	
	public synchronized void disableInput()
	{
		while (waiting)
			Thread.yield();
		wait = true;
	}
	
	public synchronized void enableInput()
	{
		while (waiting)
			Thread.yield();
		wait = false;
	}
	
	public String getInput()
	{
		return inp;
	}
}
