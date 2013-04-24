package wisematches.server.web.servlet.mvc.playground.player.profile.form;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GoogleChartTools {
	private static final String SIMPLE_MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final String EXTENDED_MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-.";

	private GoogleChartTools() {
	}

	public static String encodeSimple(short[] values, float min, float max) {
		final StringBuilder b = new StringBuilder();
		for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
			int value = values[i];
			if (value < min) {
				continue;
			}
			if (value >= 0) {
				b.append(SIMPLE_MAP.charAt((int) Math.ceil((SIMPLE_MAP.length() - 1) * (value - min) / (max - min))));
			}
		}
		return b.toString();
	}

	public static String encodeExtended(short[] values, float min, float max) {
		final int length = EXTENDED_MAP.length();
		final StringBuilder b = new StringBuilder();
		for (int value : values) {
			if (value >= 0) {
				value = (int) Math.floor(length * length * (value - min) / (max - min));
				if (value > (length * length) - 1) {
					b.append("..");
				} else if (value < 0) {
					b.append("__");
				} else {
					final int quotient = (int) Math.floor(value / length);
					final int remainder = value - length * quotient;
					b.append(EXTENDED_MAP.charAt(quotient)).append(EXTENDED_MAP.charAt(remainder));
				}
			}
		}
		return b.toString();
	}
}
