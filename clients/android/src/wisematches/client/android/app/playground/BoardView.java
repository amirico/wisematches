package wisematches.client.android.app.playground;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.model.BonusCell;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardView extends FrameLayout {
	private final Bitmap boardBitmap;

	private final Bitmap[] tilesPlacement = new Bitmap[11];

	private final Bitmap[] tilesSelected = new Bitmap[11];
	private final Bitmap[] tilesUnselected = new Bitmap[11];
	private final Bitmap[] tilesPinnedSelected = new Bitmap[11];
	private final Bitmap[] tilesPinnedUnselected = new Bitmap[11];

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setWillNotDraw(false);
		boardBitmap = createBoardBitmap();

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.board_tiles);
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 242, 110, true);
		for (int i = 0; i < 11; i++) {
			tilesPlacement[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 88, 22, 22);

			tilesSelected[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 0, 22, 22);
			tilesUnselected[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 22, 22, 22);
			tilesPinnedSelected[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 44, 22, 22);
			tilesPinnedUnselected[i] = Bitmap.createBitmap(scaledBitmap, 22 * i, 66, 22, 22);
		}

		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	Point p;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		final int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				p = new Point((int) event.getX(), (int) event.getY());
				break;
			case MotionEvent.ACTION_MOVE:
				p.set((int) event.getX(), (int) event.getY());
				break;
			case MotionEvent.ACTION_UP:
				p = null;
				break;
		}
		invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(boardBitmap, 0, 0, null);

		if (p != null) {
			canvas.drawBitmap(tilesSelected[3], p.x, p.y, null);
		}

		canvas.translate(13, 13);
		for (int i = 0; i < tilesPlacement.length; i++) {
			canvas.drawBitmap(tilesPlacement[i], 0, 22 * i, null);

			canvas.drawBitmap(tilesSelected[i], 22, 22 * i, null);
			canvas.drawBitmap(tilesUnselected[i], 44, 22 * i, null);
			canvas.drawBitmap(tilesPinnedSelected[i], 66, 22 * i, null);
			canvas.drawBitmap(tilesPinnedUnselected[i], 88, 22 * i, null);
		}
	}

	private Bitmap createBoardBitmap() {
		final Bitmap bitmap = Bitmap.createBitmap(356, 370, Bitmap.Config.ARGB_8888);

		final Canvas canvas = new Canvas(bitmap);
		final Paint paint = new Paint();
		paint.setStrokeWidth(1f);

		paint.setARGB(0xff, 0xca, 0xdb, 0xe1);
		canvas.drawRect(0, 0, 356, 356, paint);

		paint.setARGB(0xff, 0xda, 0xec, 0xf2);
		canvas.drawRect(1, 1, 354, 354, paint);

		paint.setARGB(0xff, 0x00, 0x00, 0x00);
		canvas.drawRect(13, 13, 341, 341, paint);

		paint.setARGB(0xff, 0x00, 0x00, 0x00);
		canvas.drawRect(new Rect(10, 10, 346, 346), paint);

		paint.setARGB(0xff, 0xff, 0xff, 0xff);
		canvas.drawRect(11, 11, 345, 345, paint);

		LinearGradient gradient = new LinearGradient(12, 12, 344, 344, 0xFF1947d2, 0xFF75b0f1, Shader.TileMode.CLAMP);
		Paint p = new Paint();
		p.setShader(gradient);
		canvas.drawRect(12, 12, 344, 344, p);

		final char[] asd = "АБВГДЕЖЗИКЛМНОП".toCharArray();
		paint.setTextSize(8);
		paint.setAntiAlias(true);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setARGB(0xff, 0x00, 0x00, 0x00);
		for (int i = 0; i < 15; i++) {
			canvas.drawText(String.valueOf(asd[i]), 5, 26 + 22 * i, paint);
			canvas.drawText(String.valueOf(i + 1), 24 + 22 * i, 9, paint);
			canvas.drawText(String.valueOf(asd[i]), 350, 26 + 22 * i, paint);
			canvas.drawText(String.valueOf(i + 1), 24 + 22 * i, 354, paint);
		}
		paint.setAntiAlias(false);

		paint.setARGB(0xff, 0xff, 0xff, 0xff);
		for (int i = 0; i < 15; i++) {
			canvas.drawLine(24 + 22 * i, 12, 24 + 22 * i, 344, paint);
			canvas.drawLine(12, 24 + 22 * i, 344, 24 + 22 * i, paint);

			for (int j = 0; j < 15; j++) {
				canvas.drawLine((24 + 22 * i) - 2, (24 + 22 * j) - 1, (24 + 22 * i) + 3, (24 + 22 * j) - 1, paint);
				canvas.drawLine((24 + 22 * i) - 1, (24 + 22 * j) - 2, (24 + 22 * i) + 2, (24 + 22 * j) - 2, paint);
				canvas.drawLine((24 + 22 * i) - 2, (24 + 22 * j) + 1, (24 + 22 * i) + 3, (24 + 22 * j) + 1, paint);
				canvas.drawLine((24 + 22 * i) - 1, (24 + 22 * j) + 2, (24 + 22 * i) + 2, (24 + 22 * j) + 2, paint);
			}
		}

		Path path = new Path();
		path.moveTo(70, 345);
		path.lineTo(70 + 24, 345 + 24);
		path.lineTo(286 - 24, 345 + 24);
		path.lineTo(286, 345);
		path.close();

		paint.setARGB(0xFF, 0x55, 0x8b, 0xe7);
		canvas.drawPath(path, paint);

		paint.setColor(Color.BLACK);
		canvas.drawLine(70, 345, 70 + 24, 345 + 24, paint);
		canvas.drawLine(70 + 24, 345 + 24, 286 - 24, 345 + 24, paint);
		canvas.drawLine(286 - 24, 345 + 24, 286, 345, paint);

		paint.setColor(Color.WHITE);
		canvas.drawLine(71, 345, 70 + 24, 344 + 24, paint);
		canvas.drawLine(70 + 24, 344 + 24, 286 - 24, 344 + 24, paint);
		canvas.drawLine(286 - 24, 344 + 24, 285, 345, paint);

		BonusCell[] cells = new BonusCell[]{
				new BonusCell(0, 0, BonusCell.Type.L2),
				new BonusCell(3, 3, BonusCell.Type.L2),
				new BonusCell(5, 5, BonusCell.Type.W2),
				new BonusCell(1, 1, BonusCell.Type.L3),
		};

		paint.setTextSize(11);
		paint.setAntiAlias(true);
		paint.setTextAlign(Paint.Align.CENTER);
		for (BonusCell cell : cells) {
			int i = cell.getRow();
			int j = cell.getColumn();

			final float radius = 10f;
			paint.setColor(cell.getType().getColor());
			canvas.drawCircle(24 + 22 * i, 24 + 22 * j, radius, paint);
			canvas.drawCircle(332 - 22 * i, 332 - 22 * j, radius, paint);
			canvas.drawCircle(24 + 22 * i, 332 - 22 * j, radius, paint);
			canvas.drawCircle(332 - 22 * i, 24 + 22 * j, radius, paint);

			paint.setARGB(0xff, 0x00, 0x00, 0x00);
			canvas.drawText(cell.getType().name(), 24 + 22 * i, 28 + 22 * j, paint);
			canvas.drawText(cell.getType().name(), 332 - 22 * i, 336 - 22 * j, paint);
			canvas.drawText(cell.getType().name(), 24 + 22 * i, 336 - 22 * j, paint);
			canvas.drawText(cell.getType().name(), 332 - 22 * i, 28 + 22 * j, paint);
		}
		return bitmap;
	}
}
