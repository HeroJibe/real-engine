package core;

import main.Main;

public class TypingHandler 
	implements Runnable
{
	public static final int KEY_UPDATE_RATE = 0;
	
	private InputListener listener;
	private boolean isKeyTyped;
	private String keyTyped;
	private String[] keysTyped;
	private boolean reset = true;
	
	public TypingHandler(InputListener listener)
	{
		this.listener = listener;
	}
	
	public void run()
	{
		int loops = 0;
		while (true)
		{
			if (listener.wasKeyTyped())
			{
				isKeyTyped = true;
				keysTyped = listener.getKeysPressed();
				keyTyped = listener.getKeyPressed();
			}
			
			
			while (isKeyTyped) {System.out.print("");}		
		}
	}
	
	public boolean wasKeyTyped()
	{
		return isKeyTyped;
	}
	
	public String keyTyped()
	{
		String returned = keyTyped;
		
		if (reset)
		{
			keysTyped = null;
			isKeyTyped = false;
			keyTyped = null;
		}
		
		return returned;
	}
	
	public String[] keysTyped()
	{
		String[] returned = keysTyped;
		if (reset)
		{
			keysTyped = null;
			isKeyTyped = false;
			keyTyped = null;
		}
		return returned;
	}
}
