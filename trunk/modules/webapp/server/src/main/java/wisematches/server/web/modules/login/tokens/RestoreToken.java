package wisematches.server.web.modules.login.tokens;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "tn_restore")
public class RestoreToken extends AbstractToken {
    private RestoreToken() {
    }

    public RestoreToken(long playerId) {
        super(playerId);
    }

    public RestoreToken(long playerId, String token, Date date) {
        super(playerId, token, date);
    }
}
