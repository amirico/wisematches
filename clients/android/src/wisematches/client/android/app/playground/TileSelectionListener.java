package wisematches.client.android.app.playground;

import wisematches.client.android.app.playground.scribble.model.ScribbleTile;
import wisematches.client.android.app.playground.scribble.model.ScribbleWord;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TileSelectionListener {
	void onTileSelected(ScribbleTile tile, boolean selected);

	void onWordSelected(ScribbleWord word);
}
