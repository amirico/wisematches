package wisematches.server.web.controllers.personality.settings.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class TimeZonesInfo {
    private static final Collection<TimeZone> timeZones = new ArrayList<TimeZone>();

    static {
        final String[] availableIDs = TimeZone.getAvailableIDs();
        Arrays.sort(availableIDs);
        for (String availableID : availableIDs) {
            if (!availableID.contains("/")) {
                continue;
            }
            timeZones.add(TimeZone.getTimeZone(availableID));
        }
    }

    private TimeZonesInfo() {
    }

    public static Collection<TimeZone> getTimeZones() {
        return timeZones;
    }
}
