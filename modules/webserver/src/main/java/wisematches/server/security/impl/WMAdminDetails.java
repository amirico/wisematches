package wisematches.server.security.impl;

import org.springframework.security.core.userdetails.User;
import wisematches.server.security.WMAuthorities;

import java.util.Collections;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMAdminDetails extends User {
    public WMAdminDetails(String password) {
        super("admin", password, Collections.singleton(WMAuthorities.ADMIN));
    }

    public String getNickname() {
        return getUsername();
    }
}
