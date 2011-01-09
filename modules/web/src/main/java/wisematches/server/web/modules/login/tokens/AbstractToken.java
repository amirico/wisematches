package wisematches.server.web.modules.login.tokens;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@MappedSuperclass
public class AbstractToken {
    @Id
    protected long playerId;

    @Column(nullable = false)
    protected String token;

    @Basic
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date date;

    protected AbstractToken() {
    }

    public AbstractToken(long playerId) {
        this(playerId, generateToken(), new Date());
    }

    public AbstractToken(long playerId, String token, Date date) {
        if (token == null) {
            throw new IllegalArgumentException("token can't be null");
        }
        if (date == null) {
            throw new IllegalArgumentException("date can't be null");
        }
        this.playerId = playerId;
        this.token = token;
        this.date = date;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    protected static String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractToken that = (AbstractToken) o;
        return playerId == that.playerId && date.equals(that.date) && token.equals(that.token);
    }

    @Override
    public int hashCode() {
        int result;
        result = (int) (playerId ^ (playerId >>> 32));
        result = 31 * result + token.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

    public String toString() {
        return "Token{playerId=" + playerId + ", token=" + token + ", date=" + date + "}";
    }
}