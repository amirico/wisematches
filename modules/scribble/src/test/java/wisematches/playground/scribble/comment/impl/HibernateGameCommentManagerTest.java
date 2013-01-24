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
import wisematches.core.personality.machinery.RobotPlayer;
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

import static junit.framework.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml",
		"classpath:/config/playground-config.xml",
		"classpath:/config/scribble-junit-config.xml"
})
public class HibernateGameCommentManagerTest {
	private Personality player1;
	private Personality player2;
	private ScribbleBoard board;

	@Autowired
	private GameCommentManager commentManager;

	@Autowired
	private ScribblePlayManager boardManager;

	public HibernateGameCommentManagerTest() {
	}

	@Before
	public void setUp() throws BoardCreationException {
		player1 = RobotPlayer.DULL;
		player2 = RobotPlayer.EXPERT;

		board = boardManager.createBoard(new ScribbleSettings("Mock game", Language.EN, 3, false, false), Arrays.<Personality>asList(player1, player2));
	}

	@Test
	public void testMarkReadUnread() throws BoardCreationException, GameMoveException {
		assertEquals(0, commentManager.getCommentsCount(board, player1));
		assertEquals(0, commentManager.getCommentsCount(board, player2));

		GameComment c1 = commentManager.addComment(board, player1, "This is message1");
		assertEquals(1, commentManager.getCommentsCount(board, player1));
		assertEquals(1, commentManager.getCommentsCount(board, player2));

		GameComment c2 = commentManager.addComment(board, player2, "This is message2");
		assertEquals(2, commentManager.getCommentsCount(board, player1));
		assertEquals(2, commentManager.getCommentsCount(board, player2));

		commentManager.markRead(board, player1, c1.getId());
		final List<GameCommentState> s1 = commentManager.getCommentStates(board, player1);
		assertEquals(2, s1.size());
		assertTrue(s1.get(1).isRead()); //c1
		assertFalse(s1.get(0).isRead()); //c2
		final List<GameCommentState> s2 = commentManager.getCommentStates(board, player2);
		assertEquals(2, s2.size());
		assertFalse(s2.get(1).isRead()); //c1
		assertTrue(s2.get(0).isRead()); //c2 (but true)

		commentManager.markRead(board, player2);
		final List<GameCommentState> s3 = commentManager.getCommentStates(board, player1);
		assertEquals(2, s3.size());
		assertTrue(s3.get(1).isRead());  //c1
		assertFalse(s3.get(0).isRead());  //c2
		final List<GameCommentState> s4 = commentManager.getCommentStates(board, player2);
		assertEquals(2, s4.size());
		assertTrue(s4.get(1).isRead());  //c1
		assertTrue(s4.get(0).isRead());  //c2

		commentManager.markUnread(board, player2, c2.getId());
		final List<GameCommentState> s5 = commentManager.getCommentStates(board, player1);
		assertEquals(2, s5.size());
		assertTrue(s5.get(1).isRead());  //c1
		assertFalse(s5.get(0).isRead());  //c2
		final List<GameCommentState> s6 = commentManager.getCommentStates(board, player2);
		assertEquals(2, s6.size());
		assertTrue(s6.get(1).isRead());  //c1
		assertTrue(s6.get(0).isRead());  //c2

		commentManager.removeComment(board, player1, c1.getId());
		assertEquals(1, commentManager.getCommentsCount(board, player1));
		assertEquals(1, commentManager.getCommentsCount(board, player2));

		commentManager.removeComment(board, player2, c2.getId());
		assertEquals(0, commentManager.getCommentsCount(board, player1));
		assertEquals(0, commentManager.getCommentsCount(board, player2));
	}

	@Test
	public void testGetComments() throws BoardCreationException, GameMoveException {
		assertEquals(0, commentManager.getCommentsCount(board, player1));
		assertEquals(0, commentManager.getCommentsCount(board, player2));

		final GameComment c1 = commentManager.addComment(board, player1, "This is message1");
		assertEquals(1, commentManager.getCommentsCount(board, player1));
		assertEquals(1, commentManager.getCommentsCount(board, player2));

		final GameComment c2 = commentManager.addComment(board, player2, "This is message2");
		assertEquals(2, commentManager.getCommentsCount(board, player1));
		assertEquals(2, commentManager.getCommentsCount(board, player2));

		assertEquals(2, commentManager.getComments(board, player1).size());
		assertEquals(2, commentManager.getComments(board, player2).size());
		assertEquals(2, commentManager.getComments(board, player1, c1.getId(), c2.getId()).size());

		assertEquals(1, commentManager.getComments(board, player1, c1.getId()).size());
		assertEquals(1, commentManager.getComments(board, player1, c1.getId(), c1.getId()).size());

		assertNull(commentManager.removeComment(board, player1, c2.getId())); // no remove
		assertEquals(2, commentManager.getComments(board, player1).size());
		assertEquals(2, commentManager.getCommentsCount(board, player1));
		assertEquals(2, commentManager.getCommentsCount(board, player2));

		assertNotNull(commentManager.removeComment(board, player1, c1.getId())); // removed
		assertEquals(1, commentManager.getComments(board, player1).size());
		assertEquals(1, commentManager.getCommentsCount(board, player1));
		assertEquals(1, commentManager.getCommentsCount(board, player2));

		commentManager.clearComments(board);
		assertEquals(0, commentManager.getCommentsCount(board, player1));
		assertEquals(0, commentManager.getCommentsCount(board, player2));
		assertEquals(0, commentManager.getComments(board, player1).size());
	}
}
