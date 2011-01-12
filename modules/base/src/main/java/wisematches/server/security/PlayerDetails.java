package wisematches.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import wisematches.core.user.Language;
import wisematches.core.user.Player;

import java.util.Collection;

/**
 * This is implementation of Spring Security {@code UserDetails} interface that extends {@code Player} interface as
 * well. So any authorized user is a player by default.
 *
 * @author klimese
 */
public class PlayerDetails extends User implements Player {
	private final Player originalPlayer;

	public PlayerDetails(Player originalPlayer, String password, boolean enabled, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(originalPlayer.getUsername(), password, enabled, true, true, accountNonLocked, authorities);
		this.originalPlayer = originalPlayer;
	}

	@Override
	public long getId() {
		return originalPlayer.getId();
	}

	@Override
	public String getUsername() {
		return originalPlayer.getUsername();
	}

	@Override
	public String getEmail() {
		return originalPlayer.getEmail();
	}

	@Override
	public Language getLanguage() {
		return originalPlayer.getLanguage();
	}

	@Override
	public int getRating() {
		return originalPlayer.getRating();
	}
}
