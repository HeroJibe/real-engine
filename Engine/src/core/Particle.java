/**
 * The Particle class is the visible part of
 * particle effects.
 * 
 * @author Ethan Vrhel
 */

package core;

import main.Main;

public class Particle 
{
	private Entity e;
	private ParticleArguments arg;
	private int life;
	private boolean alive;
	
	public Particle(Entity e, ParticleArguments arg)
	{
		e.setKinetic(arg.useGravity());
		e.setXVel(arg.getSXVel());
		e.setYVel(arg.getSYVel());
		e.setFriction(arg.getFriction());
		e.setZBuffer(arg.getZ());
		e.renderInReflections(false);
		if (arg.getGravity() != -1)
		{
			e.setGravity(arg.getGravity());
			e.updateWithPhysics(false);
		}
		this.e = e;
		this.arg = arg;
		life = 0;
		alive = true;
	}
	
	/**
	 * Updates the particle
	 */
	public void tick()
	{
		if (alive)
		{
			life++;
			boolean collided = false;
			if (arg.getStopType() == ParticleArguments.StopType.COLLISION)
			{
				e.setSolid(true);
				collided = e.hasCollided();
				e.setSolid(false);
			}
			
			if (life >= arg.getLife() || collided)
			{
				alive = false;
				if (arg.getSound() != null)
				{
					arg.getSound().setSource(new SoundSource(e.getActualX(), e.getActualY()));
					arg.getSound().playSound();
				}
				Main.getEntityHandler().removeEntity(e);
				//e = null;
			}
		}
	}
	
	/**
	 * Whether the particle is alive
	 * 
	 * @return
	 */
	public boolean isAlive()
	{
		return alive;
	}
}
