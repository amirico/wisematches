package wisematches.client.gwt.app.client.content.playboard.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import wisematches.client.gwt.app.client.content.playboard.infos.MemoryWord;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface MemoryWordsService extends RemoteService {
    /**
     * Adds specified word to player's memory
     *
     * @param boardId the board id.
     * @param word    the word to be added.
     */
    void addMemoryWord(long boardId, MemoryWord word);

    /**
     * Removes word from memory.
     *
     * @param boardId the board id.
     * @param word    the word to be removed.
     */
    void removeMemoryWord(long boardId, int wordNumber);

    /**
     * Clears all words from memory.
     *
     * @param boardId the board id where memory words should be cleared.
     */
    void clearMemoryWords(long boardId);

    /**
     * Returns array of all memory words.
     *
     * @param boardId the board id.
     * @return the array of memory words.
     */
    MemoryWord[] getMemoryWords(long boardId);

    /**
     * Utility/Convenience class.
     * Use MemoryWordsService.App.getInstance() to access static instance of MemoryWordsServiceAsync
     */
    public static class App {
        private static final MemoryWordsServiceAsync ourInstance;

        static {
            ourInstance = (MemoryWordsServiceAsync) GWT.create(MemoryWordsService.class);
            ((ServiceDefTarget) ourInstance).setServiceEntryPoint("/rpc/MemoryWordsService");
        }

        public static MemoryWordsServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
