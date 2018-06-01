package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class PrimitiveCell{
	public int x;
	public int y;
	public boolean visited = false;
	public boolean isCurrent = false;
	public boolean isWall = true;

	public PrimitiveCell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/*
	 * 1.) there are blocks at predefined positions in your maze

    you run the algorithm on a 2*k+1 grid
    assume the numbering of your cells starts top left with (0,0). mark all cells with 2 odd coordinates ( (2*p+1, 2*q+1); p,q < k ) as blocks.
    you run the modified algorithm from your source on the remaining cells ('even cells'). the modifications are:
        start with an even cell picked at random
        a 'neighbour cell' is the second but next cell in any grid direction; i.e. you 'jump' over a brick.
        instead of knocking down the wall between cells, you turn the block into an accesible cell. however, this cell will not be considered by the selection and backtracking

2.) instead of walls separating cells you want blocks.

before starting the algorithm mark any number of cells as blocks. proceed as outlined in your source but never consider any of the block cells. you will have to take special precautions if you want want to guarantee complete accessibility in your maze. a simple scheme would be to never mark a cell as a block that has more than 1 blocks as neighbours.

hope these ideas suit your needs,
	 */

	public boolean hasNonVisitedNeighbors(PrimitiveCell[][] maze) {
		if (getNonVisitedNeighbors(maze).size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public ArrayList<PrimitiveCell> getNonVisitedNeighbors(PrimitiveCell[][] maze) {
		ArrayList<PrimitiveCell> neighborList = new ArrayList<>();

		// top neighbor
		if (y > 1 && !maze[x][y - -2].isVisited()) {
			neighborList.add(maze[x][y - 2]);
		}

		// right
		if (x < maze.length -2 && !maze[x + 2][y].isVisited()) {
			neighborList.add(maze[x + 2][y]);
		}

		// bottom
		if (y < maze.length - 2 && !maze[x][y + 2].isVisited()) {
			neighborList.add(maze[x][y + 2]);
		}

		// left
		if (x > 1 && !maze[x - 2][y].isVisited()) {
			neighborList.add(maze[x - 2][y]);
		}

		return neighborList;
	}

	public void draw(Graphics g, int cellSize ) {
		if(isWall) {
			g.setColor(Color.BLACK);
		} else {
			// default color
			g.setColor(Color.YELLOW);
			
			// choose special color if needed
			if (isVisited()) {
				g.setColor(Color.orange);
			}
			if (isCurrent()) {
				g.setColor(Color.CYAN);
			}
		}
		
		// draw the rectangle 
		g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
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
	
	public boolean isWall() {
		return isWall;
	}
	
	public void setWall(boolean isWall) {
		this.isWall = isWall;
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
