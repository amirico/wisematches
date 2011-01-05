package wisematches.server.core.account.impl;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.kernel.player.Player;
import wisematches.server.core.account.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HibernateLockAccountManager extends HibernateDaoSupport implements LockAccountManager {
    private final Collection<LockAccountListener> accountListeners = new CopyOnWriteArraySet<LockAccountListener>();
    private final Collection<LockUsernameListener> usernameListeners = new CopyOnWriteArraySet<LockUsernameListener>();

    private static final Logger log = Logger.getLogger(HibernateLockAccountManager.class);

    public void addLockAccountListener(LockAccountListener l) {
        accountListeners.add(l);
    }

    public void removeLockAccountListener(LockAccountListener l) {
        accountListeners.remove(l);
    }

    public void addLockUsernameListener(LockUsernameListener l) {
        usernameListeners.add(l);
    }

    public void removeLockUsernameListener(LockUsernameListener l) {
        usernameListeners.remove(l);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void lockAccount(Player player, String publicReason, String privateReason, Date unlockdate) {
        final HibernateTemplate template = getHibernateTemplate();
        final LockAccountInfo lai = (LockAccountInfo) template.get(LockAccountInfo.class, player.getId());
        if (lai != null) {
            lai.setPublicReason(publicReason);
            lai.setPrivateReason(privateReason);
            lai.setUnlockDate(unlockdate);
            template.update(lai);
        } else {
            template.persist(new LockAccountInfo(player, publicReason, privateReason, unlockdate));
        }
        template.flush();

        for (LockAccountListener accountListener : accountListeners) {
            accountListener.accountLocked(player, publicReason, privateReason, unlockdate);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void unlockAccount(Player player) {
        final HibernateTemplate template = getHibernateTemplate();

        final LockAccountInfo lai = (LockAccountInfo) template.get(LockAccountInfo.class, player.getId());
        if (lai != null) {
            template.delete(lai);
            template.flush();

            for (LockAccountListener accountListener : accountListeners) {
                accountListener.accountUnlocked(player);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean isAccountLocked(final Player player) {
        final HibernateTemplate template = getHibernateTemplate();
        Date unlockDate = (Date) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                final Query query = session.createQuery(
                        "select lock.unlockDate from " + LockAccountInfo.class.getName() +
                                " lock where playerId = ?");
                query.setLong(0, player.getId());

                return query.uniqueResult();
            }
        });

        if (unlockDate != null) {
            final boolean res = System.currentTimeMillis() < unlockDate.getTime();
            if (!res) {
                template.execute(new HibernateCallback() {
                    public Object doInHibernate(Session session) throws HibernateException, SQLException {
                        final Query q = session.createQuery(
                                "delete from " + LockAccountInfo.class.getName() + " where playerId = ?");
                        q.setLong(0, player.getId());
                        q.executeUpdate();

                        for (LockAccountListener accountListener : accountListeners) {
                            accountListener.accountUnlocked(player);
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
    public LockAccountInfo getLockAccountInfo(Player player) {
        final HibernateTemplate template = getHibernateTemplate();
        template.setCacheQueries(false);

        LockAccountInfo accountInfo = (LockAccountInfo) template.get(LockAccountInfo.class, player.getId());
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
                accountInfo.setPlayer(player);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Account already isn't locked. We can remove lock. Unlock date: " +
                            accountInfo.getUnlockDate().getTime() + ". Now time: " + System.currentTimeMillis());
                }

                template.delete(accountInfo);
                template.flush();

                for (LockAccountListener accountListener : accountListeners) {
                    accountListener.accountUnlocked(player);
                }
                accountInfo = null;
            }
        }
        return accountInfo;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String isUsernameLocked(String username) {
        final HibernateTemplate template = getHibernateTemplate();
        final LockUsernameInfo name = (LockUsernameInfo) template.get(LockUsernameInfo.class, username);
        if (name != null) {
            return name.getReason();
        }
        return null;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void lockUsername(String username, String reason) {
        final HibernateTemplate template = getHibernateTemplate();

        LockUsernameInfo info = (LockUsernameInfo) template.get(LockUsernameInfo.class, username);
        if (info == null) {
            info = new LockUsernameInfo(username, reason);
            template.persist(info);
        } else {
            info.setReason(reason);
            template.update(info);
        }
        template.flush();

        for (LockUsernameListener usernameListener : usernameListeners) {
            usernameListener.usernameLocked(username, reason);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void unlockUsername(final String username) {
        final HibernateTemplate template = getHibernateTemplate();
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query q = session.createQuery(
                        "delete from " + LockUsernameInfo.class.getName() + " where username = ?");
                q.setString(0, username);
                final int removedCount = q.executeUpdate();
                if (removedCount != 0) {
                    for (LockUsernameListener usernameListener : usernameListeners) {
                        usernameListener.usernameUnlocked(username);
                    }
                }
                return removedCount;
            }
        });
    }
}
