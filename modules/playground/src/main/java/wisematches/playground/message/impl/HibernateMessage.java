package wisematches.playground.message.impl;

import wisematches.core.Personality;
import wisematches.playground.message.Message;

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

	@Column(name = "notification", nullable = true, updatable = false)
	private boolean notification;

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
	public HibernateMessage(Personality recipient, String text) {
		this(recipient, text, true, null, null);
	}

	/**
	 * Creates new message for specified personality from an user.
	 *
	 * @param recipient t
	 * @param text
	 * @param sender
	 */
	public HibernateMessage(Personality recipient, String text, Personality sender) {
		this(recipient, text, false, sender, null);
	}

	public HibernateMessage(Message original, String text, Personality sender) {
		this(Personality.person(original.getSender()), text, false, sender, original);
	}

	private HibernateMessage(Personality recipient, String text, boolean notification, Personality sender, Message original) {
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
		if (original != null) {
			this.original = original.getId();
		}
		this.creationDate = new Date();
		this.text = text;
		this.notification = notification;
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
		return notification;
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
