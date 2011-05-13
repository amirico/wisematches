package wisematches.server.web.services.recovery;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.personality.account.Account;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RecoveryTokenManager extends HibernateDaoSupport {
	private long tokenExpirationTime = DEFAULT_EXPIRATION_TIME;

	private static final int DEFAULT_EXPIRATION_TIME = 24 * 60 * 60 * 1000;  // 1day

	public RecoveryTokenManager() {
	}

	public RecoveryToken createToken(Account player) {
		try {
			RecoveryToken token1 = getToken(player);
			if (token1 != null) {
				removeToken(token1);
			}
		} catch (TokenExpiredException e) {
			;
		}

		final RecoveryToken token = new RecoveryToken(player);
		getHibernateTemplate().persist(token);
		return token;
	}

	/**
	 * Returns cookies token for specified player.
	 *
	 * @param player the player who token should be returned.
	 * @return cookies token for specified player or <code>null</code> if player has no token.
	 * @throws TokenExpiredException if token exist but already expired
	 */
	public RecoveryToken getToken(Account player) throws TokenExpiredException {
		final RecoveryToken token = getHibernateTemplate().get(RecoveryToken.class, player.getId());
		if (token == null) {
			return null;
		}
		// check that token still alive
		if (token.getDate().getTime() + tokenExpirationTime < System.currentTimeMillis()) {
			removeToken(token);
			throw new TokenExpiredException("Token expired exception: " + token.getDate(), token.getDate());
		}
		token.setPlayer(player);
		return token;
	}

	/**
	 * Removes specified token from the storage.
	 *
	 * @param token the token to be removed
	 */
	public void removeToken(RecoveryToken token) {
		if (token == null) {
			throw new IllegalArgumentException("Removing token can't be null");
		}
		if (token != null) {
			getHibernateTemplate().delete(token);
		}
	}

	public void setTokenExpirationTime(long tokenExpirationTime) {
		this.tokenExpirationTime = tokenExpirationTime;
	}
}