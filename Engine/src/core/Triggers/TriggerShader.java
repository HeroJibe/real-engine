package core.Triggers;

import core.Entity;
import core.Shader;
import core.Trigger;
import main.Main;

public class TriggerShader 
	extends Trigger
{
	private Shader shader;
	
	public TriggerShader(Entity triggerEntity, String name, String shaderName) 	
	{
		super(triggerEntity, name);
		shader = Main.getShaderHandler().getByName(shaderName);
	}
	
	public void onGameUpdate() 
	{

	}

	public void onGameInit() 
	{

	}

	public void onTouch(Entity triggerEntity)
	{
		if (shader != null)
			shader.enabled(true);
	}

	public void onStop() 
	{
		if (shader != null)
			shader.enabled(false);
	}
	
	public Shader getShader()
	{
		return shader;
	}
	
	public void setShader(Shader shader)
	{
		this.shader = shader;
	}
}
