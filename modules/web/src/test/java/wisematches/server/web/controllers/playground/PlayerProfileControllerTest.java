package wisematches.server.web.controllers.playground;

import org.junit.Test;
import wisematches.server.standing.rating.RatingBatch;
import wisematches.server.standing.rating.RatingPeriod;
import wisematches.server.web.utils.GoogleChartTools;
import wisematches.server.web.utils.RatingChart;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerProfileControllerTest {
	public PlayerProfileControllerTest() {
	}

	final String[] NAMES = new String[]{"Jun", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

	final Calendar c = Calendar.getInstance();

	private Collection<RatingBatch> getPlayerBatches(RatingPeriod period) {
		final List<RatingBatch> batches = new ArrayList<RatingBatch>();
		int prev = 1200;
		for (int i = 0; i < period.getDaysNumber(); i++) {
//			if (Math.random() < 0.7) {
//				continue;
//			}
			short rating = (short) (prev + -10 + Math.random() * 20);
			batches.add(new RatingBatch(i, (short) (rating - 10), (short) (rating + 20), rating));
			prev = rating;
		}
		return batches;
	}

	@Test
	public void asd() {
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.MONTH, 1);

		final Date endDate = c.getTime();
		RatingChart chart = new RatingChart(c, RatingPeriod.YEAR, getPlayerBatches(RatingPeriod.YEAR));


		StringBuilder b = new StringBuilder();
		b.append("http://chart.apis.google.com/chart");
		b.append("?chf=bg,s,67676700");
		b.append("&chxl=0:|");

		final int[] monthIndexes = chart.getMonthIndexes();
		for (int i = 0; i < monthIndexes.length; i++) {
			if (i % 2 != 0) {
				b.append(monthIndexes[i]);
			}
			b.append("|");
		}
		b.append("1:|");
		for (int i = chart.getMinRating(); i < chart.getMaxRating(); i += 100) {
			b.append(i);
			b.append("|");
		}
		b.append(chart.getMaxRating());
		b.append("&chxs=0,676767,11.5,0,lt,676767|1,676767,15.5,0,l,676767");
		b.append("&chxt=x,y");
		b.append("&chs=300x150");
		b.append("&cht=lxy");
		b.append("&chco=008000,FFCC33,AA0033");
		b.append("&chd=e:");
		b.append(GoogleChartTools.encodeExtended(chart.getDates(), chart.getStartDate(), chart.getEndDate())).append(",");
		b.append(GoogleChartTools.encodeExtended(chart.getRatingsMax(), chart.getMinRating(), chart.getMaxRating())).append(",");
		b.append(GoogleChartTools.encodeExtended(chart.getDates(), chart.getStartDate(), chart.getEndDate())).append(",");
		b.append(GoogleChartTools.encodeExtended(chart.getRatingsAvg(), chart.getMinRating(), chart.getMaxRating())).append(",");
		b.append(GoogleChartTools.encodeExtended(chart.getDates(), chart.getStartDate(), chart.getEndDate())).append(",");
		b.append(GoogleChartTools.encodeExtended(chart.getRatingsMin(), chart.getMinRating(), chart.getMaxRating())).append(",");
//		b.append("&chd=s:").append(GoogleChartTools.encodeSimple(points, 0, batches.size() - 1)).append(",").append(GoogleChartTools.encodeSimple(values, min, max));
//		b.append("&chd=e:").append(encodeExtended(points, 0, 11)).append(",").append(encodeExtended(values, min, max));
		b.append("&chg=8.33,25");
		b.append("&chls=1,4,4|1|1,4,4");

		System.out.println(b.length());
		System.out.println(b);
	}
}
