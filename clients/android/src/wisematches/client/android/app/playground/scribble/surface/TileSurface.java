package wisematches.client.android.app.playground.scribble.surface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import wisematches.client.android.app.playground.scribble.model.ScribbleTile;
import wisematches.client.android.graphics.BitmapFactory;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TileSurface {
	private boolean pinned = false;
	private boolean selected = false;

	private ScribbleTile tile;
	private final BitmapFactory bitmapFactory;

	public TileSurface(ScribbleTile tile, boolean pinned, BitmapFactory bitmapFactory) {
		this.tile = tile;
		this.pinned = pinned;
		this.bitmapFactory = bitmapFactory;
	}

	public ScribbleTile getTile() {
		return tile;
	}

	public boolean isPinned() {
		return pinned;
	}

	public void onDraw(Canvas canvas, int x, int y) {
		final Paint paint = new Paint();
		paint.setTextSize(16);
		paint.setTextAlign(Paint.Align.CENTER);
		if (pinned) {
			if (selected) {
				paint.setFakeBoldText(true);
				canvas.drawBitmap(bitmapFactory.getTilePinnedSelectedIcon(tile.getCost()), x, y, null);
			} else {
				paint.setFakeBoldText(false);
				canvas.drawBitmap(bitmapFactory.getTilePinnedIcon(tile.getCost()), x, y, null);
			}
		} else {
			if (selected) {
				paint.setFakeBoldText(true);
				canvas.drawBitmap(bitmapFactory.getTileSelectedIcon(tile.getCost()), x, y, null);
			} else {
				paint.setFakeBoldText(false);
				canvas.drawBitmap(bitmapFactory.getTileIcon(tile.getCost()), x, y, null);
			}
		}
		paint.setAntiAlias(true);
		if (tile.isWildcard()) {
			paint.setColor(Color.BLACK);
		} else {
			paint.setColor(Color.WHITE);
		}
		canvas.drawText(tile.getLetter().toUpperCase(), x + 11, y + 18, paint);
		paint.setAntiAlias(false);
	}

	public void pin() {
		pinned = true;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
