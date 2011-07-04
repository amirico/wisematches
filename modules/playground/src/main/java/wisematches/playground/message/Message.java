package wisematches.playground.message;

import wisematches.personality.Personality;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "message")
public class Message {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "recipient")
    private long recipient;

    @Column(name = "sender")
    private long sender;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body")
    private String body;

    protected Message() {
    }

    public Message(Personality recipient, Date created, String subject, String body) {
        this.recipient = recipient.getId();
        this.created = created;
        this.subject = subject;
        this.body = body;
    }

    public Message(Personality sender, Personality recipient, Date created, String subject, String body) {
        this.sender = sender.getId();
        this.recipient = recipient.getId();
        this.created = created;
        this.subject = subject;
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public long getSender() {
        return sender;
    }

    public Date getCreated() {
        return created;
    }

    public long getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
