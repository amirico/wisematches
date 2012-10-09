package wisematches.server.web.services.recovery;

import wisematches.personality.account.Account;

import javax.persistence.*;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "persistent_recoveries")
public final class RecoveryToken {
    @Id
    private long playerId;

    @Column(nullable = false)
    private String token;

    @Basic
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Transient
    private Account player;

    /**
     * Hibernate constructor
     */
    RecoveryToken() {
    }

    RecoveryToken(Account player) {
        if (player == null) {
            throw new IllegalArgumentException("Player can't be null");
        }
        this.player = player;
        this.playerId = player.getId();
        this.token = generateToken();
        this.date = new Date();
    }

    Account getPlayer() {
        return player;
    }

    public String getToken() {
        return token;
    }

    public Date getDate() {
        return date;
    }

    void setPlayer(Account player) {
        this.player = player;
    }

    private static String generateToken() {
        final StringBuilder b = new StringBuilder();
        while (b.length() < 10) {
            b.append((int) (Math.random() * 9));
        }
        return b.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RecoveryToken that = (RecoveryToken) o;
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
