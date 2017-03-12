
public class Cell {
	public final static int EMPTY = -2;	
	public int charge;
	
	public Cell() {
		this.charge = EMPTY;
	}
	
	public Cell(Cell other) {
		this.charge = other.charge;
	}
	
	public boolean isEmpty() {
		return charge == EMPTY;
	}
	
	@Override
	public String toString() {
		if (this.isEmpty()) {
			return "  ";
		} else {
			return charge >= 0 ? " " + Integer.toString(charge) : Integer.toString(charge);
		}
	}
}
