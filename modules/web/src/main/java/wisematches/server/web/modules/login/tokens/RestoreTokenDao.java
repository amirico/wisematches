package wisematches.server.web.modules.login.tokens;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.kernel.player.Player;

import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RestoreTokenDao extends HibernateDaoSupport {
    public RestoreToken createToken(Player player) {
        RestoreToken token = new RestoreToken(player.getId());
        final HibernateTemplate template = getHibernateTemplate();
        template.persist(token);
        template.flush();
        return token;
    }


    /**
     * Returns cookies token for specified player.
     *
     * @param player the player who token should be returned.
     * @return cookies token for specified player or <code>null</code> if player has no token.
     */
    @SuppressWarnings("unchecked")
    public RestoreToken getToken(Player player) {
        return (RestoreToken) getHibernateTemplate().get(RestoreToken.class, player.getId());
    }

    public void removeToken(RestoreToken token) {
        final HibernateTemplate template = getHibernateTemplate();
        template.delete(token);
        template.flush();
    }

    public void clearTokens(Date date) {
        //TODO: not implemented
        throw new UnsupportedOperationException("Not implemented");
    }
}