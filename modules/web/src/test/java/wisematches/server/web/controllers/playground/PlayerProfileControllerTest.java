package wisematches.server.web.controllers.playground;

import org.junit.Test;
import wisematches.server.standing.rating.RatingBatch;
import wisematches.server.web.utils.GoogleChartTools;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerProfileControllerTest {
	public PlayerProfileControllerTest() {
	}

	final String[] NAMES = new String[]{"Jun", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

	@Test
	public void asd() {
		final Collection<RatingBatch> batches = Arrays.<RatingBatch>asList(
				new RatingBatch(1, (short) 1214),
				new RatingBatch(3, (short) 1287),
				new RatingBatch(5, (short) 1245),
				new RatingBatch(6, (short) 1227),
				new RatingBatch(7, (short) 1276),
				new RatingBatch(8, (short) 1221),
				new RatingBatch(10, (short) 1232),
				new RatingBatch(11, (short) 1296),
				new RatingBatch(9, (short) 1200),
				new RatingBatch(12, (short) 1309),
				new RatingBatch(2, (short) 1281),
				new RatingBatch(4, (short) 1200));

		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());

		final int middle = c.get(Calendar.MONTH) + 1;

		final int[] points = new int[12];
		final int[] values = new int[12];
		Arrays.fill(values, (short) -1);
		for (RatingBatch batch : batches) {
			int pos = (int) batch.getBatchNumber();
			if (pos > middle) {
				pos -= 12;
			}
			int index = 11 - middle + pos;
			values[index] = batch.getRating();
		}

		final String[] names = new String[12];
		for (int i = 1; i <= NAMES.length; i++) {
			int pos = i > middle ? i - 12 : i;
			names[11 - middle + pos] = NAMES[i - 1];
			points[i - 1] = i - 1;
		}

		int min = 1200;
		int max = 1200;
		for (int value1 : values) {
			if (value1 > max) {
				max = value1;
			} else if (value1 >= 0) {
				min = value1;
			}
		}
		max = (int) Math.floor((max / 100) * 100 + 100);
		min = (int) Math.ceil((min / 100) * 100 - 100);

		StringBuilder b = new StringBuilder();
		b.append("http://chart.apis.google.com/chart");
		b.append("?chf=bg,s,67676700");
		b.append("&chxl=0:|");
		for (int i = 0; i < names.length; i++) {
			if (i % 2 != 0) {
				b.append(names[i]);
			}
			b.append("|");
		}
		b.append("1:|");
		for (int i = min; i < max; i += 100) {
			b.append(i);
			b.append("|");
		}
		b.append(max);
		b.append("&chxs=0,676767,11.5,0,lt,676767|1,676767,15.5,0,l,676767");
		b.append("&chxt=x,y");
		b.append("&chs=300x150");
		b.append("&cht=lxy");
		b.append("&chd=s:").append(GoogleChartTools.encodeSimple(points, 0, 11)).append(",").append(GoogleChartTools.encodeSimple(values, min, max));
//		b.append("&chd=e:").append(encodeExtended(points, 0, 11)).append(",").append(encodeExtended(values, min, max));
		b.append("&chg=8.33,25");
		b.append("&chls=2");
		b.append("&chm=o,FF9900,0,-1,8");
	}


}
