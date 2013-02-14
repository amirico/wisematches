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

	private final int hashCode;

	private final PersonalityType type;

	/**
	 * Creates new personality with specified id.
	 *
	 * @param id the id for this personality.
	 */
	Personality(long id) {
		if (this instanceof Player) {
			type = PersonalityType.PLAYER;
		} else if (this instanceof Visitor) {
			type = PersonalityType.VISITOR;
		} else if (this instanceof Robot) {
			type = PersonalityType.ROBOT;
		} else {
			throw new IncompatibleClassChangeError("You can't extends Personality directly. Please extend one of: Visitor, Robot or Player classes");
		}
		this.id = id;
		hashCode = (int) (id ^ (id >>> 32));
	}

	/**
	 * Returns personality id.
	 *
	 * @return the id of the personality.
	 */
	public final Long getId() {
		return id;
	}

	/**
	 * Indicates that this personality is traceable and have profile, statistics and so on.
	 * <p/>
	 * This method can be used is view forms for fast checks.
	 *
	 * @return {@code true} if personality is traceable or {@code false} if not.
	 */
	public final PersonalityType getType() {
		return type;
	}

	/**
	 * Returns hash code of the personality calculated by formula:
	 * {@code (int) (id ^ (id >>> 32))}
	 *
	 * @return the hash code
	 */
	@Override
	public final int hashCode() {
		return hashCode;
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
		return id.equals(that.id);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("{id=").append(id).append('}');
		return sb.toString();
	}
}