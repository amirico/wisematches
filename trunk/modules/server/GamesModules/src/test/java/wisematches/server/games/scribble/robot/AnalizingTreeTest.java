package wisematches.server.games.scribble.robot;

import static org.junit.Assert.*;
import org.junit.Test;
import wisematches.server.games.scribble.board.ScribbleBoard;
import wisematches.server.games.scribble.core.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class AnalizingTreeTest {
    private WorkTile[] tiles = new WorkTile[]{
            new WorkTile(new Tile(1, 'a', 1), null), //0
            new WorkTile(new Tile(2, 'b', 1), null), //1
            new WorkTile(new Tile(3, 'c', 1), null), //2
            new WorkTile(new Tile(4, 'e', 1), new Position(7, 7)), //3
            new WorkTile(new Tile(5, 'f', 1), new Position(7, 8)), //4
            new WorkTile(new Tile(6, 'f', 1), new Position(7, 9)), //5
            new WorkTile(new Tile(7, 'r', 1), new Position(9, 8)), //6
    };

    @Test
    public void test_analizingTreeItem() {
        AnalizingTree.AnalizingTreeItem item1 = new AnalizingTree.AnalizingTreeItem(null, tiles[0], null);
        AnalizingTree.AnalizingTreeItem item2 = new AnalizingTree.AnalizingTreeItem(item1, tiles[1], null);

        AnalizingTree.AnalizingTreeItem item3 = new AnalizingTree.AnalizingTreeItem(item2, tiles[3], Direction.VERTICAL);
        assertEquals(7, item3.getRow());
        assertEquals(7, item3.getColumn());
        assertEquals(new Position(5, 7), item3.getWord(false).getPosition());
        assertEquals(Direction.VERTICAL, item3.getWord(false).getDirection());
        assertEquals(3, item3.getWord(false).getTiles().length);

        AnalizingTree.AnalizingTreeItem item4 = new AnalizingTree.AnalizingTreeItem(item2, tiles[3], Direction.HORIZONTAL);
        assertEquals(7, item4.getRow());
        assertEquals(7, item4.getColumn());
        assertEquals(new Position(7, 5), item4.getWord(false).getPosition());
        assertEquals(Direction.HORIZONTAL, item4.getWord(false).getDirection());
        assertEquals(3, item4.getWord(false).getTiles().length);

        AnalizingTree.AnalizingTreeItem item5 = new AnalizingTree.AnalizingTreeItem(item4, tiles[2], Direction.HORIZONTAL);
        assertEquals(7, item5.getRow());
        assertEquals(8, item5.getColumn());
        assertEquals(new Position(7, 5), item5.getWord(false).getPosition());
        assertEquals(Direction.HORIZONTAL, item5.getWord(false).getDirection());
        assertEquals(4, item5.getWord(false).getTiles().length);
    }

    @Test
    public void test_offerFirstChar() {
        TilesPlacement tp = new MockTilesPlacement(Arrays.asList(tiles));

        final AnalizingTree analizingTree = new AnalizingTree(tp, Arrays.asList(tiles));
        assertTrue(analizingTree.offerNextChar('a'));

        assertEquals(analizingTree.getCurrentLevel(), 1);
        assertEquals(analizingTree.getCurrentWord(), "a");

        assertTrue(analizingTree.offerNextChar('f'));
        assertEquals(analizingTree.getCurrentLevel(), 2);
        assertEquals(analizingTree.getCurrentWord(), "af");
        assertEquals(2, analizingTree.getAcceptableWords().size());

        assertTrue(analizingTree.offerNextChar('c'));
        assertEquals(analizingTree.getCurrentLevel(), 3);
        assertEquals(analizingTree.getCurrentWord(), "afc");
        assertEquals(2, analizingTree.getAcceptableWords().size());

        //There is no tile with 'h' letter.
        assertFalse(analizingTree.offerNextChar('h'));
        assertEquals(analizingTree.getCurrentLevel(), 4);
        assertEquals(analizingTree.getCurrentWord(), "afch");
        assertEquals(0, analizingTree.getAcceptableWords().size());

        //Rollbank to level 3
        analizingTree.rollback(3);
        assertEquals(analizingTree.getCurrentLevel(), 3);
        assertEquals(analizingTree.getCurrentWord(), "afc");
        assertEquals(2, analizingTree.getAcceptableWords().size());

        // add letter 'r': now one word.
        assertTrue(analizingTree.offerNextChar('r'));
        assertEquals(analizingTree.getCurrentLevel(), 4);
        assertEquals(analizingTree.getCurrentWord(), "afcr");
        assertEquals(1, analizingTree.getAcceptableWords().size());

        Word word = analizingTree.getAcceptableWords().get(0);
        assertEquals(new Position(6, 8), word.getPosition());
        assertEquals(Direction.VERTICAL, word.getDirection());
        assertEquals(4, word.getTiles().length);

        //Rollbank to level 3
        analizingTree.rollback(3);

        // add letter 'c': letter already busy and can't be added 
        assertFalse(analizingTree.offerNextChar('c'));
        assertEquals(analizingTree.getCurrentLevel(), 4);
        assertEquals(analizingTree.getCurrentWord(), "afcc");
        assertEquals(0, analizingTree.getAcceptableWords().size());

        //Rollbank to level 3
        analizingTree.rollback(3);

        // add letter 'c': letter already busy and can't be added 
        assertTrue(analizingTree.offerNextChar('b'));
        assertEquals(analizingTree.getCurrentLevel(), 4);
        assertEquals(analizingTree.getCurrentWord(), "afcb");
        assertEquals(1, analizingTree.getAcceptableWords().size());

        word = analizingTree.getAcceptableWords().get(0);
        assertEquals(new Position(6, 9), word.getPosition());
        assertEquals(Direction.VERTICAL, word.getDirection());
        assertEquals(4, word.getTiles().length);
    }

    @Test
    public void test_offerWildcard() {
        final List<WorkTile> tiles1 = new ArrayList<WorkTile>(Arrays.asList(tiles));
        tiles1.add(new WorkTile(new Tile(8, '*', 0), null));
        tiles1.add(new WorkTile(new Tile(9, 's', 0), new Position(8, 9)));

        TilesPlacement tp = new MockTilesPlacement(tiles1);

        final AnalizingTree analizingTree = new AnalizingTree(tp, tiles1);
        assertTrue(analizingTree.offerNextChar('a'));
        assertTrue(analizingTree.offerNextChar('f'));
        assertEquals(4, analizingTree.getAcceptableWords().size());

        assertTrue(analizingTree.offerNextChar('s'));
        assertEquals(analizingTree.getCurrentLevel(), 3);
        assertEquals(analizingTree.getCurrentWord(), "afs");
        assertEquals(4, analizingTree.getAcceptableWords().size());

        Word word = analizingTree.getAcceptableWords().get(0);
        assertEquals(new Position(6, 8), word.getPosition());
        assertEquals(Direction.VERTICAL, word.getDirection());
        assertEquals(3, word.getTiles().length);

        assertEquals(8, word.getTiles()[2].getNumber());
        assertEquals('s', word.getTiles()[2].getLetter());

        word = analizingTree.getAcceptableWords().get(1);
        assertEquals(new Position(6, 9), word.getPosition());
        assertEquals(Direction.VERTICAL, word.getDirection());
        assertEquals(3, word.getTiles().length);

        word = analizingTree.getAcceptableWords().get(1);
        assertEquals(new Position(6, 9), word.getPosition());
        assertEquals(Direction.VERTICAL, word.getDirection());
        assertEquals(3, word.getTiles().length);

        word = analizingTree.getAcceptableWords().get(3);
        assertEquals(new Position(8, 7), word.getPosition());
        assertEquals(Direction.HORIZONTAL, word.getDirection());
        assertEquals(3, word.getTiles().length);

        //There is no tile with 'h' letter.
        assertTrue(analizingTree.offerNextChar('l'));
        assertEquals(analizingTree.getCurrentLevel(), 4);
        assertEquals(analizingTree.getCurrentWord(), "afsl");
        assertEquals(1, analizingTree.getAcceptableWords().size());

        word = analizingTree.getAcceptableWords().get(0);
        assertEquals(new Position(6, 9), word.getPosition());
        assertEquals(Direction.VERTICAL, word.getDirection());
        assertEquals(4, word.getTiles().length);

        assertEquals(8, word.getTiles()[3].getNumber());
        assertEquals('l', word.getTiles()[3].getLetter());
        assertEquals(0, word.getTiles()[3].getCost());
    }

    @Test
    public void test_simpleFirstMove() {
        for (int j = 0; j < 1000; j++) {
            final List<WorkTile> tiles1 = new ArrayList<WorkTile>();
            tiles1.add(new WorkTile(new Tile(1, 'a', 1), null));
            tiles1.add(new WorkTile(new Tile(2, 'b', 1), null));
            tiles1.add(new WorkTile(new Tile(3, 'c', 1), null));

            final TilesPlacement tp = new MockTilesPlacement(tiles1);

            final AnalizingTree analizingTree = new AnalizingTree(tp, tiles1);
            assertTrue(analizingTree.offerNextChar('a'));
            assertTrue(analizingTree.offerNextChar('b'));
            assertTrue(analizingTree.offerNextChar('c'));

            assertEquals(1, analizingTree.getAcceptableWords().size());

            final Word actual = analizingTree.getAcceptableWords().get(0);
            assertNotNull(actual.getDirection());
            assertCenterWord(actual);
        }
    }

    @Test
    public void test_longFirstMove() {
        for (int j = 0; j < 1000; j++) {
            char startChar = 'a';
            final List<WorkTile> tiles1 = new ArrayList<WorkTile>();
            for (int i = 0; i < 7; i++) {
                tiles1.add(new WorkTile(new Tile(i, startChar, 1), null));
                startChar += 1;
            }
            final TilesPlacement tp = new MockTilesPlacement(tiles1);

            final AnalizingTree analizingTree = new AnalizingTree(tp, tiles1);
            for (int i = 0; i < 7; i++) {
                startChar -= 1;
                assertTrue(analizingTree.offerNextChar(startChar));
            }

            assertEquals(1, analizingTree.getAcceptableWords().size());

            final Word actual = analizingTree.getAcceptableWords().get(0);
            if (actual.getDirection() != Direction.VERTICAL && actual.getDirection() != Direction.HORIZONTAL) {
                fail("Unknown direction");
            }

            assertCenterWord(actual);
        }
    }

    @Test
    public void test_wordOutOfBOard() {
        final List<WorkTile> workTiles = Arrays.asList(
                new WorkTile(new Tile(44, 'n', 1), new Position(7, 7)),
                new WorkTile(new Tile(35, 'o', 1), new Position(8, 7)),
                new WorkTile(new Tile(43, 'n', 1), new Position(9, 7)),
                new WorkTile(new Tile(78, 'e', 1), new Position(10, 7)),
                new WorkTile(new Tile(16, 't', 1), new Position(11, 7)),
                new WorkTile(new Tile(5, 'w', 1), null),
                new WorkTile(new Tile(91, 'a', 1), null),
                new WorkTile(new Tile(61, 'i', 1), null),
                new WorkTile(new Tile(49, 'l', 1), null),
                new WorkTile(new Tile(98, '*', 1), null),
                new WorkTile(new Tile(99, '*', 1), null),
                new WorkTile(new Tile(63, 'h', 1), null)
        );

        /*
            Bug was that tree accept word 'ag' at place [6;9] and vertical direction.
            But position [8;9] is busy of 'r' tile.
         */
        final TilesPlacement tp = new MockTilesPlacement(workTiles);

        final AnalizingTree tree = new AnalizingTree(tp, workTiles);
        tree.offerNextChar('t'); //11
        tree.offerNextChar('w'); //12
        tree.offerNextChar('i'); //13
        tree.offerNextChar('l'); //14
        tree.offerNextChar('h'); //15
        tree.offerNextChar('a'); //16
    }

    @Test
    public void test_noTilesFromHand() {
        final List<WorkTile> workTiles = Arrays.asList(
                new WorkTile(new Tile(44, 'n', 1), new Position(7, 7)),
                new WorkTile(new Tile(35, 'o', 1), new Position(8, 7)),
                new WorkTile(new Tile(43, 'n', 1), new Position(9, 7)),
                new WorkTile(new Tile(78, 'e', 1), new Position(10, 7)),
                new WorkTile(new Tile(16, 't', 1), new Position(11, 7))
        );

        final TilesPlacement tp = new MockTilesPlacement(workTiles);

        final AnalizingTree tree = new AnalizingTree(tp, workTiles);
        tree.offerNextChar('n');
        tree.offerNextChar('o');
        tree.offerNextChar('n');
        tree.offerNextChar('e');
        tree.offerNextChar('t');

        assertEquals(0, tree.getAcceptableWords().size());
    }

    @Test
    public void test_realExample() {
        // 'hello' word placed horizontally at position (7;5)
        final List<WorkTile> workTiles = Arrays.asList(
                new WorkTile(new Tile(1, 'h', 1), new Position(7, 5)),
                new WorkTile(new Tile(2, 'e', 1), new Position(7, 6)),
                new WorkTile(new Tile(3, 'l', 1), new Position(7, 7)),
                new WorkTile(new Tile(4, 'l', 1), new Position(7, 8)),
                new WorkTile(new Tile(5, 'o', 1), new Position(7, 9)),
                new WorkTile(new Tile(6, 'a', 1), null),
                new WorkTile(new Tile(7, 'b', 1), null),
                new WorkTile(new Tile(8, 'c', 1), null),
                new WorkTile(new Tile(9, 'd', 1), null),
                new WorkTile(new Tile(10, '*', 0), null)
        );

        final TilesPlacement tp = new MockTilesPlacement(workTiles);

        final AnalizingTree tree = new AnalizingTree(tp, workTiles);
        tree.offerNextChar('a');
        tree.offerNextChar('h');
        tree.offerNextChar('e');
        tree.offerNextChar('d');
        assertEquals(2, tree.getAcceptableWords().size());

        tree.offerNextChar('c');
        assertEquals(2, tree.getAcceptableWords().size());

        tree.rollback(0);
        tree.offerNextChar('l');
        tree.offerNextChar('o');
        assertEquals(3, tree.getAcceptableWords().size());

        tree.offerNextChar('a');
        assertEquals(5, tree.getAcceptableWords().size());

        tree.offerNextChar('d');
        assertEquals(6, tree.getAcceptableWords().size());
    }

    @Test
    public void test_realExample2() {
        final List<WorkTile> workTiles = Arrays.asList(
                new WorkTile(new Tile(91, 'a', 1), new Position(6, 9)),
                new WorkTile(new Tile(46, 'm', 1), new Position(7, 7)),
                new WorkTile(new Tile(90, 'a', 1), new Position(7, 8)),
                new WorkTile(new Tile(23, 'r', 1), new Position(7, 9)),
                new WorkTile(new Tile(70, 'e', 1), new Position(7, 10)),
                new WorkTile(new Tile(64, 'g', 1), new Position(7, 11)),
                new WorkTile(new Tile(77, 'e', 1), new Position(7, 12)),
                new WorkTile(new Tile(54, 'i', 1), null),
                new WorkTile(new Tile(93, 'a', 1), null),
                new WorkTile(new Tile(75, 'e', 1), null),
                new WorkTile(new Tile(99, '*', 1), null),
                new WorkTile(new Tile(16, 't', 1), null),
                new WorkTile(new Tile(28, 'q', 1), null),
                new WorkTile(new Tile(17, 't', 1), null)
        );

        /*
            Bug was that tree accept word 'ag' at place [6;9] and vertical direction.
            But position [8;9] is busy of 'r' tile. 
         */
        final TilesPlacement tp = new MockTilesPlacement(workTiles);

        final AnalizingTree tree = new AnalizingTree(tp, workTiles);
        tree.offerNextChar('a');
        tree.offerNextChar('g');

        assertEquals(1, tree.getAcceptableWords().size());
    }

    @Test
    public void test_realExampole3() {
        final List<WorkTile> workTiles = Arrays.asList(
                new WorkTile(new Tile(19, 's', 1), new Position(0, 13)),
                new WorkTile(new Tile(76, 'e', 1), new Position(1, 13)),
                new WorkTile(new Tile(91, 'a', 1), new Position(2, 13)),
                new WorkTile(new Tile(25, 'r', 1), new Position(3, 13)),
                new WorkTile(new Tile(100, 't', 1), new Position(4, 8)),
                new WorkTile(new Tile(24, 'r', 1), new Position(4, 10)),
                new WorkTile(new Tile(0, 'z', 1), null),
                new WorkTile(new Tile(87, 'b', 1), null),
                new WorkTile(new Tile(78, 'e', 1), null),
                new WorkTile(new Tile(84, 'd', 1), null),
                new WorkTile(new Tile(9, 'u', 1), null),
                new WorkTile(new Tile(50, 'l', 1), null),
                new WorkTile(new Tile(103, '*', 0), null)
        );
        /*
            Bug was that tree accept word 'ag' at place [6;9] and vertical direction.
            But position [8;9] is busy of 'r' tile.
         */
        final TilesPlacement tp = new MockTilesPlacement(workTiles);

        final AnalizingTree tree = new AnalizingTree(tp, workTiles);
        tree.offerNextChar('b');
        tree.offerNextChar('a');
        tree.offerNextChar('l');
        tree.offerNextChar('u');
        tree.offerNextChar('s');
        tree.offerNextChar('t');
        tree.offerNextChar('e');
        tree.offerNextChar('r');

        assertEquals(0, tree.getAcceptableWords().size());
    }


    private void assertCenterWord(Word actual) {
        boolean hasCenterCell = false;
        for (Word.IteratorItem item : actual) {
            if (item.getRow() == ScribbleBoard.CENTER_CELL && item.getColumn() == ScribbleBoard.CENTER_CELL) {
                hasCenterCell = true;
                break;
            }
        }
        assertTrue("First word placed not in center: " + actual, hasCenterCell);
    }
}
