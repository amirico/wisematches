package wisematches.server.web.services.comment.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.server.web.services.comment.ScribbleComment;
import wisematches.server.web.services.comment.ScribbleCommentManager;

import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml",
		"classpath:/config/playground-config.xml",
		"classpath:/config/scribble-junit-config.xml",
		"classpath:/config/application-settings.xml",
		"classpath:/config/server-web-junit-config.xml"
})
public class HibernateScribbleCommentManagerTest {
	@Autowired
	private ScribbleCommentManager commentManager;

	public HibernateScribbleCommentManagerTest() {
	}

	@Test
	public void test() {
		final Player player1 = RobotPlayer.DULL;
		final Player player2 = RobotPlayer.EXPERT;

		final ScribbleBoard board = createMock(ScribbleBoard.class);
		expect(board.getBoardId()).andReturn(2L).times(8);
		replay(board);

		commentManager.addComment(player1, board, "This is message1");
		commentManager.addComment(player2, board, "This is message2");

		final long[] boardComments = commentManager.getBoardComments(board);
		assertEquals(2, boardComments.length);

		assertComment(commentManager.getComment(boardComments[0]), player2, 2L, "This is message2");
		assertComment(commentManager.getComment(boardComments[1]), player1, 2L, "This is message1");
		try {
			commentManager.removeComment(player1, board, boardComments[0]);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			assertEquals(2, commentManager.getBoardComments(board).length);
		}

		commentManager.removeComment(player1, board, boardComments[1]);
		assertEquals(1, commentManager.getBoardComments(board).length);

		commentManager.clearComments(board);
		assertEquals(0, commentManager.getBoardComments(board).length);

		verify(board);
	}

	private void assertComment(ScribbleComment c, Player p, long board, String text) {
		assertEquals(p.getId(), c.getPerson());
		assertEquals(board, c.getBoard());
		assertEquals(text, c.getText());
		assertNotNull(c.getCreationDate());
	}
}
