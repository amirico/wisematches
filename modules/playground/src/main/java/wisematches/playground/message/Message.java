package wisematches.playground.message;

import wisematches.personality.Personality;
import wisematches.playground.GameBoard;

import javax.persistence.*;
import java.util.Date;

/**
 * This is a message class. Each message has a id, recipient and a message itself. Also message can have
 * a sender, can be associated with a game board or be replay to another message.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "player_message")
public class Message {
	@Id
	@Column(name = "id", nullable = false, updatable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "created", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(name = "recipient", nullable = false, updatable = false)
	private long recipient;

	@Column(name = "text", nullable = true, updatable = false, length = 65535)
	private String text;

	@Column(name = "notification", nullable = true, updatable = false)
	private boolean notification;

	@Column(name = "sender", nullable = true, updatable = false)
	private long sender;

	@Column(name = "original", nullable = true, updatable = false)
	private long original;

	@Column(name = "board", nullable = true, updatable = false)
	private long board;

	protected Message() {
	}

	/**
	 * Creates new notification message. This message is sent by administrator and notifies about changes, new and so one.
	 *
	 * @param recipient the recipient who will received the message.
	 * @param text	  the message
	 */
	public Message(Personality recipient, String text) {
		this(recipient, text, true, null, null, null);
	}

	/**
	 * Creates new message for specified personality from an user.
	 *
	 * @param recipient t
	 * @param text
	 * @param sender
	 */
	public Message(Personality recipient, String text, Personality sender) {
		this(recipient, text, false, sender, null, null);
	}

	public Message(Personality recipient, String text, Personality sender, GameBoard board) {
		this(recipient, text, false, sender, board, null);
	}

	public Message(Personality recipient, String text, Personality sender, Message original) {
		this(recipient, text, false, sender, null, original);
	}

	private Message(Personality recipient, String text, boolean notification, Personality sender, GameBoard board, Message original) {
		if (recipient == null) {
			throw new IllegalArgumentException("Recipient is not specified");
		}
		if (text == null || text.trim().isEmpty()) {
			throw new IllegalArgumentException("There is no message content");
		}
		if (notification && sender != null) {
			throw new IllegalArgumentException("Sender can't sent a notification");
		}
		this.recipient = recipient.getId();
		if (sender != null) {
			this.sender = sender.getId();
		}
		if (board != null) {
			this.board = board.getBoardId();
		}
		if (original != null) {
			this.original = original.getId();
		}
		this.created = new Date();
		this.text = text;
		this.notification = notification;
	}

	public long getId() {
		return id;
	}

	public Date getCreated() {
		return created;
	}

	public long getRecipient() {
		return recipient;
	}

	public String getText() {
		return text;
	}

	public boolean isNotification() {
		return notification;
	}

	public long getSender() {
		return sender;
	}

	public long getOriginal() {
		return original;
	}

	public long getBoard() {
		return board;
	}
}
