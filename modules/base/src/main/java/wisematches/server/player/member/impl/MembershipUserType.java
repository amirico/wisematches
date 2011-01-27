package wisematches.server.player.member.impl;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import wisematches.server.player.Membership;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MembershipUserType implements UserType {
	private static final int[] SQL_TYPES = new int[]{Types.VARCHAR};

	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	public Class returnedClass() {
		return Membership.class;
	}

	public boolean equals(Object x, Object y) {
		return x == y;
	}

	public int hashCode(Object x) {
		return x.hashCode();
	}

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		final String s = rs.getString(names[0]);
		if (s == null) {
			return null;
		}
		return Membership.valueOf(s.toUpperCase());
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, Types.VARCHAR);
		} else {
			st.setString(index, ((Membership) value).name().toLowerCase());
		}
	}

	public Object deepCopy(Object value) {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	public Serializable disassemble(Object value) {
		return (Membership) value;
	}

	public Object assemble(Serializable cached, Object owner) {
		return cached;
	}

	public Object replace(Object original, Object target, Object owner) {
		return original;
	}
}
