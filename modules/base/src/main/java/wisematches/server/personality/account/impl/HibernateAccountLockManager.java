package wisematches.server.personality.account.impl;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.personality.account.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HibernateAccountLockManager extends HibernateDaoSupport implements AccountLockManager {
	private final Collection<AccountLockListener> accountLockListeners = new CopyOnWriteArraySet<AccountLockListener>();
	private final Collection<AccountNicknameLockListener> listenerAccountNicknames = new CopyOnWriteArraySet<AccountNicknameLockListener>();

	private static final Logger log = Logger.getLogger(HibernateAccountLockManager.class);

	public void addAccountLockListener(AccountLockListener l) {
		accountLockListeners.add(l);
	}

	public void removeAccountLockListener(AccountLockListener l) {
		accountLockListeners.remove(l);
	}

	public void addAccountNicknameLockListener(AccountNicknameLockListener l) {
		listenerAccountNicknames.add(l);
	}

	public void removeAccountNicknameLockListener(AccountNicknameLockListener l) {
		listenerAccountNicknames.remove(l);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void lockAccount(Account player, String publicReason, String privateReason, Date unlockDate) {
		final HibernateTemplate template = getHibernateTemplate();
		final HibernateAccountLockInfo lai = template.get(HibernateAccountLockInfo.class, player.getId());
		if (lai != null) {
			lai.setPublicReason(publicReason);
			lai.setPrivateReason(privateReason);
			lai.setLockDate(new Date());
			lai.setUnlockDate(unlockDate);
			template.update(lai);
		} else {
			template.persist(new HibernateAccountLockInfo(player, publicReason, privateReason, unlockDate));
		}

		for (AccountLockListener accountLockListener : accountLockListeners) {
			accountLockListener.accountLocked(player, publicReason, privateReason, unlockDate);
		}
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void unlockAccount(Account player) {
		final HibernateTemplate template = getHibernateTemplate();

		final AccountLockInfo lai = template.get(HibernateAccountLockInfo.class, player.getId());
		if (lai != null) {
			template.delete(lai);

			for (AccountLockListener accountLockListener : accountLockListeners) {
				accountLockListener.accountUnlocked(player);
			}
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public boolean isAccountLocked(final Account player) {
		final HibernateTemplate template = getHibernateTemplate();
		final Date unlockDate = template.execute(new HibernateCallback<Date>() {
			public Date doInHibernate(Session session) throws HibernateException, SQLException {
				final Query query = session.createQuery("" +
						"select lock.unlockDate " +
						"from wisematches.server.personality.account.impl.HibernateAccountLockInfo lock " +
						"where lock.playerId = ?");
				query.setLong(0, player.getId());
				return (Date) query.uniqueResult();
			}
		});

		if (unlockDate != null) {
			final boolean res = System.currentTimeMillis() < unlockDate.getTime();
			if (!res) {
				template.execute(new HibernateCallback<Void>() {
					public Void doInHibernate(Session session) throws HibernateException, SQLException {
						final Query q = session.createQuery(
								"delete from wisematches.server.personality.account.impl.HibernateAccountLockInfo lock where lock.playerId = ?");
						q.setLong(0, player.getId());
						q.executeUpdate();

						for (AccountLockListener accountLockListener : accountLockListeners) {
							accountLockListener.accountUnlocked(player);
						}
						return null;
					}
				});
			}
			return res;
		}
		return false;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public AccountLockInfo getAccountLockInfo(Account player) {
		final HibernateTemplate template = getHibernateTemplate();
		HibernateAccountLockInfo accountInfo = template.get(HibernateAccountLockInfo.class, player.getId());
		if (log.isDebugEnabled()) {
			log.debug("Get account lock info: " + accountInfo);
		}
		if (accountInfo != null) {
			//object can be cached so we must update it from database.
			template.refresh(accountInfo);

			if (System.currentTimeMillis() < accountInfo.getUnlockDate().getTime()) {
				if (log.isDebugEnabled()) {
					log.debug("Account is stil locked");
				}
				accountInfo.setAccount(player);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Account already isn't locked. We can remove lock. Unlock date: " +
							accountInfo.getUnlockDate().getTime() + ". Now time: " + System.currentTimeMillis());
				}
				template.delete(accountInfo);

				for (AccountLockListener accountLockListener : accountLockListeners) {
					accountLockListener.accountUnlocked(player);
				}
				accountInfo = null;
			}
		}
		return accountInfo;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String isNicknameLocked(String nickname) {
		final HibernateTemplate template = getHibernateTemplate();
		final AccountNicknameLockInfo name = template.get(HibernateAccountNicknameLockInfo.class, nickname);
		if (name != null) {
			return name.getReason();
		}
		return null;
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void lockNickname(String nickname, String reason) {
		final HibernateTemplate template = getHibernateTemplate();

		HibernateAccountNicknameLockInfo info = template.get(HibernateAccountNicknameLockInfo.class, nickname);
		if (info == null) {
			info = new HibernateAccountNicknameLockInfo(nickname, reason);
			template.persist(info);
		} else {
			info.setReason(reason);
			template.update(info);
		}

		for (AccountNicknameLockListener listenerAccountNickname : listenerAccountNicknames) {
			listenerAccountNickname.usernameLocked(nickname, reason);
		}
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void unlockNickname(final String nickname) {
		final HibernateTemplate template = getHibernateTemplate();
		template.execute(new HibernateCallback<Void>() {
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				Query q = session.createQuery(
						"delete from wisematches.server.personality.account.impl.HibernateAccountNicknameLockInfo where username = ?");
				q.setString(0, nickname);
				final int removedCount = q.executeUpdate();
				if (removedCount != 0) {
					for (AccountNicknameLockListener listenerAccountNickname : listenerAccountNicknames) {
						listenerAccountNickname.usernameUnlocked(nickname);
					}
				}
				return null;
			}
		});
	}
}
