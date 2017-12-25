package core;

/**
 * The GameRunnable interface is the
 * base for virtual game threads
 * 
 * @author Ethan Vrhel
 * @see GameThread
 */
public interface GameRunnable
{
	
	/**
	 * Called by the <code>GameThreadHandler</code> or the
	 * <code>DelayedGameThreadHandler</code> to execute
	 * the contents of the class implementing the
	 * <code>GameRunnable</code> interface
	 */
	public void onGameUpdate();
	
	/**
	 * Called through the <code>init();</code> method in
	 * the <code>GameThreadHandler</code> and the
	 * <code>DelayedGameThreadHandler</code>
	 */
	public void onGameInit();
}