package wisematches.server.web.modules.app.playboard.memory;

import org.junit.Assert;
import org.junit.Test;
import wisematches.server.gameplaying.scribble.Direction;
import wisematches.server.gameplaying.scribble.Position;
import wisematches.server.gameplaying.scribble.Tile;
import wisematches.server.gameplaying.scribble.Word;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WordUserTypeTest {
	@Test
	public void test_nullSafeGet() throws SQLException {
/*
        ResultSet resultSet = createMock(ResultSet.class);
        expect(resultSet.getInt("a1")).andReturn(3);
        expect(resultSet.getInt("a2")).andReturn(7);
        expect(resultSet.getInt("a3")).andReturn(0);
        expect(resultSet.getString("a4")).andReturn("122а1|68м2|127а10|111г3|0b0");
        replay(resultSet);

        final WordUserType type = new WordUserType();
        final Word o = (Word) type.nullSafeGet(resultSet, new String[]{"a1", "a2", "a3", "a4"}, this);
        assertNotNull(o);

        assertEquals(new Position(3, 7), o.getPosition());
        assertEquals(Direction.VERTICAL, o.getDirection());
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
*/
		Assert.fail("Asdasd");
	}

	@Test
	public void test_nullSafeSet() throws Exception {
		final Word w = new Word(new Position(3, 7), Direction.HORIZONTAL, new Tile(1, 'a', 1), new Tile(2, 'b', 1), new Tile(123, 'c', 10), new Tile(0, 'd', 0));

		final PreparedStatement ps = createStrictMock(PreparedStatement.class);
		ps.setInt(12, 3);
		ps.setInt(13, 7);
		ps.setInt(14, 1);
		ps.setString(15, "1a1|2b1|123c10|0d0");
		replay(ps);

		final WordUserType type = new WordUserType();
		type.nullSafeSet(ps, w, 12);

		verify(ps);
	}
}
