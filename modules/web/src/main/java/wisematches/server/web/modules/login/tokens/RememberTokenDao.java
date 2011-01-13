package wisematches.server.web.modules.login.tokens;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.server.player.Player;

import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RememberTokenDao extends HibernateDaoSupport {
	public RememberToken createToken(Player player, String remoteAddress) {
		RememberToken token = new RememberToken(new RememberTokenId(player.getId(), remoteAddress));
		final HibernateTemplate template = getHibernateTemplate();
		template.persist(token);
		template.flush();
		return token;
	}

	/**
	 * Returns remember token for specified player.
	 *
	 * @param player	the player who token should be returned.
	 * @param ipAddress the address of client ip
	 * @return cookies token for specified player or <code>null</code> if player has no token.
	 */
	@SuppressWarnings("unchecked")
	public RememberToken getToken(Player player, String ipAddress) {
		return (RememberToken) getHibernateTemplate().get(
				RememberToken.class, new RememberTokenId(player.getId(), ipAddress));
	}

	public void removeToken(RememberToken token) {
		final HibernateTemplate template = getHibernateTemplate();
		template.delete(token);
		template.flush();
	}

	public void clearTokens(Date date) {
		//TODO: not implemented
		throw new UnsupportedOperationException("Not implemented");
	}
}
