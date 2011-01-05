package wisematches.client.gwt.app.client.util;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TimeFormatter {
    public static String getTimezoneName(int timezoneCode) {
        String prefix = "GMT";
        if (timezoneCode >= 0) {
            prefix += "+";
        } else {
            prefix += "-";
        }
        if (timezoneCode >= -9 && timezoneCode <= 9) {
            prefix += "0";
        }
        return prefix + Math.abs(timezoneCode);
    }

    /**
     * Convert timeout in minutes to string.
     *
     * @param timeToLeft the time to left in minutes.
     * @return the string representation.
     */
    public static String convertTimeout(int timeToLeft) {
        if (timeToLeft == 0) {
            return wisematches.client.gwt.app.client.content.i18n.AppRes.APP.lblNA();
        }

        int days = timeToLeft / 24 / 60;
        int hours = (timeToLeft - days * 24 * 60) / 60;
        int minutes = timeToLeft % 60;

        StringBuilder res = new StringBuilder();
        if (days > 365) {
            res.append(days / 365);
            res.append("y ");
            days = days - ((days / 365) * 365);
        }

        if (days != 0) {
            res.append(days);
            res.append("d ");
        }
        if (hours != 0) {
            res.append(hours);
            res.append("h ");
        }
        if (minutes != 0) {
            res.append(minutes);
            res.append("m ");
        }

        if (res.length() == 0) {
            res.append(wisematches.client.gwt.app.client.content.i18n.AppRes.APP.lblLessThanMinute());
        }
        return res.toString();
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long serverTimeMillis() {
        return currentTimeMillis(); //TODO: incorrect. Correction with server time should be maden
    }
}
