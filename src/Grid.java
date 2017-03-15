import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Grid {
	private int width;
	private int height;
	private int offsetX;
	private int offsetY;
	
	private Coordinate currentPos;
	private Cell[][] cells;
	private List<Assignment> assignments;
	private boolean movedVertical;
	
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		this.offsetX = 0;
		this.offsetY = 0;
		
		this.currentPos = Coordinate.INITIAL_POS;
		this.cells = new Cell[height][width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j] = new Cell();
			}
		}
		this.assignments = new ArrayList<>();
		this.movedVertical = false;
	}
	
	public Grid(Grid other) {
		this.width = other.width;
		this.height = other.height;
		this.offsetX = other.offsetX;
		this.offsetY = other.offsetY;
		
		this.currentPos = other.currentPos;
		this.cells = new Cell[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				cells[i][j] = new Cell(other.cells[i][j]);
			}
		}
		this.assignments = new ArrayList<>(other.assignments);
		this.movedVertical = other.movedVertical;
	}
	
	public List<Assignment> getAssignments() {
		return assignments;
	}
	
	public int computeScore() {
		int score = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (cells[i][j].isEmpty()) {
					continue;
				}
				if (!cells[(i + 1) % height][j].isEmpty()) {
					score += cells[i][j].charge * cells[(i + 1) % height][j].charge;
				}
				if (!cells[i][(j + 1) % width].isEmpty()) {
					score += cells[i][j].charge * cells[i][(j + 1) % width].charge;
				}
			}
		}
		return score;
	}
	
	public List<Direction> getLegalMoves(int seqLength) {
		List<Direction> dirs = new ArrayList<>();
		if (currentPos.equals(Coordinate.INITIAL_POS) || currentPos.equals(Coordinate.ORIGIN)) {
			dirs.add(Direction.RIGHT);
			return dirs;
		}
		
		Coordinate up = getUpCoordinate(currentPos);
		Coordinate down = getDownCoordinate(currentPos);
		Coordinate left = getLeftCoordinate(currentPos);
		Coordinate right = getRightCoordinate(currentPos);
		
		if (cells[up.y][up.x].isEmpty() && movedVertical && isEnoughReachableCells(Direction.UP, seqLength)) {
			dirs.add(Direction.UP);
		}
		if (cells[down.y][down.x].isEmpty() && isEnoughReachableCells(Direction.DOWN, seqLength)) {
			dirs.add(Direction.DOWN);
		}
		if (cells[left.y][left.x].isEmpty() && isEnoughReachableCells(Direction.LEFT, seqLength)) {
			dirs.add(Direction.LEFT);
		}
		if (cells[right.y][right.x].isEmpty() && isEnoughReachableCells(Direction.RIGHT, seqLength)) {
			dirs.add(Direction.RIGHT);
		}
		
		return dirs;
	}
	
	private boolean isEnoughReachableCells(Direction dir, int seqLength) {
		Coordinate initPos = getNextCoordinate(currentPos, dir);
		
		for (Direction nextDir : Direction.values()) {
			boolean[][] tempCells = new boolean[height][width];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					tempCells[i][j] = !this.cells[i][j].isEmpty();
				}
			}
			tempCells[initPos.y][initPos.x] = true;
			
			int reachable = getReachableCellsOnOneSide(tempCells, getNextCoordinate(initPos, nextDir));
			if (reachable >= seqLength -1) {
				return true;
			}
		}
		return false;
	}
	
	private int getReachableCellsOnOneSide(boolean[][] tempCells, Coordinate startPos) {
		Queue<Coordinate> q = new LinkedList<>();
		q.add(startPos);
				
		int reachable = 0;
		while (!q.isEmpty()) {
			Coordinate pos = q.poll();
			if (tempCells[pos.y][pos.x]) {
				continue;
			}
			tempCells[pos.y][pos.x] = true;
			reachable++;
			
			for (Direction dir : Direction.values()) {
				Coordinate nextPos = getNextCoordinate(pos, dir);
				if (!tempCells[nextPos.y][nextPos.x]) {
					q.add(nextPos);
				}
			}		
		}
		return reachable;
	}
		
	public boolean assign(Direction dir, int charge) {
		boolean assigned;
		switch (dir) {
		case UP:
			assigned = assignUp(charge);
			if (assigned) {
				movedVertical = true;
			}
			return assigned;
		case DOWN:
			assigned = assignDown(charge);
			if (assigned) {
				movedVertical = true;
			}
			return assigned;
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
				Coordinate.ORIGIN : getUpCoordinate(currentPos);
		return assignCharge(nextPos, charge);
	}
	
	private boolean assignDown(int charge) {
		Coordinate nextPos = currentPos.equals(Coordinate.INITIAL_POS) ?
				Coordinate.ORIGIN : getDownCoordinate(currentPos);
		return assignCharge(nextPos, charge);
	}
	
	private boolean assignLeft(int charge) {
		Coordinate nextPos = currentPos.equals(Coordinate.INITIAL_POS) ?
				Coordinate.ORIGIN : getLeftCoordinate(currentPos);
		return assignCharge(nextPos, charge);
	}
	
	private boolean assignRight(int charge) {
		Coordinate nextPos = currentPos.equals(Coordinate.INITIAL_POS) ?
				Coordinate.ORIGIN : getRightCoordinate(currentPos);		
		return assignCharge(nextPos, charge);
	}
	
	private Coordinate getNextCoordinate(Coordinate pos, Direction dir) {
		switch (dir) {
		case UP:
			return getUpCoordinate(pos);
		case DOWN:
			return getDownCoordinate(pos);
		case LEFT:
			return getLeftCoordinate(pos);
		case RIGHT:
			return getRightCoordinate(pos);
		default:
			throw new IllegalArgumentException();
		}
	}
	
	private Coordinate getUpCoordinate(Coordinate pos) {
		Coordinate upCoordinate = new Coordinate(pos.x, pos.y - 1);
		if (upCoordinate.y < 0) {
			upCoordinate.y = height - 1;
		}
		return upCoordinate;
	}
	
	private Coordinate getDownCoordinate(Coordinate pos) {
		return new Coordinate(pos.x, (pos.y + 1) % height);
	}
	
	private Coordinate getLeftCoordinate(Coordinate pos) {
		Coordinate leftCoordinate = new Coordinate(pos.x - 1, pos.y);
		if (leftCoordinate.x < 0) {
			leftCoordinate.x = width - 1;
		}
		return leftCoordinate;
	}
	
	private Coordinate getRightCoordinate(Coordinate pos) {
		return new Coordinate((pos.x + 1) % width, pos.y);
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
			cor.x = width + cor.x;
		}
		cor.y -= offsetY;
		if (cor.y < 0) {
			cor.y = height + cor.y;
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
		String grid = currentPos + "\n";
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				grid += cells[i][j].toString();
			}
			grid += "\n";
		}
		return grid;
	}
}
