package wisematches.server.player.member.impl;

import org.hibernate.annotations.*;
import wisematches.server.player.Language;
import wisematches.server.player.Membership;
import wisematches.server.player.Player;
import wisematches.server.player.member.MemberPlayer;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Implementation of player that contains Hibernate annotations and can be stored into database using Hibernate
 * framework.
 * <p/>
 * This implementation redefines {@code equals} and {@code hashCode} methods and garanties that
 * two players are equals if and only if it's IDs are equals. Any other attributes are ignored.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "user_player")
@org.hibernate.annotations.Table
		(appliesTo = "user_player",
				indexes = {
						@Index(name = "id", columnNames = {"id"}),
						@Index(name = "email", columnNames = {"email"}),
						@Index(name = "username", columnNames = {"username"})
				}
		)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
/*
@NamedQueries({
		@NamedQuery
				(name = "PlayerPosition", query =
						"SELECT count( a.id ), a.id " +
								"FROM wisematches.server.core.account.impl.PlayerImpl a, wisematches.server.core.account.impl.PlayerImpl b " +
								"WHERE a.id = ? AND (b.rating > a.rating OR (b.rating = a.rating AND b.id <= a.id)) GROUP BY a.id",
						hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
				)
})
*/
@TypeDefs(
		{
				@TypeDef(
						name = "language",
						typeClass = LanguageUserType.class
				),
				@TypeDef(
						name = "membership",
						typeClass = MembershipUserType.class
				)
		}
)
public class HibernatePlayerImpl implements MemberPlayer {
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Basic
	@Column(name = "username", nullable = false, length = 100, updatable = false)
	private String username;

	@Basic
	@Column(name = "password", nullable = false, length = 100)
	private String password;

	@Basic
	@Column(name = "email", nullable = false, length = 150)
	private String email;

	@Basic
	@Column(name = "rating")
	private int rating;

	@Type(type = "language")
	@Column(name = "language")
	private Language language;

	@Type(type = "membership")
	@Column(name = "membership")
	private Membership membership;

	/**
	 * Hibernate only constructor
	 */
	HibernatePlayerImpl() {
	}

	public HibernatePlayerImpl(Player player) {
		this.id = player.getId();
		this.username = player.getNickname();
		updatePlayerInfo(player);
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getNickname() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	@Override
	public Membership getMembership() {
		return membership;
	}

	@Override
	public int getRating() {
		return rating;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		HibernatePlayerImpl that = (HibernatePlayerImpl) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HibernatePlayerImpl");
		sb.append("{id=").append(id);
		sb.append('}');
		return sb.toString();
	}

	/**
	 * Private method. Allows update player info without creation new object.
	 *
	 * @param player the player with exist
	 */
	final void updatePlayerInfo(Player player) {
		if (player.getId() != id) {
			throw new IllegalArgumentException("Player ids are not equals.");
		}
		this.password = player.getPassword();
		this.email = player.getEmail();
		this.language = player.getLanguage();
		this.membership = player.getMembership();
		this.rating = player.getRating();
	}
}
