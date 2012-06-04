package wisematches.server.web.security.spring.fixes;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMPersistentTokenRepository extends JdbcTokenRepositoryImpl {
	/**
	 * The default SQL used by <tt>removeUserTokens</tt>
	 */
	public static final String DEF_REMOVE_SERIES_SQL =
			"delete from persistent_logins where series = ?";

	public WMPersistentTokenRepository() {
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		try {
			return getJdbcTemplate().queryForObject(DEF_TOKEN_BY_SERIES_SQL, new RowMapper<PersistentRememberMeToken>() {
				public PersistentRememberMeToken mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new PersistentRememberMeToken(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4));
				}
			}, seriesId);
		} catch (EmptyResultDataAccessException noOne) {
			logger.info("Querying token for series '" + seriesId + "' returned empty result.");
		} catch (IncorrectResultSizeDataAccessException moreThanOne) {
			logger.error("Querying token for series '" + seriesId + "' returned more than one value. Series" +
					" should be unique");
		} catch (DataAccessException e) {
			logger.error("Failed to load token for series " + seriesId, e);
		}
		return null;
	}

	public void removeToken(String seriesId) {
		getJdbcTemplate().update(DEF_REMOVE_SERIES_SQL, seriesId);
	}
}
