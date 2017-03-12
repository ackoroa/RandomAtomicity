
public class Coordinate {
	public final static Coordinate INITIAL_POS = new Coordinate(-1, -1);
	public static final Coordinate ORIGIN = new Coordinate(0, 0);
	public int x;
	public int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate(Coordinate other) {
		this.x = other.x;
		this.y = other.y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Coordinate) {
			Coordinate other = (Coordinate) o;
			return this.x == other.x && this.y == other.y;
		}
		return false;
	}
	
	@Override 
	public String toString() {
		return "{" + x + "," + y + "}";
	}
}
