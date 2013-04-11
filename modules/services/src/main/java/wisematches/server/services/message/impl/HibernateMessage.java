package wisematches.server.services.message.impl;

import wisematches.core.Player;
import wisematches.server.services.message.Message;

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
public class HibernateMessage implements Message {
	@Id
	@Column(name = "id", nullable = false, updatable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false, updatable = false)
	private Date creationDate;

	@Column(name = "recipient", nullable = false, updatable = false)
	private long recipient;

	@Column(name = "text", nullable = true, updatable = false, length = 65535)
	private String text;

	@Column(name = "sender", nullable = true, updatable = false)
	private long sender;

	@Column(name = "original", nullable = true, updatable = false)
	private long original;

	@Column(name = "state")
	private int state = 0;

	protected HibernateMessage() {
	}

	/**
	 * Creates new notification message. This message is sent by administrator and notifies about changes, new and so one.
	 *
	 * @param recipient the recipient who will received the message.
	 * @param text      the message
	 */
	public HibernateMessage(Player recipient, String text) {
		this(recipient.getId(), text, 0, null);
	}

	public HibernateMessage(Player recipient, String text, Player sender) {
		this(recipient.getId(), text, sender.getId(), null);
	}

	public HibernateMessage(Message original, String text, Player sender) {
		this(original.getSender(), text, sender.getId(), original);
	}

	private HibernateMessage(long recipient, String text, long sender, Message original) {
		if (text == null || text.trim().isEmpty()) {
			throw new IllegalArgumentException("There is no message content");
		}
		this.recipient = recipient;
		this.sender = sender;
		if (original != null) {
			this.original = original.getId();
		}
		this.creationDate = new Date();
		this.text = text;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public long getRecipient() {
		return recipient;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public boolean isNotification() {
		return sender == 0;
	}

	@Override
	public long getSender() {
		return sender;
	}

	@Override
	public long getOriginal() {
		return original;
	}

	int getState() {
		return state;
	}

	void setState(int state) {
		this.state = state;
	}
}
