package wisematches.server.player.member.impl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.player.*;
import wisematches.server.testimonial.lock.LockAccountManager;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateAccountManager extends HibernateDaoSupport implements PlayerManager, AccountManager {
	private LockAccountManager lockAccountManager;

	private final Collection<PlayerListener> playerListeners = new CopyOnWriteArraySet<PlayerListener>();
	private final Collection<AccountListener> accountListeners = new CopyOnWriteArraySet<AccountListener>();

	private static final String CHECK_ACCOUNT_AVAILABILITY = "" +
			"select player.username, player.email " +
			"from wisematches.server.player.member.impl.HibernatePlayerImpl player " +
			"where player.username = :username or player.email = :email";

	public HibernateAccountManager() {
	}

	@Override
	public void addPlayerListener(PlayerListener listener) {
		if (listener != null) {
			playerListeners.add(listener);
		}
	}

	@Override
	public void removePlayerListener(PlayerListener listener) {
		if (listener != null) {
			playerListeners.remove(listener);
		}
	}

	@Override
	public void addAccountListener(AccountListener l) {
		if (l != null) {
			accountListeners.add(l);
		}
	}

	@Override
	public void removeAccountListener(AccountListener l) {
		if (l != null) {
			accountListeners.remove(l);
		}
	}

	@Override
	public Player getPlayer(long playerId) {
		return getHibernateTemplate().get(HibernatePlayerImpl.class, playerId);
	}

	@Override
	public Player findByEmail(String email) {
		final HibernateTemplate template = getHibernateTemplate();
		List l = template.find("from wisematches.server.player.member.impl.HibernatePlayerImpl user where user.email=?", email);
		if (l.size() != 1) {
			return null;
		}
		return (Player) l.get(0);
	}

	@Override
	public Player findByUsername(String username) {
		final HibernateTemplate template = getHibernateTemplate();
		List l = template.find("from wisematches.server.player.member.impl.HibernatePlayerImpl user where user.username=?", username);
		if (l.size() != 1) {
			return null;
		}
		return (Player) l.get(0);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public Player createPlayer(Player player) throws DuplicateAccountException, InadmissibleUsernameException {
		checkPlayer(player);

		final HibernateTemplate template = getHibernateTemplate();
		final HibernatePlayerImpl hp = new HibernatePlayerImpl(player);
		template.save(hp);

		for (AccountListener accountListener : accountListeners) {
			accountListener.accountCreated(hp);
		}
		return hp;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void updatePlayer(Player player) throws UnknownAccountException, DuplicateAccountException, InadmissibleUsernameException {
		final AccountAvailability a = checkAccountAvailable(player.getNickname(), player.getEmail());
		if (!a.isEmailAvailable()) {
			throw new DuplicateAccountException(player, "email");
		}

		Player oldPlayer = getPlayer(player.getId());
		if (oldPlayer == null) {
			throw new UnknownAccountException(player);
		}
		// Copy previous state
		oldPlayer = new PlayerEditor(oldPlayer).createPlayer();

		// merge and update
		final HibernateTemplate hibernateTemplate = getHibernateTemplate();
		final Player p = hibernateTemplate.merge(new HibernatePlayerImpl(player));
		hibernateTemplate.flush();
		for (PlayerListener playerListener : playerListeners) {
			playerListener.playerUpdated(oldPlayer, p);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void removePlayer(Player player) throws UnknownAccountException {
		final HibernatePlayerImpl hp = (HibernatePlayerImpl) getPlayer(player.getId());
		if (hp != null) {
			getHibernateTemplate().delete(hp);

			for (AccountListener accountListener : accountListeners) {
				accountListener.accountRemove(player);
			}
		}
	}

	@Override
	public AccountAvailability checkAccountAvailable(final String username, final String email) {
		final long[] res = getHibernateTemplate().execute(new HibernateCallback<long[]>() {
			@Override
			public long[] doInHibernate(Session session) throws HibernateException, SQLException {
				final Query query = session.createQuery(CHECK_ACCOUNT_AVAILABILITY);
				query.setString("username", username);
				query.setString("email", email);

				final long[] res = new long[2];
				final List list = query.list();
				for (Object lValue : list) {
					final Object[] o = (Object[]) lValue;
					if (username.equals(o[0])) {
						res[0]++;
					}
					if (email.equals(o[1])) {
						res[1]++;
					}
				}
				return res;
			}
		});
		return new AccountAvailability(res[1] == 0, res[0] == 0, lockAccountManager.isUsernameLocked(username) == null);
	}

	private void checkPlayer(final Player player) throws InadmissibleUsernameException, DuplicateAccountException {
		final String reason = lockAccountManager.isUsernameLocked(player.getNickname());
		if (reason != null) {
			throw new InadmissibleUsernameException(player, reason);
		}

		final AccountAvailability a = checkAccountAvailable(player.getNickname(), player.getEmail());
		if (!a.isAvailable()) {
			if (!a.isEmailAvailable() && a.isUsernameAvailable()) {
				throw new DuplicateAccountException(player, "email");
			} else if (a.isEmailAvailable() && !a.isUsernameAvailable()) {
				throw new DuplicateAccountException(player, "username");
			} else {
				throw new DuplicateAccountException(player, "username", "email");
			}
		}
	}

	public void setLockAccountManager(LockAccountManager lockAccountManager) {
		this.lockAccountManager = lockAccountManager;
	}
}
