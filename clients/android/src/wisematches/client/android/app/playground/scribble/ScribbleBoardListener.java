package wisematches.client.android.app.playground.scribble;

import wisematches.client.android.app.playground.scribble.model.ScribbleTile;
import wisematches.client.android.app.playground.scribble.model.ScribbleWord;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ScribbleBoardListener {
	void onTileSelected(ScribbleTile tile, boolean selected);

	void onWordSelected(ScribbleWord word);
}
