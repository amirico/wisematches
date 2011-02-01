package wisematches.server.deprecated.web.modules.core.problems;

public enum OperationSystems {
	WINDOWS_95("Windows 95"),
	WINDOWS_ME("Windows ME"),
	WINDOWS_98("Windows 98"),
	WINDOWS_2000("Windows 2000"),
	WINDOWS_XP("Windows XP"),
	WINDOWS_SERVER_2003("Windows Server 2003"),
	WINDOWS_VISTA("Windows Vista"),
	WINDOWS_CE("Windows CE"),
	WINDOWS("Windows"),
	INTEL_MAC_OS("Intel Macintosh OS X"),
	POWER_MAC_OS("PowerPC Macintosh OS X"),
	MAC_OS("Macintosh"),
	SUNOS("SunOS"),
	LINUX("Linux");

	private final String name;

	OperationSystems(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
