package main;

public class Wall {
	public int x;
	public int y;
	public boolean value;
	public Direction direction;

	public Wall(int x, int y, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.value = true;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
