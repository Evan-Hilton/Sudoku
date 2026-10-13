package sudoku;

public class SudokuBoard {
	//SudokuBoard is a 9x9 board of integers 1-9
	//A "null" value (an entry not yet submitted) is set to 0
	private static int[][] board;
	
	/*
	 * The rules of Sudoku are as follows:
	 * -each row and column of the board must contain all 9 digits
	 * -each 3x3 board within the larger board must contain all 9 digits
	 */
	public SudokuBoard(int[][] b){
		this.board = b;
	}
	
	public boolean testEntry(int row, int col, int digit) {
		//try every column in the given row
		for(int c = 0; c < 9; c ++) {
			if(board[row][c] == digit) {
				return false;
			}
		}
		
		//try every row in the given column
		for(int r = 0; r < 9; r ++) {
			if(board[r][col] == digit) {
				return false;
			}
		}
		
		//try 3x3 surroundings
		for(int r = (row / 3) * 3; r < ((row / 3) * 3) + 3; r ++) {
			for(int c = (col / 3) * 3; c < ((col / 3) * 3) + 3; c ++) {
				if(board[r][c] == digit) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public int get(int r, int c) {
		return this.board[r][c];
	}
	
	public void set(int r, int c, int color) {
		this.board[r][c] = color;
	}
	
	public SudokuBoard copy() {
		int[][] newBoard = new int[9][9];
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				newBoard[r][c] = this.board[r][c];
			}
		}
		return new SudokuBoard(newBoard);
	}
}
