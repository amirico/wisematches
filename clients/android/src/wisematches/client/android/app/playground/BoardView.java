package wisematches.client.android.app.playground;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.model.ScribbleBord;
import wisematches.client.android.app.playground.model.Tile;
import wisematches.client.android.app.playground.surface.BoardSurface;
import wisematches.client.android.app.playground.surface.TileBitmapFactory;
import wisematches.client.android.app.playground.surface.TileSurface;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardView extends FrameLayout {
	private ScribbleBord scribbleBord;

	private static final int TILE_SIZE = 22;

	private BoardSurface boardSurface;
	private TileBitmapFactory tileBitmapFactory;

	private TileSurface draggingTile = null;
	private final Point draggingCell = new Point();
	private final Point draggingOffset = new Point();
	private final Point draggingAnchor = new Point();
	private final Point draggingPosition = new Point();

	private final TileSurface[] handTileSurfaces = new TileSurface[7];
	private final TileSurface[][] boardTileSurfaces = new TileSurface[15][15];


	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setWillNotDraw(false);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	public void setScribbleBoard(ScribbleBord scribbleBord) {
		boardSurface = new BoardSurface(getResources(), scribbleBord.getScoreEngine());
		tileBitmapFactory = new TileBitmapFactory(BitmapFactory.decodeResource(getResources(), R.drawable.board_tiles));

		for (int i = 0; i < 7; i++) {
			handTileSurfaces[i] = new TileSurface(new Tile(8, 12, "Б"), tileBitmapFactory);
		}
		handTileSurfaces[0] = new TileSurface(new Tile(1, 12, "А"), tileBitmapFactory);
		handTileSurfaces[1] = new TileSurface(new Tile(2, 12, "Б"), tileBitmapFactory);

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				boardTileSurfaces[i][j] = new TileSurface(new Tile(8, 12, "Б"), true, tileBitmapFactory);
			}
		}
		boardTileSurfaces[0][0] = new TileSurface(new Tile(2, 12, "Б"), tileBitmapFactory);
//		boardTileSurfaces[0][1] = new TileSurface(new Tile(2, 12, "Б"), tileBitmapFactory);
//		boardTileSurfaces[1][0] = new TileSurface(new Tile(2, 12, "Б"), tileBitmapFactory);
//		boardTileSurfaces[1][1] = new TileSurface(new Tile(2, 12, "Б"), tileBitmapFactory);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		final int x = (int) event.getX();
		final int y = (int) event.getY();

		final int row = (y - 12) / TILE_SIZE;
		final int col = (x - 12) / TILE_SIZE;

		final int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				if (row == 15 && col >= 4 && col <= 11) {
					draggingTile = handTileSurfaces[col - 4];

					if (draggingTile != null) {
						draggingAnchor.set(col, row);

						draggingTile.setSelected(true);
						draggingCell.set(col, row);
						draggingOffset.set(x - col * TILE_SIZE - 12, y - row * TILE_SIZE - 12);
						draggingPosition.set(x, y);

						handTileSurfaces[col - 4] = null;
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				draggingCell.set(col, row);
				draggingPosition.set(x, y);
				break;
			case MotionEvent.ACTION_UP:
				if (draggingTile == null) {
					if (row >= 0 && row <= 14 && col >= 0 && col <= 14) {
						final TileSurface tileSurface = boardTileSurfaces[row][col];
						if (tileSurface != null) {
							tileSurface.setSelected(!tileSurface.isSelected());
						}
					}
				} else {
					if (boardTileSurfaces[row][col] == null) {
						boardTileSurfaces[row][col] = draggingTile;
						draggingTile = null;
					} else {
						draggingTile.setSelected(false);
						handTileSurfaces[draggingAnchor.x - 4] = draggingTile;
					}
				}
				draggingTile = null;
				break;
		}
		invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (boardSurface != null) {
			boardSurface.onDraw(canvas);
		}

		for (int row = 0; row < boardTileSurfaces.length; row++) {
			TileSurface[] boardTileSurface = boardTileSurfaces[row];
			for (int col = 0; col < boardTileSurface.length; col++) {
				TileSurface tileSurface = boardTileSurface[col];
				if (tileSurface != null) {
					tileSurface.onDraw(canvas, 13 + col * TILE_SIZE, 13 + row * TILE_SIZE);
				}
			}
		}

		for (int i = 0; i < handTileSurfaces.length; i++) {
			TileSurface handTileSurface = handTileSurfaces[i];
			if (handTileSurface != null) {
				handTileSurface.onDraw(canvas, 13 + TILE_SIZE * 4 + i * TILE_SIZE, 15 + TILE_SIZE * 15);
			}
		}

		if (draggingTile != null) {
			canvas.drawBitmap(tileBitmapFactory.getHighlighter(draggingTile.getTile().getCost()), 12 + draggingCell.x * TILE_SIZE, 12 + draggingCell.y * TILE_SIZE, null);
			draggingTile.onDraw(canvas, draggingPosition.x - draggingOffset.x + 3, draggingPosition.y - draggingOffset.y + 3);
		}

/*
		if (tileHighlighter != null) {
			canvas.drawBitmap(tilesSelected[3], tileHighlighter.x, tileHighlighter.y, null);
		}

		for (int i = 0; i < tilesPlacement.length; i++) {
			canvas.drawBitmap(tilesPlacement[i], 0, 22 * i, null);

			canvas.drawBitmap(tilesSelected[i], 22, 22 * i, null);
			canvas.drawBitmap(tilesUnselected[i], 44, 22 * i, null);
			canvas.drawBitmap(tilesPinnedSelected[i], 66, 22 * i, null);
			canvas.drawBitmap(tilesPinnedUnselected[i], 88, 22 * i, null);
		}
*/
	}
}
