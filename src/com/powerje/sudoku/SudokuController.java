package com.powerje.sudoku;

import java.util.StringTokenizer;

/**
 * Handles interaction between SudokuModel and a user representation
 * to the playing of the Sudoku.  Creates a SudokuModel and calls it until
 * it gets a good Sudoku board and its solution, which it then makes available
 * via getTable and getAnswers respectively.
 */

public class SudokuController {
	/** Number of squares on one side of Sudoku square. */
	private static final int SIZE = 9;
	/** String value of the current board. */
	private String boardString = "";
	/** String value of the solution to the current board. */
	private String solvedBoardString = "";
	/** 2-D array representation of current board. */
	private int[][] table = new int[SIZE][SIZE];
	/** 2-D array representation of answers to current board. */
	private int[][] answer = new int[SIZE][SIZE];

	/**
	 * Constructor for SudokuController.
	 */
	public SudokuController() {
		SudokuModel board1 = new SudokuModel();
		boardString = board1.generate();
		solvedBoardString = board1.solve();

		while (solvedBoardString == null) {
			System.out.println("Failed, retry...");
			board1 = new SudokuModel();
			boardString = board1.generate();
			solvedBoardString = board1.solve();
		}	
		
		StringTokenizer strtok = new StringTokenizer(boardString, ",");
		int x = 0; 
		int y = 0;
		while (strtok.hasMoreTokens()) {
			table[x][y] = Integer.parseInt(strtok.nextToken());
			if (y == (SIZE - 1)) {
				y = 0;
				++x;
			} else {
				++y;
			}
		}

		strtok = new StringTokenizer(solvedBoardString, ",");
		x = 0; 
		y = 0;
		while (strtok.hasMoreTokens()) {
			answer[x][y] = Integer.parseInt(strtok.nextToken());
			if (y == (SIZE - 1)) {
				y = 0;
				++x;
			} else {
				++y;
			}
		}
	}
	
	/**
	 * Returns table.
	 * @return 2-D int that represents the current Sudoku board.
	 */
	public int[][] getTable() {
		return table;
	}
	
	/**
	 * Returns answers.
	 * @return 2-D int that represents the current Sudoku board answers.
	 */
	public int[][] getAnswer() {
		return answer;
	}
}
