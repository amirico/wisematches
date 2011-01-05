package wisematches.client.gwt.app.client.util;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class Localization {
    public static String convertLocale(String locale) {
        if ("ru".equals(locale)) {
            return wisematches.client.gwt.app.client.content.i18n.AppRes.APP.lblLocaleRussian();
        }
        return wisematches.client.gwt.app.client.content.i18n.AppRes.APP.lblLocaleEnglish();
    }
}
