package wisematches.playground.scribble.tracking.impl;

import org.junit.Test;
import wisematches.playground.scribble.Direction;
import wisematches.playground.scribble.Position;
import wisematches.playground.scribble.Tile;
import wisematches.playground.scribble.Word;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class StatisticsWordUserTypeTest {
	public StatisticsWordUserTypeTest() {
	}

	@Test
	public void test_nullSafeGet() throws SQLException {
		ResultSet resultSet = createMock(ResultSet.class);
		expect(resultSet.getString("a4")).andReturn(null);
		expect(resultSet.getString("a4")).andReturn("122а1|68м2|127а10|111г3|0b0");
		replay(resultSet);

		final StatisticsWordUserType type = new StatisticsWordUserType();
		assertNull(type.nullSafeGet(resultSet, new String[]{"a4"}, null, this));

		final Word o = (Word) type.nullSafeGet(resultSet, new String[]{"a4"}, null, this);
		assertNotNull(o);

		assertEquals(new Position(0, 0), o.getPosition());
		assertEquals(Direction.HORIZONTAL, o.getDirection());
		assertEquals(5, o.getTiles().length);

		final Tile[] tiles = o.getTiles();
		assertEquals(122, tiles[0].getNumber());
		assertEquals('а', tiles[0].getLetter());
		assertEquals(1, tiles[0].getCost());

		assertEquals(68, tiles[1].getNumber());
		assertEquals('м', tiles[1].getLetter());
		assertEquals(2, tiles[1].getCost());

		assertEquals(127, tiles[2].getNumber());
		assertEquals('а', tiles[2].getLetter());
		assertEquals(10, tiles[2].getCost());

		assertEquals(111, tiles[3].getNumber());
		assertEquals('г', tiles[3].getLetter());
		assertEquals(3, tiles[3].getCost());

		assertEquals(0, tiles[4].getNumber());
		assertEquals('b', tiles[4].getLetter());
		assertEquals(0, tiles[4].getCost());

		verify(resultSet);
	}

	@Test
	public void test_nullSafeSet() throws Exception {
		final Word w = new Word(new Position(3, 7), Direction.HORIZONTAL, new Tile(1, 'a', 1), new Tile(2, 'b', 1), new Tile(123, 'c', 10), new Tile(0, 'd', 0));

		final PreparedStatement ps = createStrictMock(PreparedStatement.class);
		ps.setString(12, null);
		ps.setString(12, "1a1|2b1|123c10|0d0");
		replay(ps);

		final StatisticsWordUserType type = new StatisticsWordUserType();
		type.nullSafeSet(ps, null, 12, null);

		type.nullSafeSet(ps, w, 12, null);

		verify(ps);
	}
}
