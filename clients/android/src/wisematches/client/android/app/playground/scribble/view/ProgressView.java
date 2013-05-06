package wisematches.client.android.app.playground.scribble.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import wisematches.client.android.app.playground.scribble.model.ScribbleBank;
import wisematches.client.android.app.playground.scribble.model.ScribbleGame;
import wisematches.client.android.app.playground.scribble.surface.ProgressSurface;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProgressView extends View {
	private final ProgressSurface progressSurface;

	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);

		progressSurface = new ProgressSurface(context.getResources());
	}

	protected void onDraw(Canvas canvas) {
		progressSurface.onDraw(canvas, getWidth(), getHeight());
	}

	public void updateProgress(ScribbleGame board) {
		if (board.isActive()) {
			final ScribbleBank bank = board.getScribbleBank();

			final int totalTiles = bank.getLettersCount();
			final int boardTiles = board.getBoardTilesCount();

			int k = board.getPlayers().size() * 7;
			final int handTiles = (k <= totalTiles - boardTiles) ? k : totalTiles - boardTiles;

			progressSurface.updateProgress(boardTiles, handTiles, totalTiles);
		} else {
			progressSurface.finalizeProgress("Finished");
		}
	}

/*
	public void setScribbleBoard(ScribbleBoardView scribbleBoard) {
		this.scribbleBoard = scribbleBoard;
	}
*/
}