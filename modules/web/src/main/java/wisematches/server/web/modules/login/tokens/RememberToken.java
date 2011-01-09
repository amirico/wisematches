package wisematches.server.web.modules.login.tokens;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "tn_remember")
@IdClass(RememberTokenId.class)
public class RememberToken extends AbstractToken {
    @Id
    private int address;

    private RememberToken() {
    }

    public RememberToken(RememberTokenId tokenId) {
        super(tokenId.getPlayerId());
        this.address = tokenId.getAddress();
    }

    public RememberToken(RememberTokenId tokenId, String token, Date creationDate) {
        super(tokenId.getPlayerId(), token, creationDate);
        this.address = tokenId.getAddress();
    }

    public RememberTokenId getCookiesTokenId() {
        return new RememberTokenId(playerId, address);
    }


}
