package wisematches.server.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.server.security.impl.WMPlayerDetails;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class WMSecurityContext {
    private WMSecurityContext() {
    }

    public static Player getPrincipal() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof WMPlayerDetails) {
            WMPlayerDetails details = (WMPlayerDetails) authentication.getPrincipal();
            return details.getPlayer();
        }
        return null;
    }

    public static Personality getPersonality() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Personality) {
            return (Personality) authentication.getPrincipal();
        }
        return null;
    }
}
