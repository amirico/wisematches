package wisematches.playground.timer.impl;

import org.springframework.core.task.TaskExecutor;
import wisematches.playground.timer.BreakingDayListener;
import wisematches.playground.timer.BreakingIdleListener;
import wisematches.playground.timer.TimeDispatcher;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultTimeDispatcher implements TimeDispatcher {
	private TaskExecutor taskExecutor;

	private final Set<BreakingDayListener> dayListeners = new CopyOnWriteArraySet<BreakingDayListener>();
	private final Set<BreakingIdleListener> idleListeners = new CopyOnWriteArraySet<BreakingIdleListener>();

	public DefaultTimeDispatcher() {
	}

	@Override
	public void addBreakingDayListener(BreakingDayListener l) {
		if (l != null) {
			dayListeners.add(l);
		}
	}

	@Override
	public void removeBreakingDayListener(BreakingDayListener l) {
		dayListeners.remove(l);
	}

	@Override
	public void addBreakingIdleListener(BreakingIdleListener l) {
		if (l != null) {
			idleListeners.add(l);
		}
	}

	@Override
	public void removeBreakingIdleListener(BreakingIdleListener l) {
		idleListeners.remove(l);
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
	}
}
