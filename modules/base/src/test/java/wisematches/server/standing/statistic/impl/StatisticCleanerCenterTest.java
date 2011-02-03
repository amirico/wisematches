package wisematches.server.standing.statistic.impl;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Ignore
public class StatisticCleanerCenterTest {
	@Test
	public void test() {
		Calendar c = Calendar.getInstance();

		long time = System.currentTimeMillis();
		c.setTimeInMillis(time);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR, 0);
		long asd = c.getTimeInMillis();

		long time2 = System.currentTimeMillis() - 19 * 60 * 60 * 1000;
		c.setTimeInMillis(time2);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR, 0);
		long asd2 = c.getTimeInMillis();


		System.out.println(new Date(time) + " " + new Date(asd));
		System.out.println(new Date(time2) + " " + new Date(asd2));
	}
}
