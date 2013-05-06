package wisematches.client.android.app.playground.scribble.view;

import wisematches.client.android.app.playground.scribble.model.ScribbleTile;
import wisematches.client.android.app.playground.scribble.model.ScribbleWord;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BoardViewListener {
	void onWordSelected(ScribbleWord word);

	void onTileSelected(ScribbleTile tile, boolean selected);
}
