package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Cell {
	public int x;
	public int y;
	public boolean visited = false;
	public boolean isCurrent = false;
	public Wall[] walls = new Wall[4]; // top, right, bottom, left

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;

		walls[0] = new Wall(x, y, Direction.TOP);
		walls[1] = new Wall(x, y, Direction.RIGHT);
		walls[2] = new Wall(x, y, Direction.BOTTOM);
		walls[3] = new Wall(x, y, Direction.LEFT);
	}
	
	public boolean hasNonVisitedNeighbors(Cell[][] maze) {
		if (getNonVisitedNeighbors(maze).size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public ArrayList<Cell> getNonVisitedNeighbors(Cell[][] maze) {
		ArrayList<Cell> neighborList = new ArrayList<>();

		// top neighbor
		if (y > 0 && !maze[getX()][getY() - 1].isVisited()) {
			neighborList.add(maze[getX()][getY() - 1]);
		}

		// right
		if (x < maze.length - 1 && !maze[getX() + 1][getY()].isVisited()) {
			neighborList.add(maze[getX() + 1][getY()]);
		}

		// bottom
		if (y < maze.length - 1 && !maze[getX()][getY() + 1].isVisited()) {
			neighborList.add(maze[getX()][getY() + 1]);
		}

		// left
		if (x > 0 && !maze[getX() - 1][getY()].isVisited()) {
			neighborList.add(maze[getX() - 1][getY()]);
		}

		return neighborList;
	}

	public void draw(Graphics g, int cellSize) {
		// default color
		g.setColor(Color.YELLOW);

		// choose special color if needed
		if (isVisited()) {
			g.setColor(Color.orange);
		}
		if (isCurrent()) {
			g.setColor(Color.CYAN);
		}

		// draw cell
		g.fillRect(x * cellSize, y * cellSize , cellSize, cellSize);

		// draw walls above cell
		g.setColor(Color.BLACK);
		// top
		if (getTopWall()) {
			g.drawLine(x * cellSize, y * cellSize,
					x * cellSize + cellSize, y * cellSize );
		}
		// right
		if (getRightwall()) {
			g.drawLine(x * cellSize + cellSize , y * cellSize ,
					x * cellSize + cellSize , y * cellSize + cellSize );
		}
		// bottom
		if (getBottomwall()) {
			g.drawLine(x * cellSize + cellSize , y * cellSize + cellSize ,
					x * cellSize , y * cellSize + cellSize );
		}
		// left
		if (getLeftwall()) {
			g.drawLine(x * cellSize , y * cellSize + cellSize ,
					x * cellSize , y * cellSize );
		}
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public void setTopwall(boolean wall) {
		walls[0].setValue(wall);
	}

	public void setRightwall(boolean wall) {
		walls[1].setValue(wall);
	}

	public void setBottomwall(boolean wall) {
		walls[2].setValue(wall);
	}

	public void setLeftwall(boolean wall) {
		walls[3].setValue(wall);
	}

	public boolean getTopWall() {
		return walls[0].getValue();
	}

	public boolean getRightwall() {
		return walls[1].getValue();
	}

	public boolean getBottomwall() {
		return walls[2].getValue();
	}

	public boolean getLeftwall() {
		return walls[3].getValue();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isVisited() {
		return visited;
	}

}
