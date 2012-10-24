package wisematches.playground.scheduling.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;
import wisematches.playground.scheduling.BreakingDayListener;

import java.util.Date;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SpringBreakingDayExecutorJob implements Job {
	public SpringBreakingDayExecutorJob() {
	}

	@Override
	public void execute(JobExecutionContext context) {
		final Date scheduledFireTime = context.getScheduledFireTime();
		final ApplicationContext applicationContext = (ApplicationContext) context.get("SpringApplicationContext");
		final Map<String, BreakingDayListener> listenerMap = applicationContext.getBeansOfType(BreakingDayListener.class);
		for (Map.Entry<String, BreakingDayListener> entry : listenerMap.entrySet()) {
			entry.getValue().breakingDayTime(scheduledFireTime);
		}
	}
}
