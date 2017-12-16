package core;

import java.awt.Color;

import main.Main;

public class ParticleEffectHandler 
	implements GameRunnable
{
	public int particleRateDivider = 1;
	
	private ParticleEffect[] particleEffects;
	private ParticleEffect[] particleBuffer;
	private int maxParticleEffects;
	
	public ParticleEffectHandler(int maxParticleEffects)
	{
		particleEffects = new ParticleEffect[maxParticleEffects];
		particleBuffer = new ParticleEffect[maxParticleEffects];
		this.maxParticleEffects = maxParticleEffects;
	}
	
	public void onGameInit() {}

	public void onGameUpdate() 
	{
		//Main.println("Particle effect thread started");
		//while (true)
		//{
				for (int i = 0; i < maxParticleEffects; i++)
				{
					if (particleEffects[i] != null)
					{
						particleEffects[i].update();
					}
				}
				
				/*
				try
				{
					Thread.sleep(1);
				}
				catch (Exception e)
				{
					//break;
				}
				*/
				
			//}
		//}
		//Main.println("Particle effect handler stopped.", Color.RED);
	}
	
	public void addParticleEffect(ParticleEffect effect)
	{
		for (int i = 0; i < maxParticleEffects; i++)
		{
			if (particleEffects[i] == null)
			{
				particleEffects[i] = effect;
				return;
			}
		}
		Main.println("Error adding particle effect.  Particle effect cache not big enough.", Color.RED);
	}
	
	public void addToBuffer(ParticleEffect effect)
	{
		for (int i = 0; i < maxParticleEffects; i++)
		{
			if (particleBuffer[i] == null)
			{
				particleBuffer[i] = effect;
				return;
			}
		}
		Main.println("Error adding particle effect.  Particle effect cache not big enough.", Color.RED);
	}
	
	public int getNumParticles()
	{
		int num = 0;
		for (int i = 0; i < maxParticleEffects; i++)
		{
			if (particleEffects[i] != null)
			{
				num += particleEffects[i].getCurrentParticles();
			}
		}
		return num;
	}
	
	public ParticleEffect getByName(String name)
	{
		for (int i = 0; i < maxParticleEffects; i++)
		{
			if (particleEffects[i] != null)
			{
				if (particleEffects[i].getName().equals(name))
				{
					return particleEffects[i];
				}
			}
		}
		return null;
	}
	
	public ParticleEffect getFromBuffer(String name)
	{
		for (int i = 0; i < maxParticleEffects; i++)
		{
			if (particleBuffer[i] != null)
			{
				if (particleBuffer[i].getName().equals(name))
				{
					return particleBuffer[i];
				}
			}
		}
		return null;
	}
	
	public void removeAll()
	{
		for (int i = 0; i < maxParticleEffects; i++)
		{
			particleEffects[i] = null;
		}
	}
}
