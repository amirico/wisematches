package wisematches.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import wisematches.server.player.Language;
import wisematches.server.player.Membership;
import wisematches.server.player.Player;

import java.util.Collection;

/**
 * This is implementation of Spring Security {@code UserDetails} interface that extends {@code Player} interface as
 * well. So any authorized user is a player by default.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMUserDetails extends User implements Player {
	private final Player originalPlayer;

	public WMUserDetails(Player originalPlayer, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(originalPlayer.getUsername(), originalPlayer.getPassword(), true, true, true, accountNonLocked, authorities);
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
	public String getPassword() {
		return originalPlayer.getPassword();
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
	public Membership getMembership() {
		return originalPlayer.getMembership();
	}

	@Override
	public int getRating() {
		return originalPlayer.getRating();
	}
}
