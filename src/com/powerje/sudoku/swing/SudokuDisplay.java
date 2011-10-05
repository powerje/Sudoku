package com.powerje.sudoku.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Display the sudoku game.
 */
public final class SudokuDisplay {
	/**
	 * Width of the window.
	 */
	private static final int WINDOW_WIDTH = 600;
	/**
	 * Length of the window.
	 */
	private static final int WINDOW_LENGTH = 620;

	/**
	 * Default constructor.
	 */
	private SudokuDisplay() {
	}

	/**
	 * Main function.
	 * 
	 * @param args
	 *            is the arguments passed into main.
	 */
	public static void main(final String[] args) {
		JFrame window = new SudokuGUI("Sudoku Game");
		window.setSize(WINDOW_WIDTH, WINDOW_LENGTH);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
	}
}

/**
 * GUI of Sudoku.
 */
class SudokuGUI extends JFrame {
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * SIZE of the Sudoku board.
	 */
	private static final int SIZE = 9;
	/**
	 * Position to put the first separator.
	 */
	private static final int POS_SEP_1 = 2;
	/**
	 * Position to put the second separator.
	 */
	private static final int POS_SEP_2 = 5;
	/**
	 * Length of the grid panel.
	 */
	private static final int GRID_LEN = 600;
	/**
	 * Width of the separator.
	 */
	private static final int SEP_WIDTH = 6;
	/**
	 * Length of the separator.
	 */
	private static final int SEP_LENGTH = 30;
	/**
	 * Number of the boarder.
	 */
	private static final int BOARDER_NUM = 5;
	/**
	 * Width of the window.
	 */
	private static final int WINDOW_WIDTH = 600;
	/**
	 * Length of the window.
	 */
	private static final int WINDOW_LENGTH = 620;
	/**
	 * The table to display.
	 */
	private int[][] table = new int[SIZE][SIZE];
	/**
	 * The solution of soduku.
	 */
	private int[][] answer = new int[SIZE][SIZE];
	/**
	 * 9 * 9 numbers text fields.
	 */
	private JTextField[][] numbersF = new JTextField[SIZE][SIZE];
	/**
	 * new game button.
	 */
	private JButton newGameB = new JButton("New Game");
	/**
	 * hint button.
	 */
	private JButton hintB = new JButton("Hint");
	/**
	 * solution button.
	 */
	private JButton solvB = new JButton("Solve");

	/**
	 * Is the current board solved?
	 */
	private boolean solved = false;

	/**
	 * Constructor of SudokuGUI.
	 * 
	 * @param name
	 *            title
	 */
	SudokuGUI(String name) {
		super(name);
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				numbersF[i][j] = new JTextField(1);
				numbersF[i][j].setFont(new Font(numbersF[i][j].getFont()
						.getName(), Font.BOLD, numbersF[i][j].getFont()
						.getSize()));
			}
		}

		// build a new game
		newGame();

		// grid of 9 rows
		JPanel grid = new JPanel();
		grid.setLayout(new BoxLayout(grid, BoxLayout.Y_AXIS));
		grid.setSize(GRID_LEN, GRID_LEN);

		JPanel[] rows = new JPanel[SIZE];
		for (int i = 0; i < SIZE; i++) {
			rows[i] = new JPanel();
			rows[i].setLayout(new BoxLayout(rows[i], BoxLayout.X_AXIS));
			for (int j = 0; j < SIZE; j++) {
				rows[i].add(numbersF[i][j]);
				// add the vertical separate line
				if (j == POS_SEP_1 || j == POS_SEP_2) {
					JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
					sep.setPreferredSize(new Dimension(SEP_WIDTH, SEP_LENGTH));
					sep.setBorder(BorderFactory.createLineBorder(Color.BLACK,
							BOARDER_NUM));
					rows[i].add(sep);
				}
			}

			grid.add(rows[i]);

			if (i == POS_SEP_1 || i == POS_SEP_2) {
				// add the horizontal separate line
				JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
				sep.setPreferredSize(new Dimension(SEP_LENGTH, SEP_WIDTH));
				sep.setBorder(BorderFactory.createLineBorder(Color.BLACK,
						BOARDER_NUM));
				grid.add(sep);
			}
		}
		// buttons panel
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

		buttons.add(newGameB);
		buttons.add(hintB);
		buttons.add(solvB);

		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(buttons);
		content.add(grid);
		// content.setBackground(Color.blue);

		// ... finalize layout
		this.setContentPane(content);
		this.pack();
		this.setSize(WINDOW_WIDTH, WINDOW_LENGTH);
		this.setResizable(false);
		// ... Listener to do newGame
		newGameB.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				for (int i = 0; i < SIZE; i++) {
					for (int j = 0; j < SIZE; j++) {
						numbersF[i][j].setText("");
						numbersF[i][j].setBackground(Color.WHITE);
						numbersF[i][j].setEditable(true);
						numbersF[i][j].setFocusable(true);
					}
				}
				newGame();
			}
		});
		// ... Listener to do hint
		hintB.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				popNum();
			}
		});
		// ... Listener to do solve
		solvB.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				solve();
			}
		});
	}

	/**
	 * generate a new table.
	 */
	private void newGame() {
		SudokuController s = new SudokuController();
		solved = false;
		table = s.getTable();
		answer = s.getAnswer();

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				numbersF[i][j].setColumns(1);
				numbersF[i][j].setHorizontalAlignment(JTextField.CENTER);
				numbersF[i][j].setBorder(BorderFactory
						.createLineBorder(Color.BLACK));
				// different the nonzero numbers
				if (table[i][j] > 0) {
					numbersF[i][j].setText(String.valueOf(table[i][j]));
					numbersF[i][j].setBackground(Color.ORANGE);
					numbersF[i][j].setEditable(false);
					numbersF[i][j].setFocusable(false);
				}
			}
		}

		hintB.setFocusable(true);
		solvB.setFocusable(true);
	}

	/**
	 * Display the answer.
	 */
	private void solve() {
		solved = true;
		hintB.setFocusable(false);
		solvB.setFocusable(false);
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if ((numbersF[i][j].isEditable())
						&& (numbersF[i][j].getBackground() != Color.BLUE)) {
					if (String.valueOf(answer[i][j]).equals(
							numbersF[i][j].getText())) {
						numbersF[i][j].setBackground(Color.GREEN);
					} else {
						numbersF[i][j].setText(String.valueOf(answer[i][j]));
						numbersF[i][j].setBackground((Color.RED));
					}
				}
				numbersF[i][j].setEditable(false);
				numbersF[i][j].setFocusable(false);
			}
		}
	}

	/**
	 * Pop a number in answer by randomly.
	 */
	private void popNum() {
		if (solved) {
			return;
		}
		try {
			Random randomGenerator = new Random();
			// generate a random x
			int i = randomGenerator.nextInt(SIZE);
			// generate a random y
			int j = randomGenerator.nextInt(SIZE);

			if ((numbersF[i][j].isEditable())
					&& (numbersF[i][j].getBackground() != Color.BLUE)) {
				numbersF[i][j].setText(String.valueOf(answer[i][j]));
				numbersF[i][j].setBackground((Color.BLUE));
				numbersF[i][j].setEditable(false);
				numbersF[i][j].setFocusable(false);
			} else {
				popNum();
			}
		} catch (java.lang.StackOverflowError e) {
			return;
		}
	}
}
