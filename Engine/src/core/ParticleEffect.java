/**
 * The ParticleEffect class allows for particle
 * effects in game.
 * 
 * @author Ethan Vrhel
 */

package core;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.Main;

public class ParticleEffect 
{
	private String name;
	private BufferedImage buffimg;
	private ParticleArguments arg;
	private int maxParticles;
	private Particle[] particles;
	private boolean shouldRun;
	
	private long tick;
	
	public ParticleEffect(String name, Image img, ParticleArguments arg)
	{
		this.name = name;
		maxParticles = arg.getMaxParticles();
		particles = new Particle[maxParticles];
		buffimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffimg.getGraphics();
		g.drawImage(img, 0, 0, null);
		this.arg = arg;
		shouldRun = false;
		tick = 0;
	}
	
	public ParticleEffect(ParticleEffect orig)
	{
		ParticleArguments arg = new ParticleArguments(orig.getParticleArguments());
		name = orig.getName();
		maxParticles = arg.getMaxParticles();
		particles = new Particle[maxParticles];
		buffimg = orig.getImage();
		this.arg = arg;
	}
	
	/**
	 * Returns the particle effect's image
	 * 
	 * @return
	 */
	public BufferedImage getImage()
	{
		return buffimg;
	}
	
	/**
	 * Updates the particle effect
	 */
	public void update()
	{
		if (Main.gamePaused && ! Main.isMultiplayer)
			return;
		
		if (! shouldRun || Main.getParticleEffectHandler().particleRateDivider == -1)
			return;
		
		tick++;
		if (tick >= arg.getRate() * Main.getParticleEffectHandler().particleRateDivider)
		{
			//Main.println("add");
			if (getCurrentParticles() < arg.getMaxParticles())
			{
				addParticle();
				//Main.println("added");
				tick = 0;
			}
		}
		
		for (int i = 0; i < particles.length; i++)
		{
			if (particles[i] != null)
			{
				particles[i].tick();
			}
		}
	}
	
	/**
	 * Returns the current number of particles
	 * 
	 * @return
	 */
	public int getCurrentParticles()
	{
		int numParticles = 0;
		for (int i = 0; i < particles.length; i++)
		{
			if (particles[i] != null)
			{
				if (particles[i].isAlive())
					numParticles++;
			}
		}
		return numParticles;
	}
	
	/**
	 * Returns the ParticleArguments
	 * 
	 * @return
	 */
	public ParticleArguments getParticleArguments()
	{
		return arg;
	}
	
	private void addParticle()
	{
		int slot = 0;
		for (int i = 0; i < particles.length; i++)
		{
			if (particles[i] == null)
			{
				slot = i;
				break;
			}
			else
			{
				if (! particles[i].isAlive())
				{
					slot = i;
					break;
				}
			}
		}
		//Main.println("added: " + slot);
		double x = arg.getX();
		double y = arg.getY();
		if (arg.getSpatialRelation() == ParticleArguments.SpatialRelation.BOX)
		{
			Random r = new Random();
			int transX = r.nextInt(arg.getWidth());
			int transY = r.nextInt(arg.getHeight());
			x += transX;
			y += transY;
		}
		Entity e = new Entity(Entity.DYNAMIC, buffimg, buffimg.getWidth(), buffimg.getHeight(), false, x, y, arg.getZ(), 100, false);
		e.setKinetic(arg.getSolid());
		//Main.println("added at " + slot + "(" + e + ")");
		Main.getEntityHandler().addDynamicEntity(e);
		Particle particle = new Particle(e, arg);
		particles[slot] = particle;
	}
	
	/**
	 * Whether the particle effect should run
	 * 
	 * @param shouldRun
	 */
	public void shouldRun(boolean shouldRun)
	{
		this.shouldRun = shouldRun;
	}
	
	/**
	 * Returns the particle effect by its name
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}
}
