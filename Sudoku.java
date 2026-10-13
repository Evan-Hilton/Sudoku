package sudoku;

import javax.swing.*;

import PersonalProjects.Game3D.MyKeyListener;
import PersonalProjects.Game3D.MyMouseListener;
import PersonalProjects.Game3D.MyMouseMotionListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.*;
import java.util.ArrayList;

public class Sudoku extends JPanel {
	//basic information about the program
	public static int width = 720;
	public static int height = 720;
	public static int frameRate = 10; // ms per frame
	public static int currentFrame = 0;
	public static Color backgroundColor = Color.WHITE;
	
	//INSTRUCTIONS:
	// To start, click through each ball and set it to what color it should be
	// Once set up, hit 'C' to update the possible colors that each ball could be
	// hit 'SPACE' to search each ball, and if a ball can only be one color, set that ball to that color
	// hit 'R' to reset
	
	//other instance variables
	public static int[][] easyExampleBoard = new int[][] {{0, 7, 0, 8, 2, 9, 3, 0, 0},
											   {3, 0, 0, 0, 0, 0, 1, 0, 0},
											   {6, 5, 2, 3, 0, 0, 4, 0, 0},
											   {9, 2, 3, 7, 0, 0, 8, 4, 0},
											   {0, 0, 0, 0, 0, 0, 0, 0, 0},
											   {0, 1, 8, 0, 0, 6, 9, 7, 3},
											   {0, 0, 5, 0, 0, 4, 2, 8, 9},
											   {0, 0, 7, 0, 0, 0, 0, 0, 1},
											   {0, 0, 6, 1, 3, 8, 0, 5, 0}};
	public static int[][] start = new int[9][9];
	public static SudokuBoard board = new SudokuBoard(easyExampleBoard);
	public static boolean showPossibleColors = true;
	public static boolean go = false;
	public static int speed = 30; // how many frames to skip before updating loop
	public static int boardSize = 500;
	public static int ballRad = 20;
	public static int padX = 100;
	public static int padY = 100;
	public static ArrayList<Color> colors = new ArrayList<Color>();
	
	//the true color possibilities
	public static ArrayList<Integer>[][] possibleColors = (ArrayList<Integer>[][]) new ArrayList[9][9];
	
	//the test color possibilities
	public static ArrayList<Integer>[][] evilColors = (ArrayList<Integer>[][]) new ArrayList[9][9];
	
	
	public static void initialize() {
		colors.add(new Color(230, 230, 230));
		colors.add(new Color(200, 0, 0));
		colors.add(new Color(230, 130, 20));
		colors.add(new Color(230, 200, 0));
		colors.add(new Color(20, 230, 20));
		colors.add(new Color(20, 130, 20));
		colors.add(new Color(20, 230, 230));
		colors.add(new Color(20, 20, 130));
		colors.add(new Color(210, 170, 250));
		colors.add(new Color(130, 20, 230));
		
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				possibleColors[r][c] = new ArrayList<Integer>();
			}
		}
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				evilColors[r][c] = new ArrayList<Integer>();
			}
		}
	}
	
	public static void solveEmptyColors() { // C
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				if(board.get(r, c) == 0) {
					possibleColors[r][c] = new ArrayList<Integer>();
					for(int i = 1; i <= 9; i ++) {
						if(board.testEntry(r, c, i) && !evilColors[r][c].contains(i)) {
							possibleColors[r][c].add(i);
						}
					}
				}
			}
		}
	}
	
	public static int solveEmptyColors(State s) { // C
		int count = 0;
		ArrayList<Integer>[][] oldColors = s.getColors();
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				if(s.get(r, c) == 0) {
					s.updateColors(r, c, new ArrayList<Integer>());
					for(int i = 1; i <= 9; i ++) {
						if(s.getBoard().testEntry(r, c, i) && !evilColors[r][c].contains(i)) {
							s.addColor(r, c, i);
						}
					}
				}
			}
		}
		//compare the old colors to the new colors to see if changes have occurred
		// we really only need count to be zero if no changes happened. if we see a change has happened,
		// then we increment count and break;
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				if(s.get(r, c) == 0 && compareArrayLists(oldColors[r][c], s.getColors()[r][c])) {
					count ++;
					break;
				}
			}
		}
		return count;
	}
	
	//returns true if changes have happened
	//a1 and a2 might not be the same size
	public static boolean compareArrayLists(ArrayList<Integer> a1, ArrayList<Integer> a2) {
		if(a1.size() != a2.size()) {
			return true;
		}
		for(int i = 0; i < a1.size(); i ++) {
			if(a1.get(i) != a2.get(i)) {
				return true;
			}
		}
		return false;
	}
	
	public static void setSingleColorBalls() { // SPACE
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				if(board.get(r, c) == 0 && possibleColors[r][c].size() == 1) {
					board.set(r, c, possibleColors[r][c].get(0));
				}
			}
		}
	}
	
	public static int setSingleColorBalls(State s) { // SPACE
		int count = 0;
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				if(s.get(r, c) == 0 && s.getColors()[r][c].size() == 1) {
					count ++;
					s.updateBoard(r, c, s.getColors()[r][c].get(0));
				}
			}
		}
		return count;
	}
	
	public static void reset() {
		start = new int[9][9];
		possibleColors = (ArrayList<Integer>[][]) new ArrayList[9][9];
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				possibleColors[r][c] = new ArrayList<Integer>();
			}
		}
	}
	
	public static void mainLoop() {
		if(go && currentFrame % speed == 0) {
			solveEmptyColors();
			setSingleColorBalls();
			//impossible means that there are more than one choice per unsolved ball
			if(impossible()) {
				//search for the first next choice and check if it's correct by iterating until complete
				//if the search fails, eliminate the color as a viable option
				//I know this is very badly optimized, I could just find the optimal solution
				//by calling the method and solving the board, but then you couldn't see it happen real
				//time.
				for(int r = 0; r < 9; r ++) {
					for(int c = 0; c < 9; c ++) {
						//if the color is 0, then choose the first color choice from the viable options
						if(board.get(r, c) == 0) {
							if(possibleColors[r][c].size() > 0) {
								//we want to set board[r][c] to possibleColors[r][c].get(0).
								//we need to make a copy of everything PROPERLY and create a new state
								//then pass the state to funkyshit and check the color.
								SudokuBoard boardCopy = board.copy();
								ArrayList<Integer>[][] colorsCopy = copy(possibleColors);
								State startS = new State(boardCopy, colorsCopy);
								//if this random choice of color is correct, set the board's color to this color
								if(doFunkyShit(startS)) {
									board.set(r, c, possibleColors[r][c].get(0));
								}else { // otherwise remove this color from viable options
									evilColors[r][c].add(possibleColors[r][c].get(0));
									possibleColors[r][c].remove(0);
								}
								break;
							}else {
								//divide by zero error means that the current updated board
								//is stupid and bad and is wrong
								int x = 1/0;
							}
						}
					}
				}
				doFunkyShit(new State(board, possibleColors));
			}
		}
	}
	
	public static ArrayList<Integer>[][] copy(ArrayList<Integer>[][] binky){
		ArrayList<Integer>[][] copy = (ArrayList<Integer>[][]) new ArrayList[9][9];
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				copy[r][c] = new ArrayList<Integer>();
				for(int i = 0; i < binky[r][c].size(); i ++) {
					copy[r][c].add(binky[r][c].get(i));
				}
			}
		}
		return copy;
	}
	
	//s gets automatically updated because it is a reference to the state
	public static void iterateUntilComplete(State s) {
		int numChanges = 1;
		while(numChanges > 0) {
			numChanges = 0;
			numChanges += solveEmptyColors(s);
			numChanges += setSingleColorBalls(s);
		}
	}
	
	//returns true if the state leads to a success, false otherwise
	//iterative tree search to determine if one random choice of color is correct
	public static boolean doFunkyShit(State s) {
		//first we need to iterate s until it's impossible to iterate further by standard means
		//this should actually change s because we pass a reference to the object and s is updated in
		//the method
		iterateUntilComplete(s);
		
		//base case. if the state is completely full (no more guesses to make), check success:
		//return true if this subtree leads to success
		//return false if this subtree leads to failure
		if(s.full()) {
			if(s.solved()) {
				return true;
			}else {
				return false;
			}
		}
		
		//recursive step: try each subtree to check success. If one leads to success, then return true
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				if(s.get(r, c) == 0) {
					ArrayList<Integer> colors = s.getBoardColors(r, c);
					for(int i = 0; i < colors.size(); i ++) {
						//for each choice of color, determine if the color leads to success
						s.updateBoard(r, c, colors.get(i));
						if(doFunkyShit(s)) {
							return true;
						}
					}
				}
			}
		}
		//if all choices of potential colors lead to failure, return false
		return false;
	}
	
	public static boolean impossible() {
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				if(board.get(r, c) == 0 && possibleColors[r][c].size() == 1) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static void paintLoop(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawRect(padX, padY, boardSize, boardSize);
	    float[] dashPattern = {10f, 5f}; // 10 units opaque, 5 units transparent
	    BasicStroke dashedStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f);
	    g.setStroke(dashedStroke);
	    int miniBoard = boardSize / 3;
	    g.drawLine(padX, padY + miniBoard, padX + boardSize, padY + miniBoard);
	    g.drawLine(padX, padY + 2 * miniBoard, padX + boardSize, padY + 2 * miniBoard);
	    g.drawLine(padX + miniBoard, padY, padX + miniBoard, padY + boardSize);
	    g.drawLine(padX + 2 * miniBoard, padY, padX + 2 * miniBoard, padY + boardSize);
		drawBoard(g, board);
	}
	
	public static void drawBoard(Graphics2D g, SudokuBoard b) {
		int w = boardSize / 9;
		for(int r = 0; r < 9; r ++) {
			for(int c = 0; c < 9; c ++) {
				if(board.get(r, c) == 0) {
					if(showPossibleColors) {
						ArrayList<Integer> cols = possibleColors[r][c];
						for(int i = 0; i < cols.size(); i ++) {
							g.setColor(colors.get(cols.get(i)));
							int x_ = padX + w / 2 + c * w - ballRad;
							int y_ = padY + w / 2 + r * w - ballRad;
							int colorIndex = cols.get(i); // a number 1-9 inclusive
							colorIndex -= 1; // a number 0-8 inclusive
							int wid = ballRad * 2 / 9;
							g.fillRect(x_ + wid * colorIndex, y_, wid, wid);
						}
					}else {
						g.setColor(colors.get(b.get(r, c)));
						g.fillOval(padX + w / 2 + c * w - ballRad, padY + w / 2 + r * w - ballRad, ballRad * 2, ballRad * 2);
					}
				}else {
					g.setColor(colors.get(b.get(r, c)));
					g.fillOval(padX + w / 2 + c * w - ballRad, padY + w / 2 + r * w - ballRad, ballRad * 2, ballRad * 2);
				}
			}
		}
	}

	//------------------------------------------- o -------------------------------------------------
	
	/**
	 * These below 2 methods are setup and do not need to be changed;
	 * mainLoop() and paintLoop() will be called every frame. Frame rate is determined above in the 
	 * static class variables. However, the frame rate corresponds to something different than
	 * normal in this program: frameRate describes how many milliseconds the system waits 
	 * before each call of mainLoop() and paintLoop(), so if either of these methods takes 
	 * a non-insignificant time to run, the frame rate of the program will not correspond 
	 * to the frameRate variable
	 */
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        //antialiasing off means hard edges, antialiasing on means soft edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        
        //background
        g.setColor(backgroundColor);
        g.fillRect(0,0,2000,2000);
        
        //calling paintLoop()
        paintLoop(g2d);
    }
    
    public Sudoku() {
		KeyListener d = new MyKeyListener();
		addKeyListener(d);
		MouseListener mML = new MyMouseListener();
		addMouseListener(mML);
		MouseMotionListener mMML = new MyMouseMotionListener();
		addMouseMotionListener(mMML);
		setFocusable(true);
	}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Java 2D Graphics Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Sudoku());
        frame.setSize(width, height);
        frame.setVisible(true);
        initialize();
        while(true){
        	// currentFrame describes how many frames have occurred throughout the duration of run time
            currentFrame ++;
            frame.repaint();
            mainLoop();
            try{
                Thread.sleep(frameRate);
            }
            catch(Exception e){
                System.out.println("Error!");
            }
        }
    }
    
    public class MyKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			// this is a dumb method, use 'keyPressed(KeyEvent e)' unless you know what
			// you're doing
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				for(int r = 0; r < 9; r ++) {
					for(int c = 0; c < 9; c ++) {
						if(board.get(r, c) == 0 && possibleColors[r][c].size() == 1) {
							board.set(r, c, possibleColors[r][c].get(0));
						}
					}
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_C) {
				for(int r = 0; r < 9; r ++) {
					for(int c = 0; c < 9; c ++) {
						if(board.get(r, c) == 0) {
							possibleColors[r][c] = new ArrayList<Integer>();
							for(int i = 1; i <= 9; i ++) {
								if(board.testEntry(r, c, i)) {
									possibleColors[r][c].add(i);
								}
							}
						}
					}
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_R) {
				start = new int[9][9];
				possibleColors = (ArrayList<Integer>[][]) new ArrayList[9][9];
				for(int r = 0; r < 9; r ++) {
					for(int c = 0; c < 9; c ++) {
						possibleColors[r][c] = new ArrayList<Integer>();
					}
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_G) {
				go = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_S) {
				go = false;
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}
	}

	public class MyMouseListener implements MouseListener {
		@Override
		public void mouseExited(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			int w = boardSize / 9;
			int x = e.getX();
			int y = e.getY();
			x -= padX;
			y -= padY;
			x /= w;
			y /= w;
			if(x >= 0 && x < 9 && y >= 0 && y < 9) {
				//we have r = y and c = x. these are the indexes for the board. iterate through the board colors
				board.set(y, x, board.get(y, x) + 1);
				if(board.get(y, x) > 9) {
					board.set(y, x, 0);
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}
	}

	public class MyMouseMotionListener implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {

		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}
	}
}