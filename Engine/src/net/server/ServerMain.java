package net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.Main;
import net.NetMain;

public class ServerMain 
	implements Runnable
{
	private short port;
	private ServerSocket server;
	private Socket socket;
	private boolean run;
	private RemoteClientHandler clientHandler;
	private String serverVersion;
	private String mapName;
	public volatile boolean settingUp;
	
	public ServerMain(short port)
	{
		this.port = port;
		run = false;
		mapName = "test";
		serverVersion = Main.ENGINE_VERSION_NAME;
	}
	
	public void run()
	{
		try
		{
			server = new ServerSocket(port);
		}
		catch (Exception e)
		{
			NetMain.println("Error creating server socket: " + e.getCause() + "(" + e.getMessage() + ")");
			return;
		}
		
		while (! run)
		{
			Thread.yield();
		}
		NetMain.println("Server started on port: " + port);
		
		while (run)
		{
			try
			{
				settingUp = true;
				socket = server.accept();
				RemoteClient cl = new RemoteClient(socket, this);
				(new Thread(cl)).start();
				while (settingUp)
					Thread.yield();
			}
			catch (IOException e)
			{
				NetMain.println("A connection was made, but it failed. (" + e.getCause() + ")");
			}
			catch (Exception e)
			{
				NetMain.println("An unhandled exception occured: " + e.getCause());
			}
			settingUp = false;
		}
	}
	
	public void start()
	{
		NetMain.println("Attempting to start server...");
		run = true;
	}
	
	public void stop()
	{
		NetMain.println("Attempting to stop server...");
		//for (int i = 0; i < )
		run = false;
	}
	
	public RemoteClientHandler getRemoteClientHandler()
	{
		return clientHandler;
	}
	
	public void setMaxUsers(int maxUsers)
	{
		if (maxUsers <= 0)
		{
			NetMain.println("maxUsers must be > 0");
			return;
		}
		
		if (! run) {
		} else
			NetMain.println("Server is already running!");
	}
	
	public String getMapName()
	{
		return mapName;
	}
	
	public String getVersion()
	{
		return serverVersion;
	}
}
