package wisematches.server.core.account.impl;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.kernel.player.Player;
import wisematches.server.core.account.PlayerListener;
import wisematches.server.core.account.PlayerManager;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernatePlayerManager extends HibernateDaoSupport implements PlayerManager {
    private final Collection<PlayerListener> playerListeners = new CopyOnWriteArraySet<PlayerListener>();

    public void addPlayerListener(PlayerListener l) {
        playerListeners.add(l);
    }

    public void removePlayerListener(PlayerListener l) {
        playerListeners.remove(l);
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public Player getPlayer(long playerId) {
        return (Player) getHibernateTemplate().get(PlayerImpl.class, playerId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updatePlayer(Player player) {
        final HibernateTemplate template = getHibernateTemplate();
        template.update(player);
        template.flush();

        for (PlayerListener playerListener : playerListeners) {
            playerListener.playerUpdated(player);
        }
    }
}