package wisematches.client.gwt.core.client.content.problems;

/**
 * This class has only one static method that checks current browser is it supported or not. If browser
 * is not supported appropriate message box shown.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class BrowsersChecker {
	private BrowsersChecker() {
	}

//    /**
//     * Checks current browser is it supported or not. If browser is not supported appropriate message box shown.
//     */
//    public static void checkBrowser() {
//        final Browser browser = getCurrentBrowser();
//        if (browser == null || !browser.isSupported()) {
//            final MessageBoxConfig boxConfig = new MessageBoxConfig();
//            boxConfig.setModal(true);
//            boxConfig.setTitle(COMMON.ttlUnsupportedBrowser());
//            boxConfig.setClosable(true);
//
//            final StringBuilder message = new StringBuilder();
//            message.append("<ul>");
//            final Browser[] browsers = Browser.values();
//            for (Browser b : browsers) {
//                if (b.isSupported()) {
//                    message.append("&nbsp;&nbsp; - <b>").append(b.getName()).append("</b></br>");
//                }
//            }
//            message.append("</ul>");
//            boxConfig.setMsg(MCOMMON.msgUnsupportedBrowsers(message.toString()));
//
//            MessageBox.show(boxConfig);
//        }
//    }
//
//    public static Browser getCurrentBrowser() {
//        Browser browser = null;
//
//        final String name = getUserAgent();
//        Log.info("User agent: " + name);
//        System.out.println("User agent: " + name);
//        if (name.contains("chrome")) {
//            browser = Browser.CHROME;
//        } else if (name.contains("safari")) {
//            browser = Browser.SAFARI_3;
//        } else if (name.contains("opera")) {
//            browser = Browser.OPERA;
//        } else if (name.contains("MSIE")) {
//            if (name.contains("MSIE 6")) {
//                browser = Browser.EXPLORER_6;
//            } else if (name.contains("MSIE 7")) {
//                browser = Browser.EXPLORER_7;
//            } else if (name.contains("MSIE 8")) {
//                browser = Browser.EXPLORER_8;
//            }
//        } else if (name.contains("firefox")) {
//            if (name.contains("/3")) {
//                browser = Browser.FIREFOX_3;
//            } else if (name.contains("/2")) {
//                browser = Browser.FIREFOX_2;
//            }
//        } else if (name.contains("netscape")) {
//            browser = Browser.NETSCAPE;
//        }
//        return browser;
//    }
//
//    public static native String getUserAgent() /*-{
//        return navigator.userAgent.toLowerCase();
//    }-*/;
//
}
