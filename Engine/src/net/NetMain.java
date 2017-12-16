package net;

import net.core.CommandHandler;
import net.server.ServerConsole;
import net.server.ServerMain;

public class NetMain 
{
	public static CommandHandler commandHandler = new CommandHandler();
	public static ServerMain serverMain;
	public static ServerConsole serverConsole;
	private static boolean server;
	
	public static void setup()
	{
		commandHandler = new CommandHandler();
	}
	
	public static void setup(short port)
	{
		commandHandler = new CommandHandler();
		server = true;
		serverConsole = new ServerConsole();
		serverMain = new ServerMain(port);
	}
	
	public static void print(String str)
	{
		if (server)
		{
			serverConsole.print(str);
		}
	}
	
	public static void println(String str)
	{
		if (server)
		{
			serverConsole.println(str);
		}
	}
	
	public static boolean isServer()
	{
		return server;
	}
}
