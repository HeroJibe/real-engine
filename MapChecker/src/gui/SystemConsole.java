package gui;

import java.io.BufferedReader;
import java.util.Scanner;

import main.Main;

public class SystemConsole 
	implements Runnable
{
	private Scanner in;
	
	public SystemConsole()
	{
		in = new Scanner(System.in);
	}
	
	public void run()
	{
		while (true)
		{
			String cmd = in.nextLine();
			Main.execute(cmd);
		}
	}
}
