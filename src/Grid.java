import java.util.ArrayList;
import java.util.List;

public class Grid {
	private int width;
	private int height;
	private int offsetX;
	private int offsetY;
	
	private Coordinate currentPos;
	private Cell[][] cells;
	private List<Assignment> assignments;
	
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		this.offsetX = 0;
		this.offsetY = 0;
		
		this.currentPos = Coordinate.INITIAL_POS;
		this.cells = new Cell[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j] = new Cell();
			}
		}
		this.assignments = new ArrayList<>();
	}
	
	public Grid(Grid other) {
		this.width = other.width;
		this.height = other.height;
		this.offsetX = other.offsetX;
		this.offsetY = other.offsetY;
		
		this.currentPos = other.currentPos;
		this.cells = new Cell[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j] = new Cell(other.cells[i][j]);
			}
		}
		this.assignments = new ArrayList<>(other.assignments);
	}
	
	public List<Assignment> getAssignments() {
		return assignments;
	}
	
	public int computeScore() {
		int score = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (cells[i][j].isEmpty()) {
					continue;
				}
				if (!cells[(i + 1) % width][j].isEmpty()) {
					score += cells[i][j].charge * cells[(i + 1) % width][j].charge;
				}
				if (!cells[i][(j + 1) % height].isEmpty()) {
					score += cells[i][j].charge * cells[i][(j + 1) % height].charge;
				}
			}
		}
		return score;
	}
	
	public boolean isDeadEnd() {
		if (currentPos.equals(Coordinate.INITIAL_POS)) {
			return false;
		} 
		
		Coordinate up = getUpCoordinate();
		Coordinate down = getDownCoordinate();
		Coordinate left = getLeftCoordinate();
		Coordinate right = getRightCoordinate();
		if (cells[up.y][up.x].isEmpty() 
				|| cells[down.y][down.x].isEmpty()
				|| cells[left.y][left.x].isEmpty()
				|| cells[right.y][right.x].isEmpty()) {
			return false;
		}
		
		return true;
	}
	
	public List<Direction> getLegalMoves() {
		List<Direction> dirs = new ArrayList<>();
		if (currentPos.equals(Coordinate.INITIAL_POS) || currentPos.equals(Coordinate.ORIGIN)) {
			dirs.add(Direction.RIGHT);
			return dirs;
		}
		
		Coordinate up = getUpCoordinate();
		Coordinate down = getDownCoordinate();
		Coordinate left = getLeftCoordinate();
		Coordinate right = getRightCoordinate();
		
		;
		if (cells[up.y][up.x].isEmpty()) {
			dirs.add(Direction.UP);
		}
		if (cells[down.y][down.x].isEmpty()) {
			dirs.add(Direction.DOWN);
		}
		if (cells[left.y][left.x].isEmpty()) {
			dirs.add(Direction.LEFT);
		}
		if (cells[right.y][right.x].isEmpty()) {
			dirs.add(Direction.RIGHT);
		}
		
		return dirs;
	}
		
	public boolean assign(Direction dir, int charge) {
		switch (dir) {
		case UP:
			return assignUp(charge);
		case DOWN:
			return assignDown(charge);
		case LEFT:
			return assignLeft(charge);
		case RIGHT:
			return assignRight(charge);
			default:
				return false;
		}
	}
	
	private boolean assignUp(int charge) {
		Coordinate nextPos = currentPos.equals(Coordinate.INITIAL_POS) ?
				Coordinate.ORIGIN : getUpCoordinate();
		return assignCharge(nextPos, charge);
	}
	
	private Coordinate getUpCoordinate() {
		Coordinate upCoordinate = new Coordinate(currentPos.x, currentPos.y - 1);
		if (upCoordinate.y < 0) {
			upCoordinate.y = height - 1;
		}
		return upCoordinate;
	}
	
	private boolean assignDown(int charge) {
		Coordinate nextPos = currentPos.equals(Coordinate.INITIAL_POS) ?
				Coordinate.ORIGIN : getDownCoordinate();
		return assignCharge(nextPos, charge);
	}
	
	private Coordinate getDownCoordinate() {
		return new Coordinate(currentPos.x, (currentPos.y + 1) % height);
	}
	
	private boolean assignLeft(int charge) {
		Coordinate nextPos = currentPos.equals(Coordinate.INITIAL_POS) ?
				Coordinate.ORIGIN : getLeftCoordinate();
		return assignCharge(nextPos, charge);
	}
	
	private Coordinate getLeftCoordinate() {
		Coordinate leftCoordinate = new Coordinate(currentPos.x - 1, currentPos.y);
		if (leftCoordinate.x < 0) {
			leftCoordinate.x = width - 1;
		}
		return leftCoordinate;
	}
	
	private boolean assignRight(int charge) {
		Coordinate nextPos = currentPos.equals(Coordinate.INITIAL_POS) ?
				Coordinate.ORIGIN : getRightCoordinate();		
		return assignCharge(nextPos, charge);
	}
	
	private Coordinate getRightCoordinate() {
		return new Coordinate((currentPos.x + 1) % width, currentPos.y);
	}
	
	public boolean assignCoordinate(final Coordinate coordinate, final int charge) {
		Coordinate cor = new Coordinate(coordinate);
		validateCoordinateBounds(cor);
		
		if (currentPos.equals(Coordinate.INITIAL_POS)) {
			offsetX = cor.x;
			offsetY = cor.y;
		}
		
		cor.x -= offsetX;
		if (cor.x < 0) {
			cor.x = width - cor.x;
		}
		cor.y -= offsetY;
		if (cor.y < 0) {
			cor.x = height - cor.y;
		}
		
		return assignCharge(cor, charge);
	}
	
	private boolean assignCharge(Coordinate nextPos, int charge) {
		validateAssignment(nextPos, charge);
		
		if (cells[nextPos.y][nextPos.x].isEmpty()) {
			cells[nextPos.y][nextPos.x].charge = charge;
			assignments.add(new Assignment(nextPos, charge));
			currentPos = nextPos;
			return true;
		} else {
			return false;
		}
	}
	
	private void validateAssignment(Coordinate coordinate, int charge) {
		if (charge != -1 && charge != 0 && charge != 1) {
			throw new IllegalArgumentException("Illegal charge: " + charge);
		}
				
		validateCoordinateBounds(coordinate);
		
		if (currentPos.equals(Coordinate.INITIAL_POS)) {
			if (!coordinate.equals(Coordinate.ORIGIN)) {
				throw new IllegalArgumentException("All sequences to be offset to start at origin.");
			}
		} else {
			if (!isCoordinatexNextToEachOther(currentPos, coordinate)) {
				throw new IllegalArgumentException("Illegal step from " + currentPos + " to " + coordinate);
			}
		}
	}
	
	private boolean isCoordinatexNextToEachOther(Coordinate coordinate1, Coordinate coordinate2) {
		if (coordinate1.x == coordinate2.x 
				&& (
						(coordinate1.y + 1) % height == coordinate2.y 
						|| (coordinate2.y + 1) % height == coordinate1.y
					) 
			) {
			return true;
		}
		
		if (coordinate1.y == coordinate2.y 
				&& (
						(coordinate1.x + 1) % width == coordinate2.x 
						|| (coordinate2.x + 1) % width == coordinate1.x
					) 
			) {
			return true;
		}
		
		return false;
	}
	
	private void validateCoordinateBounds(Coordinate coordinate) {
		if (coordinate.x <0 || coordinate.x >= width  || coordinate.y <0 || coordinate.y >= height) {
			throw new IllegalArgumentException(
					"Coordinate " + coordinate + " outside of " + width + "x" + height + " size grid");
		}
	}
	
	@Override
	public String toString() {
		String grid = "";
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				grid += cells[i][j].toString();
			}
			grid += "\n";
		}
		return grid;
	}
}
