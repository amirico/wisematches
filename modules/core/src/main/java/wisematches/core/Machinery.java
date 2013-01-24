package wisematches.core;

/**
 * The {@code Machinery} is predefined, artificial intelligence that can play instead of
 * real opponents.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class Machinery extends Personality {
	protected Machinery(long id) {
		super(id);
	}

	@Override
	public final Type getType() {
		return Type.MACHINERY;
	}
}
