package wisematches.client.gwt.login.client.services;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class SigninToken implements Serializable, IsSerializable {
    private long playerId;
    private String token;

    public SigninToken() {
    }

    public SigninToken(long playerId, String token) {
        this.playerId = playerId;
        this.token = token;
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getToken() {
        return token;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SigninToken that = (SigninToken) o;
        return playerId == that.playerId && token.equals(that.token);
    }

    @Override
    public int hashCode() {
        int result;
        result = (int) (playerId ^ (playerId >>> 32));
        result = 31 * result + token.hashCode();
        return result;
    }

    public String toString() {
        return "SigninToken{playerId=" + playerId + ", token=" + token + "}";
    }
}
