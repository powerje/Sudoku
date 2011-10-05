package com.powerje.sudoku.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.foo.R;
import com.powerje.sudoku.SudokuController;
import com.powerje.sudoku.android.views.Square;

public class SudokuActivity extends Activity {
	private TableLayout mGameBoard;
	private LinearLayout mRoot;
	private List<Square> mSquares;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mGameBoard = (TableLayout) findViewById(R.id.game_board);
		mRoot = (LinearLayout) findViewById(R.id.root_layout);
		mSquares = new ArrayList<Square>();
		createBoard(9);
	}

	private void createBoard(int size) {
		DisplayMetrics metrics;
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int w = metrics.widthPixels - 40;

		TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT);

		int dim = (w / size);
		rowParams.height = dim;
		rowParams.width = dim;

		SudokuController sc = new SudokuController();
		table = sc.getTable();
		answer = sc.getAnswer();

		for (int i = 0; i < size; i++) {
			TableRow row = new TableRow(this);
			for (int j = 0; j < size; j++) {
				Square s = null;
			
				if (table[i][j] == 0) {
					//this particular table item is to be answered by the user
					s = new Square(this, 0, false);
				} else {
					s = new Square(this, answer[i][j], true);
				}
				mSquares.add(s);
				row.addView(s, rowParams);
			}

			mGameBoard.addView(row, new TableLayout.LayoutParams(
					TableLayout.LayoutParams.WRAP_CONTENT,
					TableLayout.LayoutParams.WRAP_CONTENT));
		}
	}

	private boolean solved;
	private int[][] table = new int[9][9];
	private int[][] answer = new int[9][9];

}