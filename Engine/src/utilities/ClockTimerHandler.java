package utilities;

public class ClockTimerHandler 
{
	private ClockTimer timers[];
	private int maxTimers;
	
	public ClockTimerHandler(int maxTimers)
	{
		timers = new ClockTimer[maxTimers];
		this.maxTimers = maxTimers;
	}
	
	public void addTimer(ClockTimer timer)
	{
		for (int i = 0; i < maxTimers; i++)
		{
			if (timers[i] == null)
			{
				timers[i] = timer;
				return;
			}
		}
	}
	
	public void stopAll()
	{
		for (int i = 0; i < maxTimers; i++)
		{
			if (timers[i] != null)
			{
				timers[i].stopTimer();
				timers[i] = null;
			}
		}
	}
}
