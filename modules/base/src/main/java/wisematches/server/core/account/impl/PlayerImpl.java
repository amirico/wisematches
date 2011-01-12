package wisematches.server.core.account.impl;

import org.hibernate.annotations.*;
import wisematches.core.user.Language;
import wisematches.kernel.player.Player;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

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
@Table(name = "user_user")
@org.hibernate.annotations.Table
		(appliesTo = "user_user",
				indexes = {
						@Index(name = "id", columnNames = {"id"}),
						@Index(name = "login", columnNames = {"username", "password"})
				}
		)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "PlayersCount", query = "SELECT count(*) FROM wisematches.server.core.account.impl.PlayerImpl",
				hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
		),
		@NamedQuery
				(name = "PlayerPosition", query =
						"SELECT count( a.id ), a.id " +
								"FROM wisematches.server.core.account.impl.PlayerImpl a, wisematches.server.core.account.impl.PlayerImpl b " +
								"WHERE a.id = ? AND (b.rating > a.rating OR (b.rating = a.rating AND b.id <= a.id)) GROUP BY a.id",
						hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
				)
})
@TypeDefs(
		{
				@TypeDef(
						name = "language",
						typeClass = LanguageUserType.class,
						parameters = {
						}
				)
		}
)
public class PlayerImpl implements Player, Serializable {
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Basic
	@Column(name = "username", nullable = false, length = 50, updatable = false)
	private String username;

	@Basic
	@Column(name = "password", nullable = false, length = 100)
	private String password;

	@Basic
	@Column(name = "email", nullable = false, length = 150)
	private String email;

	@Basic
	@Column(name = "creationDate")
	private Date creationDate;

	@Basic
	@Column(name = "lastSigninDate")
	private Date lastSigninDate;

	@Basic
	@Column(name = "rating")
	private int rating;

	@Type(type = "language")
	@Column(name = "locale")
	private Language language;

	@PrimaryKeyJoinColumn()
	@OneToOne(cascade = {CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.MERGE}, optional = false, fetch = FetchType.LAZY)
	private PlayerProfileImpl playerProfile;

	@PrimaryKeyJoinColumn()
	@OneToOne(cascade = {CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.MERGE}, optional = false, fetch = FetchType.LAZY)
	private PlayerNotificationsImpl playerNotifications;

	/**
	 * For hibernate only.
	 */
	protected PlayerImpl() {
	}

	public PlayerImpl(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;

		creationDate = new Date();
		playerProfile = new PlayerProfileImpl();
		playerNotifications = new PlayerNotificationsImpl();
		lastSigninDate = creationDate;
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void changeRating(int delta) {
		rating += delta;
	}

	void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastSigninDate() {
		return lastSigninDate;
	}

	public void setLastSigninDate(Date lastSigninDate) {
		this.lastSigninDate = lastSigninDate;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public PlayerProfileImpl getPlayerProfile() {
		return playerProfile;
	}

	void setPlayerProfile(PlayerProfileImpl playerProfile) {
		this.playerProfile = playerProfile;
	}

	public PlayerNotificationsImpl getPlayerNotifications() {
		return playerNotifications;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		PlayerImpl player = (PlayerImpl) o;
		return id == player.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "PlayerImpl{" +
				"id=" + id +
				", username='" + username + '\'' +
				", rating=" + rating +
				'}';
	}
}
