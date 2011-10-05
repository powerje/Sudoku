package com.powerje.sudoku;

import java.util.Random;
import java.util.StringTokenizer;

/**
 * Models a Sudoku board.  Generates a board from scratch and
 * solves the board as well.
 *
 */
public class SudokuModel {
	/******** CLASS VARIABLES *********/

	/** Large grid size. */
	private static final int SIZE = 9;

	/** Small grid size. */
	private static final int SMALL_GRID = 3;

	/** Minimum number of squares necessary for unique Sudoku solution. */
	private static final int MIN = 22;

	/**
	 * Variance above MIN to add to the board. Board will have MIN + (0 to BONUS)
	 * squares pre-set. 
	 */
	private static final int BONUS = 20;

	/** Max iterations for DFS to reach before giving up. */
	private static final int MAX_ITERATIONS = 5000;

	/********* INSTANCE VARIABLES *******/
	
	/** Current number of iterations in depth first search,
	 * used to check against MAX_ITERATIONS for timeouts. */
	private int numIterations = 0;

	/** Value of board at pos x,y. */
	private int[][] board;

	/**
	 * Number of symbols left for placement, we start with every symbol
	 * available. Initialized in init().
	 */
	private int[] freesymbols;

	/** [x][y] is true iff that position is fixed. */
	private boolean[][] fixed = new boolean[SIZE][SIZE];

	/**
	 * Constructor for SudokuModel.
	 */
	public SudokuModel() {
		board = new int[SIZE][SIZE];
		freesymbols = new int[SIZE];
	}

	/**
	 * Generate a Sudoku board.
	 * 
	 * @return properly formatted String representing the 
	 * a generated Sudoku board, i.e.: regex(([1-9],)^80,[1-9])
	 */
	public String generate() {

		generatorInitialize();
		generateSudoku();

		return boardToString();

	}

	/**
	 * @requires board has been created.
	 * @return properly formatted String representing the 
	 * current Sudoku board, i.e.: regex(([1-9],)^80,[1-9])
	 */
	private String boardToString() {
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				sBuilder.append(board[i][j]);
				if (j != SIZE - 1) {
					sBuilder.append(',');
				}
			}
			if (i != SIZE - 1) {
				sBuilder.append(',');
			}

		}
		return sBuilder.toString();
	}

	/**
	 * Generate Sudoku board.
	 */
	private void generateSudoku() {
		Random r = new Random();
		int b = r.nextInt(BONUS);
		System.out.println("Generating board with " + (MIN + b)
				+ " pre-set squares.");

		//create MIN + b positions on our board
		for (int k = 0; k < (MIN + b); k++) {
			// pick a random square
			int i = r.nextInt(SIZE);
			int j = r.nextInt(SIZE);
			// ensure we have yet to set this square
			if (board[i][j] != 0) {
				--k;	//don't forget to decrement k!
				continue;
			}
			// pick random symbol
			int symbol = r.nextInt(freesymbols.length);
			if (freesymbols[symbol] > 0) {
				// we have a free symbol, is it legal in this square?
				if (legal(i, j, (symbol + 1))) {
					// remove symbol
					freesymbols[symbol] -= 1;
					// put symbol on board
					board[i][j] = symbol + 1;
				} else {
					--k;
				}
			} else {
				// go back through the loop
				--k;
			}
		}
	}

	/**
	 * Initializes Sudoku generator.
	 */
	private void generatorInitialize() {
		int x = 0, y = 0;

		// init symbols
		for (int i = 0; i < freesymbols.length; i++) {
			freesymbols[i] = SIZE;
		}

		while ((x < SIZE) && (y < SIZE)) {
			board[x][y] = 0;

			if (y == (SIZE - 1)) {
				y = 0;
				++x;
			} else {
				++y;
			}
		}
	}

	/**
	 * Prints Sudoku board in a _pretty_ way.
	 */
	public void printBoard() {

		System.out.println("___________________________");
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				System.out.print("|" + board[x][y] + "|");
			}
			System.out.println();
		}
		System.out.println("___________________________");
	}

	/**
	 * Returns true iff given change to board[x][y] is legal.
	 * 
	 * @param x
	 *            row of the board.
	 * @param y
	 *            column of the board.
	 * @param val
	 *            value to put in board[x][y].
	 * @return true iff given change is legal.
	 */
	private boolean legal(int x, int y, int val) {
		for (int i = 0; i < SIZE; i++) {
			if (x == i) {
				continue;
			} else if ((board[i][y] == val)) {
				return false;
			}
		}

		for (int i = 0; i < SIZE; i++) {
			if (y == i) {
				continue;
			} else if (board[x][i] == val) {
				return false;
			}
		}

		// which grid are we in?
		int gridx = 0;
		int gridy = 0;

		if (x < SMALL_GRID) {
			// if x < SMALL_GRID (3) we are in 1st row of smaller grids
			gridx = 0;
		} else if ((x >= SMALL_GRID) && (x < (SMALL_GRID * 2))) {
			gridx = 1;
		} else if ((x >= (2 * SMALL_GRID)) && (x < (SIZE))) {
			gridx = 2;
		}

		if (y < SMALL_GRID) {
			// if x < SMALL_GRID (3) we are in 1st row of smaller grids
			gridy = 0;
		} else if ((y >= SMALL_GRID) && (y < (SMALL_GRID * 2))) {
			gridy = 1;
		} else if ((y >= (2 * SMALL_GRID)) && (y < (SIZE))) {
			gridy = 2;
		}

		for (int i = gridx * SMALL_GRID; (i < ((gridx * SMALL_GRID) + SMALL_GRID)); i++) {
			// checkstyle gives me issues if i don't make this for loop look
			// stupid
			for (int j = gridy * SMALL_GRID; 
			(j < ((gridy * SMALL_GRID) + SMALL_GRID)); 
			j++) {
				// don't wanna check our grid square
				if (!((i == x) && (j == y))) {
					if (board[i][j] == val) {
						// we've already got this value in our small grid!
						return false;
					}
				}
			}
		}
		// no rules broken!
		return true;
	}

	/**
	 * Create well formatted String representing solved Sudoku board.
	 * 
	 * @requires Sudoku has been generated.
	 * @return If the board is solvable return value is a well formed Sudoku
	 * String.  If not return value is null.
	 */
	public String solve() {

		solverInitialize(boardToString());
		boolean generatedGoodBoard = dfsSolve();
		
		if (!generatedGoodBoard) {
			return null;
		}

		return boardToString();
	}

	/**
	 * Recursive depth first search board solver.
	 * 
	 * @return true iff the board was solved.
	 */
	private boolean dfsSolve() {

		++numIterations;
		
		if (numIterations > MAX_ITERATIONS) {
			return false;
		}

		// find our first empty square
		int i = 0, j = 0;
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				if (board[x][y] == 0) {
					// this is our first empty square
					i = x;
					j = y;
				}
			}
		}

		// loop through symbols until we find one that is available
		for (int k = 0; k < SIZE; k++) {

			if (freesymbols[k] > 0) {
				// we have a free symbol, is it legal in this square?
				if (legal(i, j, (k + 1))) {
					// remove symbol
					freesymbols[k] -= 1;
					// put symbol on board
					board[i][j] = k + 1;
					if (dfsSolve()) {
						return numIterations < MAX_ITERATIONS;
					} else {
						// put symbol back b/c soln sucked
						freesymbols[k] += 1;
					}
				}
			}
		}

		if (solved()) {
			return true;
		}

		// clear the square
		board[i][j] = 0;
		return false;
	}

	/**
	 * Solves a given Sudoku board if possible.
	 * 
	 * @param sBoard - properly formatted String representing Sudoku Board.
	 */
	private void solverInitialize(String sBoard) {

		int x = 0;
		int y = 0;

		// init symbols
		for (int i = 0; i < freesymbols.length; i++) {
			freesymbols[i] = SIZE;
		}

		StringTokenizer strtok = new StringTokenizer(sBoard, ",");
		while (strtok.hasMoreTokens()) {
			board[x][y] = Integer.parseInt(strtok.nextToken());
			if (board[x][y] == 0) {
				// this position was not initial set, so it's not fixed
				fixed[x][y] = false;

			} else {
				fixed[x][y] = true;
				// this symbol is now taken up once
				freesymbols[board[x][y] - 1] -= 1;
			}

			if (y == (SIZE - 1)) {
				y = 0;
				++x;
			} else {
				++y;
			}
		}

	}

	/**
	 * Returns true iff the board is solved. The board is considered "solved"
	 * when all of our freesymbols are used up.
	 * 
	 * @return true iff the board is solved.
	 */
	private boolean solved() {
		for (int i = 0; i < freesymbols.length; i++) {
			if (freesymbols[i] > 0) {
				return false;
			}
		}
		return true;
	}
}
