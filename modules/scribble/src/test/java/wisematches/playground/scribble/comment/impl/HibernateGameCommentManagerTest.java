package wisematches.playground.scribble.comment.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.playground.BoardCreationException;
import wisematches.playground.GameMoveException;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayManager;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.playground.scribble.comment.GameCommentManager;
import wisematches.playground.scribble.comment.GameCommentState;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/properties-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/gameplay-config.xml",
		"classpath:/config/scribble-config.xml"
})
public class HibernateGameCommentManagerTest {
	private Player player1;
	private Player player2;

	private ScribbleBoard board;

	@Autowired
	private GameCommentManager commentManager;

	@Autowired
	private ScribblePlayManager boardManager;

	public HibernateGameCommentManagerTest() {
	}

	@Before
	public void setUp() throws BoardCreationException {
		player1 = new DefaultMember(901, null, null, null, null, null);
		player2 = new DefaultMember(902, null, null, null, null, null);

		board = boardManager.createBoard(new ScribbleSettings("Mock game", Language.EN, 3, false, false), Arrays.<Personality>asList(player1, player2), null);
	}

	@Test
	public void testMarkReadUnread() throws BoardCreationException, GameMoveException {
		assertEquals(0, commentManager.getTotalCount(player1, board));
		assertEquals(0, commentManager.getTotalCount(player2, board));

		GameComment c1 = commentManager.addComment(board, player1, "This is message1");
		assertEquals(1, commentManager.getTotalCount(player1, board));
		assertEquals(1, commentManager.getTotalCount(player2, board));

		GameComment c2 = commentManager.addComment(board, player2, "This is message2");
		assertEquals(2, commentManager.getTotalCount(player1, board));
		assertEquals(2, commentManager.getTotalCount(player2, board));

		commentManager.markRead(board, player1, c1.getId());
		final List<GameCommentState> s1 = commentManager.searchEntities(player1, board, null, null);
		assertEquals(2, s1.size());
		assertTrue(s1.get(1).isRead()); //c1
		assertFalse(s1.get(0).isRead()); //c2
		final List<GameCommentState> s2 = commentManager.searchEntities(player2, board, null, null);
		assertEquals(2, s2.size());
		assertFalse(s2.get(1).isRead()); //c1
		assertTrue(s2.get(0).isRead()); //c2 (but true)

		commentManager.markRead(board, player2);
		final List<GameCommentState> s3 = commentManager.searchEntities(player1, board, null, null);
		assertEquals(2, s3.size());
		assertTrue(s3.get(1).isRead());  //c1
		assertFalse(s3.get(0).isRead());  //c2
		final List<GameCommentState> s4 = commentManager.searchEntities(player2, board, null, null);
		assertEquals(2, s4.size());
		assertTrue(s4.get(1).isRead());  //c1
		assertTrue(s4.get(0).isRead());  //c2

		commentManager.markUnread(board, player2, c2.getId());
		final List<GameCommentState> s5 = commentManager.searchEntities(player1, board, null, null);
		assertEquals(2, s5.size());
		assertTrue(s5.get(1).isRead());  //c1
		assertFalse(s5.get(0).isRead());  //c2
		final List<GameCommentState> s6 = commentManager.searchEntities(player2, board, null, null);
		assertEquals(2, s6.size());
		assertTrue(s6.get(1).isRead());  //c1
		assertTrue(s6.get(0).isRead());  //c2

		commentManager.removeComment(board, player1, c1.getId());
		assertEquals(1, commentManager.getTotalCount(player1, board));
		assertEquals(1, commentManager.getTotalCount(player2, board));

		commentManager.removeComment(board, player2, c2.getId());
		assertEquals(0, commentManager.getTotalCount(player1, board));
		assertEquals(0, commentManager.getTotalCount(player2, board));
	}

	@Test
	public void testGetComments() throws BoardCreationException, GameMoveException {
		assertEquals(0, commentManager.getTotalCount(player1, board));
		assertEquals(0, commentManager.getTotalCount(player2, board));

		final GameComment c1 = commentManager.addComment(board, player1, "This is message1");
		assertEquals(1, commentManager.getTotalCount(player1, board));
		assertEquals(1, commentManager.getTotalCount(player2, board));

		final GameComment c2 = commentManager.addComment(board, player2, "This is message2");
		assertEquals(2, commentManager.getTotalCount(player1, board));
		assertEquals(2, commentManager.getTotalCount(player2, board));

		assertEquals(2, commentManager.getComments(board, player1).size());
		assertEquals(2, commentManager.getComments(board, player2).size());
		assertEquals(2, commentManager.getComments(board, player1, c1.getId(), c2.getId()).size());

		assertEquals(1, commentManager.getComments(board, player1, c1.getId()).size());
		assertEquals(1, commentManager.getComments(board, player1, c1.getId(), c1.getId()).size());

		assertNull(commentManager.removeComment(board, player1, c2.getId())); // no remove
		assertEquals(2, commentManager.getComments(board, player1).size());
		assertEquals(2, commentManager.getTotalCount(player1, board));
		assertEquals(2, commentManager.getTotalCount(player2, board));

		assertNotNull(commentManager.removeComment(board, player1, c1.getId())); // removed
		assertEquals(1, commentManager.getComments(board, player1).size());
		assertEquals(1, commentManager.getTotalCount(player1, board));
		assertEquals(1, commentManager.getTotalCount(player2, board));

		commentManager.clearComments(board);
		assertEquals(0, commentManager.getTotalCount(player1, board));
		assertEquals(0, commentManager.getTotalCount(player2, board));
		assertEquals(0, commentManager.getComments(board, player1).size());
	}
}
