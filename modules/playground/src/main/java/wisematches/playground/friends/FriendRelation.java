package wisematches.playground.friends;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface FriendRelation {
    long getPerson();

    long getFriend();

    Date getRegistered();

    String getComment();
}