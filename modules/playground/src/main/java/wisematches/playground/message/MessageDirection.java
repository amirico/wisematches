package wisematches.playground.message;

public enum MessageDirection {
	RECEIVED(1),
	SENT(2);

	private final int mask;

	MessageDirection(int mask) {
		this.mask = mask;
	}

	public int mask() {
		return mask;
	}

	public int add(int mask) {
		return mask | this.mask;
	}

	public int remove(int mask) {
		return mask & ~this.mask;
	}

	public static int of(MessageDirection d1, MessageDirection d2) {
		return d1.mask | d2.mask;
	}
}