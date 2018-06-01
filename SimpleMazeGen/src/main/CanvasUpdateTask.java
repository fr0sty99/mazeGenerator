package main;

import java.util.TimerTask;


public class CanvasUpdateTask extends TimerTask {
	MazeGenerator mg;
	
	public CanvasUpdateTask(MazeGenerator mg) {
		this.mg = mg;
	}
	
	@Override
	public void run() {
		mg.draw();
	}

}
