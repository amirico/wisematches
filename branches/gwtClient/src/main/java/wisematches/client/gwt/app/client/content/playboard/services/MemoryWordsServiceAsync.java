package wisematches.client.gwt.app.client.content.playboard.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import wisematches.client.gwt.app.client.content.playboard.infos.MemoryWord;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface MemoryWordsServiceAsync {

    /**
     * Adds specified word to player's memory
     *
     * @param boardId the board id.
     * @param word    the word to be added.
     */
    void addMemoryWord(long boardId, MemoryWord word, AsyncCallback<Void> async);

    /**
     * Removes word from memory.
     *
     * @param boardId the board id.
     * @param word    the word to be removed.
     */
    void removeMemoryWord(long boardId, int wordNumber, AsyncCallback<Void> async);

    /**
     * Clears all words from memory.
     *
     * @param boardId the board id where memory words should be cleared.
     */
    void clearMemoryWords(long boardId, AsyncCallback<Void> async);

    /**
     * Returns array of all memory words.
     *
     * @param boardId the board id.
     * @return the array of memory words.
     */
    void getMemoryWords(long boardId, AsyncCallback<MemoryWord[]> async);
}
