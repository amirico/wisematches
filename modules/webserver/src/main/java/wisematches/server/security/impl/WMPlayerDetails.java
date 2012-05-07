package wisematches.server.security.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.server.security.WMAuthorities;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMPlayerDetails extends Personality implements UserDetails {
    private final Player player;
    private final String username;
    private final String password;
    private final boolean accountNonLocked;
    private final Collection<GrantedAuthority> authorities;

    public WMPlayerDetails(Player player, String username, String password, boolean accountNonLocked) {
        super(player.getId());
        this.player = player;
        this.username = username;
        this.password = password;
        this.accountNonLocked = accountNonLocked;
        authorities = WMAuthorities.forMembership(player.getMembership());
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getNickname() {
        return player.getNickname();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
