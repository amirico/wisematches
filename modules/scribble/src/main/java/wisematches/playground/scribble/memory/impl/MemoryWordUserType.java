package wisematches.playground.scribble.memory.impl;

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
 * This {@code UserType} is used to store {@link wisematches.playground.scribble.Word} object into database.
 * <p/>
 * A table that contains {@code Word} object must have four columns in following order:
 * <ol>
 * <li>row - {@code TINYINT} SQL type. Contains row of word.
 * <li>col - {@code TINYINT} SQL type. Contains column of word.
 * <li>direction - {@code TINYINT} SQL type. Contains direction of word (0 - VERTICAL, 1 - HORIZONTAL).
 * <li>word - {@code VARCHAR(104)} SQL type. Contains encoded word. ( {@code (3 + 1 + 2 + 1) * 15 - 1 = 104},
 * where 3 chars for number, 1 char for letter, 2 chars for cost, 1 char for separator, maximum 15 tiles is allowed
 * and -1 because last separator is removed.).
 * </ol>
 * <p/>
 * Each word encoded by following rule:
 * <pre>
 *   WORD := (TILE_NUMBER TILE_LETTER TILE_COST|){2,+}
 *
 *   TILE_NUMBER := INTEGER{1,3}
 *   TILE_LETTER := CHARACTER
 *   TILE_COST := INTEGER
 * </pre>
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MemoryWordUserType implements UserType {
	private static final Pattern TILES_PATTERN = Pattern.compile("((\\d{1,3})([^\\d])(\\d{1,2})\\|?)");

	private static final int[] SQL_TYPES = new int[]{Types.TINYINT, Types.TINYINT, Types.TINYINT, Types.VARCHAR};

	public MemoryWordUserType() {
	}

	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	public Class returnedClass() {
		return Word.class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return x.equals(y);
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor sessionImplementor, Object o) throws HibernateException, SQLException {
		final String tilesString = rs.getString(names[3]);

		final List<Tile> tiles = new ArrayList<Tile>(15);
		final Matcher matcher = TILES_PATTERN.matcher(tilesString);

		while (matcher.find()) {
			final String s1 = matcher.group(2);
			final String s2 = matcher.group(3);
			final String s3 = matcher.group(4);

			tiles.add(new Tile(Integer.valueOf(s1), s2.charAt(0), Integer.valueOf(s3)));
		}

		return new Word(
				new Position(rs.getInt(names[0]), rs.getInt(names[1])),
				Direction.values()[rs.getInt(names[2])],
				tiles.toArray(new Tile[tiles.size()]));
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
		final Word w = (Word) value;

		int i = index;
		st.setInt(i++, w.getPosition().row);
		st.setInt(i++, w.getPosition().column);
		st.setInt(i++, w.getDirection().ordinal());

		String s = "";
		final Tile[] tiles = w.getTiles();
		for (Tile tile : tiles) {
			s += tile.getNumber() + "" + tile.getLetter() + "" + tile.getCost() + "|";
		}

		final int length = s.length();
		if (length == 0) {
			st.setString(i, s);
		} else {
			st.setString(i, s.substring(0, length - 1));
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
