package wisematches.server.web.spring.security.guest;

import wisematches.server.player.guest.GuestPlayer;
import wisematches.server.security.WMAuthorities;
import wisematches.server.security.impl.WMUserDetails;

import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMGuestUserDetails extends WMUserDetails {
	public WMGuestUserDetails() {
		super(GuestPlayer.getPlayer(), EnumSet.of(WMAuthorities.USER, WMAuthorities.GUEST), true);
	}
}
