package wisematches.server.player.impl;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.player.*;
import wisematches.server.player.locks.LockAccountManager;

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

	private static final String CHECK_PLAYER_QUERY = "" +
			"select count(player.username), count(player.email) " +
			"from wisematches.server.player.impl.HibernatePlayerImpl player " +
			"where player.username = ? or player.email = ?";

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
	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public Player getPlayer(long playerId) {
		return getHibernateTemplate().get(HibernatePlayerImpl.class, playerId);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public Player findByEmail(String email) {
		final HibernateTemplate template = getHibernateTemplate();
		List l = template.find("from wisematches.server.player.impl.HibernatePlayerImpl user where user.email=?", email);
		if (l.size() != 1) {
			return null;
		}
		return (Player) l.get(0);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public Player findByUsername(String username) {
		final HibernateTemplate template = getHibernateTemplate();
		List l = template.find("from wisematches.server.player.impl.HibernatePlayerImpl user where user.username=?", username);
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
	public void updatePlayer(Player player) throws UnknownAccountException, DuplicateAccountException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void removePlayer(Player player) throws UnknownAccountException {
		HibernatePlayerImpl hp;
		if (player instanceof HibernatePlayerImpl) {
			hp = (HibernatePlayerImpl) player;
		} else {
			hp = (HibernatePlayerImpl) getPlayer(player.getId());
		}
		if (hp != null) {
			getHibernateTemplate().delete(hp);
		}
	}

	private void checkPlayer(final Player player) throws InadmissibleUsernameException {
		final String reason = lockAccountManager.isUsernameLocked(player.getUsername());
		if (reason != null) {
			throw new InadmissibleUsernameException(player, reason);
		}


/*
		final int count = ((Number) ((List) getHibernateTemplate().findByNamedQuery("PlayersCount")).get(0)).intValue();
		final List list = template.executeFind(new HibernateCallback<List>() {
			public List doInHibernate(Session session) {
				final Query query = session.createQuery(CHECK_PLAYER_QUERY);
				query.setString(0, player.getUsername());
				query.setString(1, player.getEmail());
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
			throw new DuplicateAccountException(player, );
		}
*/
	}

	public void setLockAccountManager(LockAccountManager lockAccountManager) {
		this.lockAccountManager = lockAccountManager;
	}
}
