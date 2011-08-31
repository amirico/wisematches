package wisematches.playground.scribble.comment.impl;

import wisematches.personality.Personality;
import wisematches.playground.GameBoard;
import wisematches.playground.scribble.comment.GameComment;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "scribble_comment")
public class HibernateGameComment implements GameComment {
	@Id
	@Column(name = "id", nullable = false, updatable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "board")
	private long board;

	@Column(name = "person")
	private long person;

	@Column(name = "created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(name = "text")
	private String text;

	HibernateGameComment() {
	}

	HibernateGameComment(GameBoard board, Personality person, String text) {
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
