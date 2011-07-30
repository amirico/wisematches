package wisematches.server.web.services.friends.impl;

import wisematches.personality.Personality;
import wisematches.server.web.services.friends.FriendRelation;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "player_friends")
@IdClass(HibernateFriendRelation.PK.class)
public class HibernateFriendRelation implements FriendRelation {
    @Id
    @Column(name = "person")
    private long person;

    @Id
    @Column(name = "friend")
    private long friend;

    @Column(name = "registered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registered;

    @Column(name = "comment")
    private String comment;

    protected HibernateFriendRelation() {
    }

    protected HibernateFriendRelation(Personality person, Personality friend, String comment) {
        this.person = person.getId();
        this.friend = friend.getId();
        this.registered = new Date();
        this.comment = comment;
    }

    @Override
    public long getPerson() {
        return person;
    }

    @Override
    public long getFriend() {
        return friend;
    }

    @Override
    public Date getRegistered() {
        return registered;
    }

    @Override
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static final class PK implements Serializable {
        private long person;
        private long friend;

        public PK() {
        }

        public PK(Personality person, Personality friend) {
            this.person = person.getId();
            this.friend = friend.getId();
        }

        public long getPerson() {
            return person;
        }

        public void setPerson(long person) {
            this.person = person;
        }

        public long getFriend() {
            return friend;
        }

        public void setFriend(long friend) {
            this.friend = friend;
        }
    }
}
