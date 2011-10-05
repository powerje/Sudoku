package com.powerje.sudoku.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class Square extends View {
	private static final Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint mAnswerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint mCluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	{
		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setStrokeWidth(4);
		mBorderPaint.setColor(Color.GRAY);

		mTrianglePaint.setStyle(Paint.Style.FILL);
		mTrianglePaint.setColor(Color.BLACK);

		mAnswerPaint.setStyle(Paint.Style.FILL);
		mAnswerPaint.setTextSize(50);
		mAnswerPaint.setTextAlign(Paint.Align.CENTER);
		mAnswerPaint.setColor(Color.RED);

		mCluePaint.setStyle(Paint.Style.FILL);
		mCluePaint.setTextSize(25);
		mCluePaint.setTextAlign(Paint.Align.CENTER);
		mCluePaint.setColor(Color.RED);
	}

	public static int[] mClues = { 0, 0 };
	private int mUserAnswer;
	private int mAnswer;
	
	private boolean mIsAnswerable;
	private boolean mShowAnswer;
	
	private Context mContext;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	
		int w = this.getWidth();
		int h = this.getHeight();
		canvas.drawRGB(255, 255, 255);
		canvas.drawRect(0, 0, w, h, mBorderPaint);
		
		if (mIsAnswerable) {
			// draw big number in center, perhaps with an annoying color
			if (mUserAnswer != 0) {
				canvas.drawText("" + mUserAnswer, w / 2, (int) (h * .75), mAnswerPaint);
			}
		} else if (mShowAnswer) {
			canvas.drawText("" + mAnswer, w / 2, (int) (h * .75), mAnswerPaint);
		} else {
			boolean drawBorder = false;
			
			if (mClues[0] > 0) {
				// draw bottom hint
				canvas.drawText("" + mClues[0], (int) (w * .3), (int) (h * .9),
						mCluePaint);
				drawBorder = true;
			} else {
				// black out bottom half
				Path bottomTriangle = new Path();
				bottomTriangle.setFillType(Path.FillType.EVEN_ODD);
				bottomTriangle.lineTo(0, h);
				bottomTriangle.lineTo(w, h);
				bottomTriangle.close();
				canvas.drawPath(bottomTriangle, mTrianglePaint);
			}
			if (mClues[1] > 0) {
				// draw top hint
				canvas.drawText("" + mClues[1], (int) (w * .7),
						(int) (h * .45), mCluePaint);
				drawBorder = true;
			} else {
				// black out top half
				Path bottomTriangle = new Path();
				bottomTriangle.setFillType(Path.FillType.EVEN_ODD);
				bottomTriangle.lineTo(w, 0);
				bottomTriangle.lineTo(w, h);
				bottomTriangle.close();
				canvas.drawPath(bottomTriangle, mTrianglePaint);
			}
			if (drawBorder) {
				// draw horizontal bar
				Path p = new Path();
				p.lineTo(w, h);
				canvas.drawPath(p, mBorderPaint);
			} else if (!mIsAnswerable || !mShowAnswer) {
				//must be a blank square, fill it in entirely
				canvas.drawRGB(255, 255, 255);
				canvas.drawRect(0, 0, w, h, mTrianglePaint);
			}
		}
	}

	/**
	 * Creates a blank Square.
	 * @param context
	 */
	public Square(Context context) {
		super(context);
	}
	
	/**
	 * Creates an answerable Square.
	 * @param context
	 * @param answer the correct answer for the Square.
	 */
	public Square(Context context, int answer, boolean showAnswer) {
		super(context);
		mContext = context;
		mAnswer = answer;
		mShowAnswer = showAnswer;
		mIsAnswerable = !showAnswer;
	}
	
	/**
	 * Creates a 'clue' square.
	 * @param context
	 * @param topClue the clue for the top triangle.
	 * @param bottomClue the clue for the bottom triangle.
	 */
	public Square(Context context, int topClue, int bottomClue) {
		super(context);
		mContext = context;
		mClues[0] = topClue;
		mClues[1] = bottomClue;
	}

}