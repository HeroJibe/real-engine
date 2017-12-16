package net.server;

public class RemoteClientHandler 
{	
	private RemoteClient[] remoteClients;
	private int maxClients;
	
	public RemoteClientHandler(int maxClients)
	{
		remoteClients = new RemoteClient[maxClients];
		this.maxClients = maxClients;
	}
	
	public int addClient(RemoteClient client) throws CannotAcceptException
	{
		for (int i = 0; i < maxClients; i++)
		{
			if (remoteClients[i] != null)
			{
				if (! remoteClients[i].connected())
				{
					remoteClients[i] = client;
					return i;
				}
			}
		}
		throw new CannotAcceptException("Maximum number of users exceeded.");
	}
	
	public RemoteClient getByName(String name)
	{
		if (name == null)
			return null;
		
		for (int i = 0; i < maxClients; i++)
		{
			if (remoteClients[i] != null)
			{
				if (name.equals(remoteClients[i].getName()))
				{
					return remoteClients[i];
				}
			}
		}
		
		return null;
	}
	
	public void send(String msg)
	{
		if (msg == null)
			return;
		
		for (int i = 0; i < maxClients; i++)
		{
			if (remoteClients[i] != null)
			{
				if (remoteClients[i].connected())
					remoteClients[i].send(msg);
			}
		}
	}
	
	public RemoteClient update()
	{		
		for (int i = 0; i < maxClients; i++)
		{
			if (remoteClients[i] != null)
			{

			}
		}
		
		return null;
	}
	
	public RemoteClient getByID(int id)
	{
		return remoteClients[id];
	}
	
	public int getID(RemoteClient client)
	{
		if (client == null)
			return -1;
		
		for (int i = 0; i < maxClients; i++)
		{
			if (remoteClients[i] == client)
			{
				return i;
			}
		}
		return -1;
	}
}
