package main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MazeGenerator implements ActionListener, ChangeListener {
	// View
	private JFrame frame;
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics g;
	private Timer timer;
	private CanvasUpdateTask canvasUpdateTask;

	// GuiPanel
	public int guiWidth = 300;
	public JButton button2;
	public JSlider slider;

	// Mazes
	public final static int cellSize = 30;
	private int mazeSize = 20;
	private Cell[][] maze;
	private PrimitiveCell[][] pMaze;

	// DFS variables
	private boolean useIterationTime = false;
	private int iterationTime = 50; // time per iteration
	private final int canvasWidth = mazeSize * cellSize;
	private final int canvasHeight = mazeSize * cellSize;
	private final String frameTitle = "SimpleMazeGen V0.1";
	private boolean dfsRunning = false;
	private boolean primitiveMode = false; // cell is a wall (or not)

	// drawing
	private final int delay = 5; // should be smaller than iterationTime
	private boolean drawingLoopRunning = false;

	// TODO: -Refactor
	// -make a boolean which changes the mazeGen to a Version with no walls -> a
	// cell can be a wall
	// -implement consistent drawing Loop
	// -start coding the solver

	// IMPLEMENT MULTITHREADING TO DIVIDE GENERATOR AND DRAWING AND THEN DO
	// THIS:
	// -menu with user Input -> initial params(maze size, cellSize,
	// buttons:
	// a) to start dfs
	// b) to pause dfs
	// c) to end dfs
	// -implement slider for timestep

	MazeGenerator() {
		// TODO: beautifying - somehow doesn't work!?
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		initFrame();
	}

	/*
	 * DFS maze genalgorithm 1 Randomly select a node (or cell) N. 2 Push the
	 * node N onto a queue Q. 3 Mark the cell N as visited. 4 Randomly select an
	 * adjacent cell A of node N that has not been visited. If all the neighbors
	 * of N have been visited: a) Continue to pop items off the queue Q until a
	 * node is encountered with at least one non-visited neighbor - assign this
	 * node to N and go to step 4. b) If no nodes exist: stop. 5 Break the wall
	 * between N and A. 6 Assign the value A to N. 7 Go to step 2.
	 * 
	 */

	public void startPrimitiveDFS() {
		// TODO: debug and find out changes for proper functionality
		// int x, y = 0;
		Random random = new Random();
		LinkedList<PrimitiveCell> queue = new LinkedList<>();
		boolean mazeGenRunning = true;

		int randomNumX = 0 + random.nextInt((pMaze.length - 0) / 2) * 2;
		int randomNumY = 0 + random.nextInt((pMaze.length - 0) / 2) * 2;

		PrimitiveCell choosenNeighborCell = null;
		PrimitiveCell currentCell = pMaze[randomNumX][randomNumY];
		currentCell.setWall(false);
		currentCell.setVisited(true);
		currentCell.setCurrent(true);

		while (mazeGenRunning) {
			if (currentCell.hasNonVisitedNeighbors(pMaze)) {
				ArrayList<PrimitiveCell> neighborList = currentCell.getNonVisitedNeighbors(pMaze);
				choosenNeighborCell = neighborList.get(random.nextInt(neighborList.size()));
			} else {
				while (choosenNeighborCell == null || !choosenNeighborCell.hasNonVisitedNeighbors(pMaze)) {
					if (queue.size() > 0) {
						choosenNeighborCell = queue.pop();
					} else {
						mazeGenRunning = false;
						break;
					}
				}
			}

			currentCell.setCurrent(false);
			choosenNeighborCell.setVisited(true);
			// cell is a wall in primitive mode
			// removeWallsBetweenCells(currentCell, choosenNeighborCell);
			currentCell = choosenNeighborCell;
			currentCell.setCurrent(true);
			currentCell.setWall(false);
			queue.add(currentCell);

			// TODO: write better timeStep-logic for using it as slider-input
			// value in the gui
			if (useIterationTime) {
				try {
					Thread.sleep(iterationTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		currentCell.setCurrent(false);
		System.out.print("Maze generation finished");
		dfsRunning = false;
	}

	public void startDFS() {
		int x, y = 0;
		Random random = new Random();
		LinkedList<Cell> queue = new LinkedList<>();
		boolean mazeGenRunning = true;

		x = random.nextInt(maze.length);
		y = random.nextInt(maze.length);

		Cell choosenNeighborCell = null;
		Cell currentCell = maze[x][y];
		currentCell.setVisited(true);
		currentCell.setCurrent(true);

		while (mazeGenRunning) {
			if (currentCell.hasNonVisitedNeighbors(maze)) {
				ArrayList<Cell> neighborList = currentCell.getNonVisitedNeighbors(maze);
				choosenNeighborCell = neighborList.get(random.nextInt(neighborList.size()));
			} else {
				while (choosenNeighborCell == null || !choosenNeighborCell.hasNonVisitedNeighbors(maze)) {
					if (queue.size() > 0) {
						choosenNeighborCell = queue.pop();
					} else {
						mazeGenRunning = false;
						break;
					}
				}
			}

			currentCell.setCurrent(false);
			choosenNeighborCell.setVisited(true);
			removeWallsBetweenCells(currentCell, choosenNeighborCell);
			currentCell = choosenNeighborCell;
			currentCell.setCurrent(true);
			queue.add(currentCell);

			// TODO: write better timeStep-logic for using it as slider-input
			// value in the gui
			if (useIterationTime) {
				try {
					Thread.sleep(iterationTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		currentCell.setCurrent(false);
		System.out.println("Maze generation finished");
		dfsRunning = false;
	}

	public boolean unvisitedCellExists() {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze.length; j++) {
				if (!maze[i][j].isVisited()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean primitiveMazeExists() {
		if (pMaze[0][0] == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean mazeExists() {
		if (maze[0][0] == null) {
			return false;
		} else {
			return true;
		}
	}

	public void initprimitiveMazeWithPrimitiveWalls() {
		// init every Cell, including the 4 walls (top, right, bottom, left)
		pMaze = new PrimitiveCell[mazeSize][mazeSize];
		for (int x = 0; x < mazeSize; x++) {
			for (int y = 0; y < mazeSize; y++) {
				pMaze[x][y] = new PrimitiveCell(x, y);
			}
		}
	}

	public void initMazeWithAdvancedWalls() {
		// init every Cell, including the 4 walls (top, right, bottom, left)
		maze = new Cell[mazeSize][mazeSize];
		for (int x = 0; x < mazeSize; x++) {
			for (int y = 0; y < mazeSize; y++) {
				maze[x][y] = new Cell(x, y);
			}
		}
	}

	public void initFrame() {
		// setup Canvas
		Dimension s = new Dimension(canvasWidth, canvasHeight);
		canvas = new Canvas();
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setMinimumSize(s);

		// setup JFrame
		frame = new JFrame(frameTitle);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		frame.getContentPane().add(canvas, c);

		// GUI
		JPanel guiPanel = new JPanel();
		guiPanel.setLayout(new GridBagLayout());

		Dimension guiPanelDimen = new Dimension(guiWidth, mazeSize * cellSize);
		guiPanel.setMinimumSize(guiPanelDimen);
		guiPanel.setPreferredSize(guiPanelDimen);
		guiPanel.setMaximumSize(guiPanelDimen);

		// add create Button
		button2 = new JButton("create new maze");
		button2.setSize(new Dimension(100, 20));
		button2.addActionListener(this);
		c.gridx = 0;
		c.gridy = 0;
		guiPanel.add(button2, c);

		// add Slider for speed
		slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		slider.addChangeListener(this);
		c.gridx = 0;
		c.gridy = 2;
		// not working by now, multithreading needed --> guiPanel.add(slider,
		// c);

		c.gridx = 1;
		c.gridy = 0;

		frame.getContentPane().add(guiPanel, c);
		frame.pack();

		// optional
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		// make frame visible
		frame.setVisible(true);

		// create bufferStrategy and Graphics
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		g = bs.getDrawGraphics();

		// !!! after draw, always call bs.show(); !!!

		// before the drawing loop we need to initiate the maze itself
		if (primitiveMode) {
			initprimitiveMazeWithPrimitiveWalls();
		} else {
			initMazeWithAdvancedWalls();
		}

		// start drawing loop
		startDrawingLoop();
	}

	public void startDrawingLoop() {
		canvasUpdateTask = new CanvasUpdateTask(this);
		timer = new java.util.Timer();
		timer.schedule(canvasUpdateTask, 0, delay);
	}

	public void endDrawingLoop() {
		timer.cancel();
		timer = null;
		drawingLoopRunning = false;
	}

	public void draw() {
		// draw every Cell
		if (primitiveMode) {
			if(primitiveMazeExists()) {
				for (int x = 0; x < pMaze.length; x++) {
					for (int y = 0; y < pMaze.length; y++) {
						pMaze[x][y].draw(g, cellSize);
					}
				}
			}
		} else {
			if(mazeExists()) {
				for (int x = 0; x < maze.length; x++) {
					for (int y = 0; y < maze.length; y++) {
						maze[x][y].draw(g, cellSize);
					}
				}
			}	
		}

		// show bufferStrategy
		bs.show();
	}

	public void removeWallsBetweenCells(Cell a, Cell b) {
		// b on top of a
		if (b.y + 1 == a.y && a.x == b.x) {
			a.setTopwall(false);
			b.setBottomwall(false);
		}

		// b on the right of a
		if (b.x - 1 == a.x && a.y == b.y) {
			a.setRightwall(false);
			b.setLeftwall(false);
		}

		// b is on the bottom of a
		if (b.y - 1 == a.y && a.x == b.x) {
			a.setBottomwall(false);
			b.setTopwall(false);
		}

		// b is on the left of a
		if (b.x + 1 == a.x && a.y == b.y) {
			a.setLeftwall(false);
			b.setRightwall(false);
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		MazeGenerator mg = new MazeGenerator();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == button2.getActionCommand()) {
			if (!dfsRunning) {
				dfsRunning = true;
				System.out.println("DFS maze generator Started!");
				if (primitiveMode) {
					initprimitiveMazeWithPrimitiveWalls();
					startPrimitiveDFS();
				} else {
					// multithreading here Thread thread = new Thread()
					initMazeWithAdvancedWalls();
					startDFS();
				}
				dfsRunning = false;
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		iterationTime = ((JSlider) e.getSource()).getValue();
	}
}
