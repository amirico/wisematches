package wisematches.client.gwt.login.client.services;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum RestorePasswordResult implements IsSerializable {
    SUCCESS,
    UNKNOWN_PLAYER,
    INVALID_TOKEN,
    TOKEN_EXPIRED
}
