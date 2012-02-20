package wisematches.playground.timer;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TimeDispatcher {
	void addBreakingDayListener(BreakingDayListener l);

	void removeBreakingDayListener(BreakingDayListener l);


	void addBreakingIdleListener(BreakingIdleListener l);

	void removeBreakingIdleListener(BreakingIdleListener l);
}
