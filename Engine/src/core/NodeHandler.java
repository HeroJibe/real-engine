package core;

public class NodeHandler 
{
	private Node[] nodes;
	
	public NodeHandler(int maxNodes)
	{
		nodes = new Node[maxNodes];
	}
	
	public void addNode(Node node)
	{
		for (int i = 0; i < nodes.length; i++)
		{
			if (nodes[i] == null)
			{
				nodes[i] = node;
				return;
			}
		}
	}
	
	public void clearAll()
	{
		for (int i = 0; i < nodes.length; i++)
		{
			nodes[i] = null;
		}
	}
	
	public Node[] getNodes()
	{
		return nodes;
	}
	
	public Node getNode(String name)
	{
		if (name == null)
			return null;
		for (int i = 0; i < nodes.length; i++)
		{
			if (nodes[i] != null)
				if (nodes[i].getName() != null)
					if (nodes[i].getName().equals(name))
						return nodes[i];
		}
		return null;
	}
}
