/**
 * The ParticleArguments class contains the information
 * for a particle effect to be run.
 * 
 * @author Ethan Vrhel
 */

package core;

public class ParticleArguments 
{
	private double x;
	private double y;
	private int z;
	private int w;
	private int h;
	private double sXVel;
	private double sYVel;
	private double friction;
	private double gravity;
	private boolean solid;
	private int rate;
	private int life;
	private GameSound destroySound;
	private int maxParticles;
	private boolean useGravity;
	private SpatialRelation relation;
	private StopType type;
	
	public enum SpatialRelation 
	{
		BOX, CENTER
	};
	
	public enum StopType
	{
		TIME, COLLISION
	};
	
	public ParticleArguments(double x, double y, int z, int w, int h, double sXVel, double sYVel, double friction, double gravity, boolean solid, int rate, int life, GameSound destroy, int maxParticles, boolean useGravity, SpatialRelation relation, StopType type)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.h = h;
		this.sXVel = sXVel;
		this.sYVel = sYVel;
		this.friction = friction;
		this.gravity = gravity;
		this.solid = solid;
		this.rate = rate;
		this.life = life;
		this.destroySound = destroy;
		this.maxParticles = maxParticles;
		this.useGravity = useGravity;
		this.relation = relation;
		this.type = type;
	}
	
	public ParticleArguments(ParticleArguments orig)
	{
		this.x = orig.getX();
		this.y = orig.getY();
		this.z = orig.getZ();
		this.w = orig.getWidth();
		this.h = orig.getHeight();
		this.sXVel = orig.getSXVel();
		this.sYVel = orig.getSYVel();
		this.friction = orig.getFriction();
		this.gravity = orig.getGravity();
		this.solid = orig.getSolid();
		this.rate = orig.getRate();
		this.life = orig.getLife();
		this.destroySound = orig.getSound();
		this.maxParticles = orig.getMaxParticles();
		this.useGravity = orig.useGravity();
		this.relation = orig.getSpatialRelation();
		this.type = orig.getStopType();
	}
	
	/**
	 * Sets the particle effect's x position
	 * 
	 * @param x
	 */
	public void setX(double x)
	{
		this.x = x;
	}
	
	/**
	 * Sets the particle effect's y position
	 * 
	 * @param y
	 */
	public void setY(double y)
	{
		this.y = y;
	}
	
	/**
	 * Returns the particle effect's x position
	 * 
	 * @return
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * Returns the particle effect's y position
	 * 
	 * @return
	 */
	public double getY()
	{
		return y;
	}
	
	/**
	 * Returns the particle effect's z buffer
	 * 
	 * @return
	 */
	public int getZ()
	{
		return z;
	}
	
	/**
	 * Returns the particle effect's width
	 * 
	 * @return
	 */
	public int getWidth()
	{
		return w;
	}
	
	/**
	 * Returns the particle effect's height
	 * 
	 * @return
	 */
	public int getHeight()
	{
		return h;
	}
	
	/**
	 * Returns the particle effect's starting x
	 * velocity
	 * 
	 * @return
	 */
	public double getSXVel()
	{
		return sXVel;
	}
	
	/**
	 * Returns the particle effect's starting y
	 * velocity
	 * 
	 * @return
	 */
	public double getSYVel()
	{
		return sYVel;
	}
	
	/**
	 * Returns the particle effect's friction
	 * 
	 * @return
	 */
	public double getFriction()
	{
		return friction;
	}
	
	/**
	 * Returns the particle effect's gravity
	 * 
	 * @return
	 */
	public double getGravity()
	{
		return gravity;
	}
	
	/**
	 * Returns whether the particle effect is
	 * solid
	 * 
	 * @return
	 */
	public boolean getSolid()
	{
		return solid;
	}
	
	/**
	 * Returns the particle effect's rate
	 * 
	 * @return
	 */
	public int getRate()
	{
		return rate;
	}
	
	/**
	 * Returns the particle effect's maximum
	 * lifetime in milliseconds
	 * 
	 * @return
	 */
	public int getLife()
	{
		return life;
	}
	
	/**
	 * Returns the particle effect's destroy sound
	 * 
	 * @return The particle effect's destroy sound
	 */
	public GameSound getSound()
	{
		return destroySound;
	}
	
	/**
	 * Returns the maximum number of particles
	 * 
	 * @return
	 */
	public int getMaxParticles()
	{
		return maxParticles;
	}
	
	/**
	 * Whether the particle uses gravity
	 * 
	 * @return
	 */
	public boolean useGravity()
	{
		return useGravity;
	}
	
	/**
	 * Returns the particle's spatial relation
	 * 
	 * @return
	 */
	public SpatialRelation getSpatialRelation()
	{
		return relation;
	}
	
	/**
	 * Returns the particle's stop type
	 * @return
	 */
	public StopType getStopType()
	{
		return type;
	}
}
