package wisematches.playground.scribble.tracking.impl;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import wisematches.playground.scribble.Direction;
import wisematches.playground.scribble.Position;
import wisematches.playground.scribble.Tile;
import wisematches.playground.scribble.Word;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class StatisticsWordUserType implements UserType {
	private static final Pattern TILES_PATTERN = Pattern.compile("((\\d{1,3})([^\\d])(\\d{1,2})\\|?)");

	private static final int[] SQL_TYPES = new int[]{Types.VARCHAR};

	public StatisticsWordUserType() {
	}

	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	public Class returnedClass() {
		return Word.class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == null) {
			return x == y;
		}
		return x.equals(y);
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor sessionImplementor, Object o) throws HibernateException, SQLException {
		final String tilesString = rs.getString(names[0]);
		if (tilesString == null) {
			return null;
		}

		final List<Tile> tiles = new ArrayList<Tile>(15);
		final Matcher matcher = TILES_PATTERN.matcher(tilesString);

		while (matcher.find()) {
			final String s1 = matcher.group(2);
			final String s2 = matcher.group(3);
			final String s3 = matcher.group(4);

			tiles.add(new Tile(Integer.valueOf(s1), s2.charAt(0), Integer.valueOf(s3)));
		}

		return new Word(
				new Position(0, 0),
				Direction.HORIZONTAL,
				tiles.toArray(new Tile[tiles.size()]));
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
		final Word w = (Word) value;
		if (w == null) {
			st.setString(index, null);
		} else {
			String s = "";
			final Tile[] tiles = w.getTiles();
			for (Tile tile : tiles) {
				s += tile.getNumber() + "" + tile.getLetter() + "" + tile.getCost() + "|";
			}

			final int length = s.length();
			if (length == 0) {
				st.setString(index, s);
			} else {
				st.setString(index, s.substring(0, length - 1));
			}
		}
	}

	public Object deepCopy(Object value) throws HibernateException {
		if (value == null) {
			return null;
		}
		final Word w = (Word) value;
		return new Word(w.getPosition(), w.getDirection(), w.getTiles().clone());
	}

	public boolean isMutable() {
		return false;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Word) value;
	}

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}
}
