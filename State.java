package sudoku;

import java.util.ArrayList;

public class State {
	private SudokuBoard board;
	private ArrayList<Integer>[][] possibleColors = (ArrayList<Integer>[][]) new ArrayList[9][9];
	
	//we are assuming a 9x9 board
	public State(SudokuBoard b, ArrayList<Integer>[][] pc) {
		this.board = b;
		this.possibleColors = pc;
	}
	
	public ArrayList<Integer> getBoardColors(int r, int c){
		return this.possibleColors[r][c];
	}
	
	public SudokuBoard getBoard() {
		return this.board;
	}
	
	public void updateBoard(SudokuBoard b) {
		this.board = b;
	}
	
	public void updateBoard(int r, int c, int col) {
		this.board.set(r, c, col);
	}
	
	public int get(int r, int c) {
		return this.board.get(r, c);
	}
	
	public ArrayList<Integer>[][] getColors(){
		return this.possibleColors;
	}
	
	public void updateColors(ArrayList<Integer>[][] pc) {
		this.possibleColors = pc;
	}
	
	public void updateColors(int r, int c, ArrayList<Integer> pc) {
		this.possibleColors[r][c] = pc;
	}
	
	public void addColor(int r, int c, int col) {
		this.possibleColors[r][c].add(col);
	}
	
	public boolean full() {
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				//the state is not full if a choice can still be made
				if(board.get(r, c) == 0 && possibleColors[r][c].size() > 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean solved() {
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				//the state is not full if a choice can still be made
				if(board.get(r, c) == 0) {
					return false;
				}
			}
		}
		return true;
	}
}
