package core;

public class MapReturn
{
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	public static final int WARNING = 2;
	public static final int OTHER = 3;
	
	private String message;
	private int messageType;
	
	public MapReturn(String message, int messageType)
	{
		this.message = message;
		this.messageType = messageType;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public int getMessageType()
	{
		return messageType;
	}
}
