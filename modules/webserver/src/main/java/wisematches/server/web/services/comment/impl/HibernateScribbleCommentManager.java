package wisematches.server.web.services.comment.impl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.personality.player.Player;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.server.web.services.comment.ScribbleComment;
import wisematches.server.web.services.comment.ScribbleCommentManager;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateScribbleCommentManager extends HibernateDaoSupport implements ScribbleCommentManager {

    private static final long[] EMPTY_ARRAY = new long[0];

    public HibernateScribbleCommentManager() {
    }

    @Override
    public ScribbleComment addComment(Player player, ScribbleBoard board, String text) {
        HibernateScribbleComment comment = new HibernateScribbleComment(player, board, text);
        getHibernateTemplate().save(comment);
        return comment;
    }

    @Override
    public void removeComment(Player player, ScribbleBoard board, long commentId) {
        HibernateTemplate template = getHibernateTemplate();
        HibernateScribbleComment comment = template.get(HibernateScribbleComment.class, commentId);
        if (comment != null) {
            if (comment.getPerson() != player.getId()) {
                throw new IllegalArgumentException("Comment with id " + commentId + " belongs to another player");
            }
            if (comment.getBoard() != board.getBoardId()) {
                throw new IllegalArgumentException("Comment with id " + commentId + " belongs to another board");
            }
            template.delete(comment);
        } else {
            throw new IllegalArgumentException("There is no comment with id " + commentId);
        }
    }

    @Override
    public long[] getBoardComments(ScribbleBoard board) {
        List l = getHibernateTemplate().find("select id from wisematches.server.web.services.comment.impl.HibernateScribbleComment where board=? order by creationDate desc, id desc", board.getBoardId());
        if (l.size() == 0) {
            return EMPTY_ARRAY;
        }

        int i = 0;
        final long[] res = new long[l.size()];
        for (Object o : l) {
            res[i++] = (Long) o;
        }
        return res;
    }

    @Override
    public ScribbleComment getComment(long commentId) {
        return getHibernateTemplate().get(HibernateScribbleComment.class, commentId);
    }

    @Override
    public void clearComments(final ScribbleBoard board) {
        getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                final Query query = session.createQuery("delete wisematches.server.web.services.comment.impl.HibernateScribbleComment where board=?");
                query.setLong(0, board.getBoardId());
                return query.executeUpdate();
            }
        });
    }
}
