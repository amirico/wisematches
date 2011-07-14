package wisematches.playground.message;

import wisematches.personality.Personality;
import wisematches.playground.GameBoard;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MessageManager {
    void addMessageListener(MessageListener l);

    void removeMessageListener(MessageListener l);

    /**
     * Sent notification to specified player with specified body
     *
     * @param recipient the player who should received the message
     * @param body      the message's body
     * @throws NullPointerException     if recipient is null
     * @throws IllegalArgumentException if recipient is unknown or {@code body} is empty.
     */
    void sendNotification(Personality recipient, String body);

    /**
     * Sent a message to specified player
     *
     * @param sender    the sender of the message
     * @param recipient the player who should received the message
     * @param body      the message's body
     * @throws NullPointerException     if sender or recipient is null
     * @throws IllegalArgumentException if body is null or empty
     */
    void sendMessage(Personality sender, Personality recipient, String body);

    /**
     * @param sender    the sender of the message
     * @param recipient the player who should received the message
     * @param body      the message's body
     * @param board     the associated game board
     * @throws NullPointerException     if sender, recipient or board is null
     * @throws IllegalArgumentException if body is null or empty
     */
    void sendMessage(Personality sender, Personality recipient, String body, GameBoard board);

    /**
     * Replay to specified message.
     *
     * @param sender  the sender of the message
     * @param message the original message
     * @param body    the message's body
     * @throws NullPointerException     if sender, recipient or message is null
     * @throws IllegalArgumentException if body is null or empty
     */
    void replayMessage(Personality sender, Message message, String body);

    /**
     * Returns a message with specified id.
     *
     * @param id the message id
     * @return the message or {@code null} if message unknown
     */
    Message getMessage(long id);

    /**
     * Returns collection of all message for specified person.
     *
     * @param person the person who's message should be returned.
     * @return the collection of all message.
     */
    Collection<Message> getMessages(Personality person);

    /**
     * Removes a message with specified id
     *
     * @param messageId the message id
     */
    void removeMessage(long messageId);

    /**
     * Removes all message for specified {@code person} received from specified {@code player}
     *
     * @param sender    the sender of the message
     * @param recipient the player who should received the message
     */
    void removeMessages(Personality sender, Personality recipient);

    /**
     * Clear all message for specified person.
     *
     * @param person the person who's message should be removed
     */
    void clearMessages(Personality person);
}
