package wisematches.client.android.app.playground.scribble.surface;

import android.content.res.Resources;
import android.graphics.*;
import wisematches.client.android.R;
import wisematches.client.android.app.playground.scribble.model.ScoreBonus;
import wisematches.client.android.app.playground.scribble.model.ScribbleGame;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardSurface {
	private final Resources resources;
	private final ScribbleGame board;

	private final Bitmap boardBackground;

	private final Rect handRegion = new Rect(102, 346, 254, 378);
	private final Rect boardRegion = new Rect(13, 13, 342, 342);

	public BoardSurface(ScribbleGame board, Resources resources) {
		this.board = board;
		this.resources = resources;

		this.boardBackground = createBoardView();
	}

	public Rect getHandRegion() {
		return handRegion;
	}

	public Rect getBoardRegion() {
		return boardRegion;
	}

	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(boardBackground, 0, 0, null);
	}

	private Bitmap createBoardView() {
		final Bitmap bitmap = Bitmap.createBitmap((int) (356), (int) (370), Bitmap.Config.ARGB_8888);

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

		final char[] asd = resources.getString(R.string.board_surface_captions).toCharArray();
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

		paint.setTextSize(11);
		paint.setTextAlign(Paint.Align.CENTER);

		paint.setColor(Color.WHITE);
		for (int i = 0; i < 15; i++) {
			canvas.drawLine(24 + 22 * i, 12, 24 + 22 * i, 344, paint);
			canvas.drawLine(12, 24 + 22 * i, 344, 24 + 22 * i, paint);
		}

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				final ScoreBonus bonus = board.getScoreEngine().getScoreBonus(i, j);
				if (bonus != null) {
					paint.setAntiAlias(true);
					int r = bonus.getRow();
					int c = bonus.getColumn();

					final float radius = 10f;
					paint.setColor(bonus.getType().getColor());
					canvas.drawCircle(24 + 22 * r, 24 + 22 * c, radius, paint);
					canvas.drawCircle(332 - 22 * r, 332 - 22 * c, radius, paint);
					canvas.drawCircle(24 + 22 * r, 332 - 22 * c, radius, paint);
					canvas.drawCircle(332 - 22 * r, 24 + 22 * c, radius, paint);

					paint.setColor(Color.BLACK);

					final String caption = resources.getString(resources.getIdentifier("board_surface_bonus_" + bonus.getType().name(), "string", "wisematches.client.android"));
					canvas.drawText(caption, 24 + 22 * r, 28 + 22 * c, paint);
					canvas.drawText(caption, 332 - 22 * r, 336 - 22 * c, paint);
					canvas.drawText(caption, 24 + 22 * r, 336 - 22 * c, paint);
					canvas.drawText(caption, 332 - 22 * r, 28 + 22 * c, paint);
					paint.setAntiAlias(false);
				} else {
					paint.setColor(Color.WHITE);
					canvas.drawLine((24 + 22 * i) - 2, (24 + 22 * j) - 1, (24 + 22 * i) + 3, (24 + 22 * j) - 1, paint);
					canvas.drawLine((24 + 22 * i) - 1, (24 + 22 * j) - 2, (24 + 22 * i) + 2, (24 + 22 * j) - 2, paint);
					canvas.drawLine((24 + 22 * i) - 2, (24 + 22 * j) + 1, (24 + 22 * i) + 3, (24 + 22 * j) + 1, paint);
					canvas.drawLine((24 + 22 * i) - 1, (24 + 22 * j) + 2, (24 + 22 * i) + 2, (24 + 22 * j) + 2, paint);
				}
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

		return bitmap;
	}
}
