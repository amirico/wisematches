package wisematches.server.web.services.comment.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.guest.GuestPlayer;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.server.web.services.comment.ScribbleCommentManager;

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
        final Player player = GuestPlayer.GUEST;

        final ScribbleBoard board = createMock(ScribbleBoard.class);
        expect(board.getBoardId()).andReturn(2L);
        replay(board);

        commentManager.addComment(player, board, "This is message");

        verify(board);

        throw new UnsupportedOperationException("Not implemented");
    }
}
