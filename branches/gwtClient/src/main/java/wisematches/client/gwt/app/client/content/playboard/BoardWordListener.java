package wisematches.client.gwt.app.client.content.playboard;

import wisematches.server.games.scribble.core.Word;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface BoardWordListener {
    void wordSelected(Word word);

    void wordDeselected(Word word);
}
