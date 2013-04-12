package wisematches.client.android.app.playground.surface;

import android.graphics.Bitmap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TileBitmapFactory {
	private final Bitmap[] tilesHighlighters = new Bitmap[11];

	private final Bitmap[] tilesSelected = new Bitmap[11];
	private final Bitmap[] tilesUnselected = new Bitmap[11];
	private final Bitmap[] tilesPinnedSelected = new Bitmap[11];
	private final Bitmap[] tilesPinnedUnselected = new Bitmap[11];

	public TileBitmapFactory(Bitmap bitmap) {
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 242, 110, true);
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

	public Bitmap getTileSelectedIcon(int cost) {
		return tilesSelected[cost];
	}

	public Bitmap getTilePinnedIcon(int cost) {
		return tilesPinnedUnselected[cost];
	}

	public Bitmap getTilePinnedSelectedIcon(int cost) {
		return tilesPinnedSelected[cost];
	}

	public Bitmap getHighlighter(int cost) {
		return tilesHighlighters[cost];
	}
}
