package wisematches.client.android.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import wisematches.client.android.R;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class BitmapFactory {
	private Bitmap[] tilesHighlighters = new Bitmap[11];

	private Bitmap[] tilesSelected = new Bitmap[11];
	private Bitmap[] tilesUnselected = new Bitmap[11];
	private Bitmap[] tilesPinnedSelected = new Bitmap[11];
	private Bitmap[] tilesPinnedUnselected = new Bitmap[11];

	public BitmapFactory(Resources resources) {
		final Bitmap bitmap = android.graphics.BitmapFactory.decodeResource(resources, R.drawable.board_tiles);

		final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 242, 110, true);
		for (int i = 0; i < 11; i++) {
			tilesHighlighters[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 88, 22, 22);

			tilesSelected[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 22, 22, 22);
			tilesUnselected[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 0, 22, 22);
			tilesPinnedSelected[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 66, 22, 22);
			tilesPinnedUnselected[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 44, 22, 22);
		}
	}

	public Bitmap getTileIcon(int cost) {
		return tilesUnselected[cost];
	}

	public Bitmap getHighlighter(int cost) {
		return tilesHighlighters[cost];
	}

	public Bitmap getTilePinnedIcon(int cost) {
		return tilesPinnedUnselected[cost];
	}

	public Bitmap getTileSelectedIcon(int cost) {
		return tilesSelected[cost];
	}

	public Bitmap getTilePinnedSelectedIcon(int cost) {
		return tilesPinnedSelected[cost];
	}

	public void terminate() {
		tilesHighlighters = null;

		tilesSelected = null;
		tilesUnselected = null;
		tilesPinnedSelected = null;
		tilesPinnedUnselected = null;
	}
}
