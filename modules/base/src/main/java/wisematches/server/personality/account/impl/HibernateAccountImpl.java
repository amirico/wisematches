package wisematches.server.personality.account.impl;

import org.hibernate.annotations.*;
import wisematches.server.personality.account.Account;
import wisematches.server.personality.account.Language;
import wisematches.server.personality.account.Membership;

import javax.persistence.Basic;
import javax.persistence.Column;
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
@Table(name = "account_personality")
@org.hibernate.annotations.Table
		(appliesTo = "account_personality",
				indexes = {
						@Index(name = "id", columnNames = {"id"}),
						@Index(name = "email", columnNames = {"email"}),
						@Index(name = "nickname", columnNames = {"nickname"})
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
public class HibernateAccountImpl extends Account {
	@Basic
	@Column(name = "nickname", nullable = false, length = 100, updatable = false)
	private String nickname;

	@Basic
	@Column(name = "password", nullable = false, length = 100)
	private String password;

	@Basic
	@Column(name = "email", nullable = false, length = 150)
	private String email;

	@Type(type = "language")
	@Column(name = "language")
	private Language language;

	@Type(type = "membership")
	@Column(name = "membership")
	private Membership membership;

	/**
	 * Hibernate only constructor
	 */
	HibernateAccountImpl() {
	}

	public HibernateAccountImpl(Account account) {
		super(account.getId());
		this.nickname = account.getNickname();
		updateAccountInfo(account);
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getNickname() {
		return nickname;
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
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HibernateAccountImpl");
		sb.append("{id=").append(getId());
		sb.append('}');
		return sb.toString();
	}

	/**
	 * Private method. Allows update player info without creation new object.
	 *
	 * @param account the player with exist
	 */
	final void updateAccountInfo(Account account) {
		if (!this.equals(account)) {
			throw new IllegalArgumentException("Player ids are not equals.");
		}
		this.password = account.getPassword();
		this.email = account.getEmail();
		this.language = account.getLanguage();
		this.membership = account.getMembership();
	}
}
