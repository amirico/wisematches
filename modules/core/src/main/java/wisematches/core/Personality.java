package wisematches.core;

import javax.persistence.*;
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
 * <p/>
 * It's possible to create very simple light version of {@code Personality} using {@link #person(long)} method. This
 * method creates {@code Personality} only with id and without any attributes that can be used in {@code Map}s.
 * For example, if only check is required.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @see #person(long)
 */
@MappedSuperclass
public class Personality implements Serializable {
	@Id
	@Column(name = "id", nullable = false, updatable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * Hibernate constructor
	 */
	protected Personality() {
	}

	/**
	 * Creates new personality with specified id.
	 *
	 * @param id the id for this personality.
	 */
	protected Personality(long id) {
		this.id = id;
	}

	/**
	 * Returns personality id.
	 *
	 * @return the id of the personality.
	 */
	public final long getId() {
		return id;
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
	 * Creates lightweight object of {@code Personality} with only id.
	 *
	 * @param id the personality id.
	 * @return the personality object only with id attribute.
	 */
	public static Personality person(long id) {
		return new Personality(id);
	}

	/**
	 * Unties specified personality object from original context. This is useful if the {@code person} object
	 * is complex object, like {@code Account} or {@code Player} and you want store it in collections or maps because
	 * reference will be locked.
	 *
	 * @param person the original personality
	 * @return player {@code Personality} object actually, instance of {@code Personality} class but not any subclass.
	 */
	public static Personality untie(Personality person) {
		if (person == null) {
			return null;
		}
		if (person.getClass() == Personality.class) {
			return person;
		}
		return person(person.getId());
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Personality");
		sb.append("{id=").append(id);
		sb.append('}');
		return sb.toString();
	}
}