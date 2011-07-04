package wisematches.playground.message;

import wisematches.personality.Personality;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MessageManager {
    void addMessageListener(MessageListener l);

    void removeMessageListener(MessageListener l);


    /**
     * Sents new message to specified player
     *
     * @param recipient the player who should receive message
     * @param subject   the subject
     * @param body      the body
     */
    void sendMessage(Personality recipient, String subject, String body);

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
     * Clear all message for specified person.
     *
     * @param person the person who's message should be removed
     */
    void clearMessages(Personality person);
}
