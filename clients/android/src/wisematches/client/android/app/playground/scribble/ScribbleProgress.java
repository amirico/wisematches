package wisematches.client.android.app.playground.scribble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;
import wisematches.client.android.app.playground.scribble.model.ScribbleGame;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleProgress extends View {
	private final int bankTiles = 56;
	private final int boardTiles = 53;
	private final int totalTiles = 123;

	private static final int RADII = 8;

	private static final ShapeDrawable LEFT = new ShapeDrawable(new RoundRectShape(new float[]{RADII, RADII, 0, 0, 0, 0, RADII, RADII}, null, null));
	private static final ShapeDrawable CENTER = new ShapeDrawable(new RectShape());
	private static final ShapeDrawable RIGHT = new ShapeDrawable(new RoundRectShape(new float[]{0, 0, RADII, RADII, RADII, RADII, 0, 0}, null, null));

	public ScribbleProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onDraw(Canvas canvas) {
		final int width = getWidth();
		final int height = getHeight();

		final float bankPercents = bankTiles / (float) totalTiles;
		final float boardPercents = boardTiles / (float) totalTiles;

		int bankSize = (int) (width * bankPercents);
		int boardSize = (int) (width * boardPercents);

		drawPart(canvas, LEFT, 1, 1, boardSize - 1, height - 1, 0xFF1aa21a, 0xFF137c13);
		drawPart(canvas, CENTER, boardSize, 1, bankSize + bankSize - 1, height - 1, 0xFFec7500, 0xFFc46100);
		drawPart(canvas, RIGHT, bankSize + bankSize, 1, width - 1, height - 1, 0xFFdcd900, 0xFFa19e01);

		final Paint textPaint = new Paint();
		textPaint.setColor(0xFF666666);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setFakeBoldText(true);
		final float densityMultiplier = getContext().getResources().getDisplayMetrics().density;
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(12f * densityMultiplier);

		int xPos = (width / 2);
		int yPos = (int) ((height / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));

		String text = boardTiles + "/" + bankTiles + "/" + (totalTiles - boardTiles - bankTiles);
		canvas.drawText(text, xPos, yPos, textPaint);
	}

	private void drawPart(Canvas canvas, final ShapeDrawable shape, int x, int y, int right, int bottom, final int background, final int border) {
		shape.setBounds(x, y, right, bottom);

		final Paint paint = shape.getPaint();
		paint.setStrokeWidth(1);

		paint.setStyle(Paint.Style.FILL);
		paint.setColor(background);
		shape.draw(canvas);

		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(border);
		shape.draw(canvas);
	}

	public void setA(ScribbleGame game) {
	}
}