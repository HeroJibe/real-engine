package net.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import core.Entity;
import main.Main;
import net.NetMain;

public class RemoteClient 
	implements Runnable
{
	private static int nextID = 0;
	
	private ServerMain server;
	private boolean connected;
	private Socket client;
	private int ID;
	private int globalID;
	private String serverName;
	private PrintWriter out;
	private BufferedReader in;
	private String clientVersion;
	
	private String clientName;
	
	public RemoteClient(Socket socket, ServerMain server)
	{
		connected = false;
		this.server = server;
		try
		{
			client = socket;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			server.getRemoteClientHandler().addClient(this);
			ID = NetMain.serverMain.getRemoteClientHandler().getID(this);
			globalID = nextID;
			nextID++;
		}
		catch (IOException e)
		{
			NetMain.println("Failed creating the remote client.  (" + e.getMessage() + ")");
			server.settingUp = false;
			return;
		}
		catch (CannotAcceptException e)
		{
			NetMain.println("Could not process the client.  (" + e.getMessage() + ")");
			server.settingUp = false;
			return;
		}
	}
	
	public void run()
	{
		try
		{
			send("confirm connected");
			
			send("request version");
			clientVersion = in.readLine();
			if (! clientVersion.equals(server.getVersion()))
			{
				send("info denyConnection version");
				disconnect();
				server.settingUp = false;
				return;
			}
			
			send("request name");
			clientName = in.readLine();
			serverName = clientName + ":" + ID + ":" + globalID;
			
			send("confirm recieved");
			send("info map " + server.getMapName());
			send("reqeust map");
			String curr = in.readLine();
			File map = new File("maps\\" + NetMain.serverMain.getMapName() + ".map");
			@SuppressWarnings("resource")
			Scanner fileIn = new Scanner(map);
			while (! curr.equals("file end"))
			{
				curr = in.readLine();
				if (! curr.equals("file end"))
				{
					String line = fileIn.nextLine();
					if (line == null)
					{
						send("info denyConnection map");
						disconnect();
						server.settingUp = false;
						return;
						
					}
					else if (! curr.equals(line))
					{
						send("info denyConnection map");
						disconnect();
						server.settingUp = false;
						return;
					}
				}
			}
			fileIn.close();
			send("confirm recieved");
			send("confirm accepted");
		}
		catch (Exception e)
		{
			NetMain.println("Error connecting " + e.getMessage());
			try 
			{
				disconnect();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			server.settingUp = false;
			return;
		}
		
		server.settingUp = false;
		server.getRemoteClientHandler().send("say " + clientName + " has joined the game.");
		connected = true;		
		while (connected)
		{
			try
			{
				String message = in.readLine();
				String[] tokens = message.split(":");
				if (tokens[0].equals("disconnect"))
				{
					server.getRemoteClientHandler().send("say " + clientName + " has left the game.");
					disconnect();
				}
				else if (tokens[0].equals("say"))
				{
					server.getRemoteClientHandler().send("say <" + clientName + "> " + tokens[1]);
				}
				else if (tokens[0].equals("entity"))
				{
					try
					{
						if (tokens[1].equals("move"))
						{
							String entityName = tokens[2];
							Entity e = Main.getEntityHandler().getEntityByName(entityName);
							e.translateX(Double.parseDouble(tokens[3]));
							e.translateY(Double.parseDouble(tokens[4]));
							server.getRemoteClientHandler().send("entity set " + entityName + 
									" " + e.getActualX() + " " + e.getActualY());
						}
						else if (tokens[2].equals("set"))
						{
							String entityName = tokens[2];
							Entity e = Main.getEntityHandler().getEntityByName(entityName);
							e.setX(Double.parseDouble(tokens[3]));
							e.setY(Double.parseDouble(tokens[4]));
							server.getRemoteClientHandler().send("entity set " + entityName + 
									" " + e.getActualX() + " " + e.getActualY());
						}
						else if (tokens[2].equals("send"))
						{
							
						}
					}
					catch (NullPointerException e)
					{
						send("info invalid entity name");
					}
				}
			}
			catch (Exception e)
			{
				send("info bad io");
			}
		}
	}
	
	public void send(String str)
	{
		out.println(str);
	}
	
	public void disconnect() throws IOException
	{
		connected = false;
		client.close();
	}
	
	public boolean connected()
	{
		return connected;
	}
	
	public String getName()
	{
		return serverName;
	}
	
	public String getClientName()
	{
		return clientName;
	}
	
	public InetAddress getClientAddress()
	{
		return client.getInetAddress();
	}
	
	public int getID()
	{
		return ID;
	}
	
	public int getGlobalID()
	{
		return globalID;
	}
}
