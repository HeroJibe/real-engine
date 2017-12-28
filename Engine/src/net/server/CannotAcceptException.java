package net.server;

public class CannotAcceptException 
	extends Exception
{
	private static final long serialVersionUID = 411817066209934849L;
	
	CannotAcceptException()
	{
		super();
	}
	
	CannotAcceptException(String msg)
	{
		super(msg);
	}
}