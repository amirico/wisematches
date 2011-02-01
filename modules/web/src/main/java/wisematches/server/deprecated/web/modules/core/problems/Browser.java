package wisematches.server.deprecated.web.modules.core.problems;

public enum Browser {
	EXPLORER_8("Internet Explorer 8", false),
	EXPLORER_7("Internet Explorer 7", false),
	EXPLORER_6("Internet Explorer 6", false),
	NETSCAPE("Netscape", false),
	FIREFOX_3("Firefox 3", true),
	FIREFOX_2("Firefox 2", false),
	SAFARI_3("Safari 3", true),
	SAFARI_2("Safari 2", false),
	CHROME("Google Chrome", true),
	OPERA("Opera", true);

	private final String name;
	private final boolean supported;

	Browser(String name, boolean supported) {
		this.name = name;
		this.supported = supported;
	}

	public String getName() {
		return name;
	}

	public boolean isSupported() {
		return supported;
	}
}
