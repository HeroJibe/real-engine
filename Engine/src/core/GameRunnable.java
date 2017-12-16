/**
 * The GameRunnable interface is the
 * base for virtual game threads
 * 
 * @author Ethan Vrhel
 */

package core;

public interface GameRunnable
{
	public void onGameUpdate();
	public void onGameInit();
}