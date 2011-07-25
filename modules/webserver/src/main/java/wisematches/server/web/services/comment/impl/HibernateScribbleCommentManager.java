package wisematches.server.web.services.comment.impl;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.personality.player.Player;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.server.web.services.comment.ScribbleComment;
import wisematches.server.web.services.comment.ScribbleCommentManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateScribbleCommentManager extends HibernateDaoSupport implements ScribbleCommentManager {

    public HibernateScribbleCommentManager() {
    }

    @Override
    public void addComment(Player player, ScribbleBoard board, String text) {
        getHibernateTemplate().saveOrUpdate(new HibernateScribbleComment(player, board, text));
    }

    @Override
    public void removeComment(Player player, ScribbleBoard board, long commentId) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public long[] getBoardComments(ScribbleBoard board) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public ScribbleComment getComment(long commentId) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }
}
