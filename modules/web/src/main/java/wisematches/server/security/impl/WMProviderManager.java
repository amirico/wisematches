package wisematches.server.security.impl;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import wisematches.server.personality.Personality;
import wisematches.server.personality.player.PlayerManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMProviderManager extends ProviderManager {
	private PlayerManager playerManager;

	public WMProviderManager() {
	}

	@Override
	public Authentication doAuthentication(Authentication authentication) throws AuthenticationException {
		final Authentication authentication1 = super.doAuthentication(authentication);
		if (authentication1 instanceof AbstractAuthenticationToken) {
			final AbstractAuthenticationToken token = (AbstractAuthenticationToken) authentication1;
			token.setDetails(playerManager.getPlayer((Personality) authentication1.getPrincipal()));
		}
		return authentication1;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}
}