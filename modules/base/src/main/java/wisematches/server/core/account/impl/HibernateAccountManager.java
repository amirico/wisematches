package wisematches.server.core.account.impl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.kernel.player.Player;
import wisematches.server.core.InvalidArgumentException;
import wisematches.server.core.account.*;
import wisematches.server.player.locks.LockAccountManager;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateAccountManager extends HibernatePlayerManager implements AccountManager, PlayerManager {
	private int defaultPlayerRating;
	private LockAccountManager lockAccountManager;

	private final Collection<AccountListener> listeners = new CopyOnWriteArraySet<AccountListener>();

	private static final String CHECK_PLAYER_QUERY =
			"select player.username, player.email from " +
					PlayerImpl.class.getName() + " player where username = ? or email = ?";

	private static final String EMAIL_REGEXP = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

	public HibernateAccountManager() {
	}

	public void addAccountListener(AccountListener l) {
		listeners.add(l);
	}

	public void removeAccountListener(AccountListener l) {
		listeners.remove(l);
	}

	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public Player authentificate(String username, String password) throws AccountLockedException,
			AccountNotFountException {

		final HibernateTemplate template = getHibernateTemplate();
		List l = template.find("from " + PlayerImpl.class.getName() +
				" user where user.username=? and user.password = ?", new Object[]{username, password});
		if (l.size() != 1) {
			throw new AccountNotFountException();
		}
		final Player player = (Player) l.get(0);
		authentificate(player);
		return player;
	}

	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public void authentificate(Player player) throws AccountLockedException {
/*
        if (lockAccountManager.isAccountLocked(player)) {
            final LockAccountInfo accountInfo = lockAccountManager.getLockAccountInfo(player);
            //we must do double checking because account may be unlocked.
            if (accountInfo != null) {
                throw new AccountLockedException(accountInfo);
            }
        }
*/
	}

	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public Player findByUsername(String name) {
		final HibernateTemplate template = getHibernateTemplate();
		List l = template.find("from " + PlayerImpl.class.getName() + " user where user.username=?", name);
		if (l.size() != 1) {
			return null;
		}
		return (PlayerImpl) l.get(0);
	}

	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public Player findByEmail(String email) {
		final HibernateTemplate template = getHibernateTemplate();
		List l = template.find("from " + PlayerImpl.class.getName() + " user where user.email=?", email);
		if (l.size() != 1) {
			return null;
		}
		return (Player) l.get(0);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public Player createPlayer(String username, String password, String email) throws AccountRegistrationException {
		checkUsername(username);
		checkEmail(email);
		checkDublicate(username, email);

		final HibernateTemplate template = getHibernateTemplate();

		final PlayerImpl player = new PlayerImpl(username, password, email);
		player.setRating(defaultPlayerRating);
		template.save(player);

		final PlayerProfileImpl playerProfile = player.getPlayerProfile();
		playerProfile.setPlayerId(player.getId());
		template.save(playerProfile);

		final PlayerNotificationsImpl playerNotifications = player.getPlayerNotifications();
		playerNotifications.setPlayerId(player.getId());
		template.save(playerNotifications);
		template.flush();

		final Player savedPlayer = findByUsername(username);
		if (savedPlayer == null) {
			throw new AccountRegistrationException("Player can't be loaded after saving");
		}


		for (AccountListener listener : listeners) {
			listener.accountCreated(savedPlayer);
		}
		return savedPlayer;
	}

	private void checkUsername(String username) throws InadmissibleUsernameException {
		if (username.length() < 3) {
			throw new InvalidArgumentException("username", "Username can't be less than 3 chars");
		}

		final String usernameLocked = lockAccountManager.isUsernameLocked(username);
		if (usernameLocked != null) {
			throw new InadmissibleUsernameException(usernameLocked);
		}
	}

	private void checkEmail(String email) {
		if (!email.matches(EMAIL_REGEXP)) {
			throw new InvalidArgumentException("email", "EMail address has invalid format");
		}
	}

	private void checkDublicate(final String username, final String email) throws DublicateAccountException {
		HibernateTemplate template = getHibernateTemplate();
		final List list = template.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				final Query query = session.createQuery(CHECK_PLAYER_QUERY);
				query.setString(0, username);
				query.setString(1, email);
				query.setMaxResults(1); //we don't check all database. First record is enaf.
				return query.list();
			}
		});
		if (list != null && list.size() != 0) {
			boolean usernameDublicate = false;
			boolean emailDublicate = false;

			for (Object o : list) {
				final Object[] values = (Object[]) o;
				final String un = (String) values[0];
				final String mail = (String) values[1];

				usernameDublicate = usernameDublicate || username.equals(un);
				emailDublicate = emailDublicate || email.equals(mail);
			}
			throw new DublicateAccountException(usernameDublicate, emailDublicate);
		}
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void deletePlayer(Player player) {
		final HibernateTemplate template = getHibernateTemplate();
		template.delete(player);
		template.flush();

		for (AccountListener listener : listeners) {
			listener.accountDeleted(player);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getRegistredPlayersCount() {
		return ((Number) ((List) getHibernateTemplate().findByNamedQuery("PlayersCount")).get(0)).intValue();
	}

	LockAccountManager getLockAccountManager() {
		return lockAccountManager;
	}

	public void setLockAccountManager(LockAccountManager lockAccountManager) {
		this.lockAccountManager = lockAccountManager;
	}

	public void setDefaultPlayerRating(int defaultPlayerRating) {
		this.defaultPlayerRating = defaultPlayerRating;
	}
}
