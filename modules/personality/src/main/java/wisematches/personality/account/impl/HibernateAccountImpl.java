package wisematches.personality.account.impl;

import wisematches.personality.Language;
import wisematches.personality.Membership;
import wisematches.personality.account.Account;

import javax.persistence.*;
import java.util.TimeZone;

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
@Cacheable(true)
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

	@Column(name = "language")
	@Enumerated(EnumType.STRING)
	private Language language;

	@Column(name = "timezone")
	private TimeZone timeZone;

	@Column(name = "membership")
	@Enumerated(EnumType.STRING)
	private Membership membership;

	/**
	 * Hibernate only constructor
	 */
	@Deprecated
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
	public TimeZone getTimeZone() {
		return timeZone;
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
		this.timeZone = account.getTimeZone();
	}
}
