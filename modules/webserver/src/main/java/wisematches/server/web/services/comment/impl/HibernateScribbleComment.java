package wisematches.server.web.services.comment.impl;

import wisematches.personality.player.Player;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.server.web.services.comment.ScribbleComment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "scribble_comment")
public class HibernateScribbleComment implements ScribbleComment {
    @Id
    @Column(name = "id")
    private byte id;

    @Column(name = "board")
    private long board;

    @Column(name = "person")
    private long person;

    @Column(name = "text")
    private String text;

    @Column(name = "creationDate")
    private Date creationDate;

    public HibernateScribbleComment() {
    }

    public HibernateScribbleComment(Player person, ScribbleBoard board, String text) {
        this.board = board.getBoardId();
        this.person = person.getId();
        this.text = text;
        this.creationDate = new Date();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getBoard() {
        return board;
    }

    @Override
    public long getPerson() {
        return person;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }
}
