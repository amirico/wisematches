package wisematches.server.web.modules.app.settings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.kernel.player.Player;

import java.sql.SQLException;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ApplicationSettingsDao extends HibernateDaoSupport {
    private static final Log log = LogFactory.getLog(ApplicationSettingsDao.class);

    public String getPlayerSettings(final Player player, final String frameViewId) {
        try {
            Object res = getHibernateTemplate().execute(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    final SQLQuery query = session.createSQLQuery("select us." + frameViewId + " from user_settings as us where playerId = ?");
                    query.setLong(0, player.getId());
                    return query.uniqueResult();
                }
            });
            return (String) res;
        } catch (DataAccessException ex) {
            log.error("settings for frame view " + frameViewId + " for player " + player + " can't be received", ex);
            return null;
        }
    }

    public void setPlayerSettings(final Player player, final String frameViewId, final String settings) {
        try {
            final long playerId = player.getId();

            getHibernateTemplate().execute(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    final SQLQuery query = session.createSQLQuery("select us." + frameViewId + " from user_settings as us where playerId = ?");
                    query.setLong(0, playerId);
                    if (query.list().size() == 0) {
                        session.createSQLQuery("insert into user_settings(playerId, " + frameViewId + ") values (?, ?)").setLong(0, playerId).setString(1, settings).executeUpdate();
                    } else {
                        session.createSQLQuery("update user_settings set " + frameViewId + " = ? where playerId = ?").setString(0, settings).setLong(1, playerId).executeUpdate();
                    }
                    return null;
                }
            });
        } catch (DataAccessException ex) {
            log.error("settings for frame view " + frameViewId + " for player " + player + " can't be stored", ex);
        }
    }
}
