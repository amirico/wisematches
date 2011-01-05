package wisematches.server.web.modules.login.tokens;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Embeddable
public class RememberTokenId implements Serializable {
    private long playerId;
    private int address;

    private static final int BYTE_MASK = 0xFF;

    public RememberTokenId() {
    }

    public RememberTokenId(long playerId, String ipAddress) {
        this(playerId, decode(ipAddress));
    }

    public RememberTokenId(long playerId, int address) {
        this.playerId = playerId;
        this.address = address;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    protected static int decode(String ipAddress) {
        final Pattern pattern = Pattern.compile("\\b(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\b");

        final Matcher matcher = pattern.matcher(ipAddress);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Specified string is not IP address");
        }

        int res = 0;
        final int i = matcher.groupCount();
        for (int j = 1; j <= i; j++) { //first group is all string
            int v = Integer.valueOf(matcher.group(j));
            res |= v << (i - j) * 8;
        }
        return res;
    }

    protected static String encode(int address) {
        StringBuilder b = new StringBuilder();
        b.append((address >> 32) & BYTE_MASK);
        b.append('.');
        b.append((address >> 16) & BYTE_MASK);
        b.append('.');
        b.append((address >> 8) & BYTE_MASK);
        b.append('.');
        b.append(address & BYTE_MASK);
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

        RememberTokenId that = (RememberTokenId) o;
        return address == that.address && playerId == that.playerId;
    }

    @Override
    public int hashCode() {
        int result = (int) (playerId ^ (playerId >>> 32));
        result = 31 * result + address;
        return result;
    }
}
