package wisematches.core;

import java.io.Serializable;

/**
 * {@code Personality} is main element of the Wisematches. It identifies account, players and so on. Any actions,
 * data and so one belongs to a personality. For example, only {@code Personality} can play a game, have a rating,
 * statistic and so on.
 * <p/>
 * Each personality must have id to be identified. Two personality with the same id are always equals.
 * Any other attributes are ignored.
 * <p/>
 * {@code Personality} is the Hibernate entity and {@code id} field is always mapped to {@code id} column and must
 * be unique, can't be null and updated.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class Personality implements Serializable {
	private final Long id;

	/**
	 * Creates new personality with specified id.
	 *
	 * @param id the id for this personality.
	 */
	Personality(Long id) {
		this.id = id;
	}

	/**
	 * Returns type of this player.
	 *
	 * @return the type of this player.
	 */
	public abstract Type getType();

	/**
	 * Returns personality id.
	 *
	 * @return the id of the personality.
	 */
	public final Long getId() {
		return id;
	}

	/**
	 * Returns hash code of the personality calculated by formula:
	 * {@code (int) (id ^ (id >>> 32))}
	 *
	 * @return the hash code
	 */
	@Override
	public final int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	/**
	 * Two personalities are equals if they ids are equals. Any subclasses of {@code Personality} are acceptable.
	 *
	 * @param o the object to be checked.
	 * @return {@code true} if specified object is subclass of {@code Personality} and has the same id;
	 *         {@code false} - otherwise.
	 */
	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Personality)) return false;
		final Personality that = (Personality) o;
		return id == that.id;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Personality");
		sb.append("{id=").append(id);
		sb.append('}');
		return sb.toString();
	}

	/**
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	public static enum Type {
		PLAYER,
		MACHINERY;

		public boolean isPlayer() {
			return this == PLAYER;
		}

		public boolean isMachinery() {
			return this == MACHINERY;
		}
	}
}