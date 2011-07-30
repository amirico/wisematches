package wisematches.server.web.services.friends;

import wisematches.personality.Personality;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface FriendsManager {
    void addFriendsListener(FriendsListener l);

    void removeFriendsListener(FriendsListener l);


    void addFriend(Personality person, Personality friend, String comment);

    void removeFriend(Personality person, Personality friend);

    Collection<FriendRelation> getFriendsList(Personality person);
}
