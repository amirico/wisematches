package wisematches.server.gameplaying.room;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class MockRoom implements Room {
	public static final MockRoom type = new MockRoom("type");

	public static final MockRoom type2 = new MockRoom("type2");

	public static final MockRoom type3 = new MockRoom("type3");
	private final String name;

	private MockRoom(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("MockRoom").append("{").append(name).append('}');
		return sb.toString();
	}
}
