
public class Assignment {
	public Coordinate pos;
	public int charge;
	
	public Assignment(Coordinate pos, int charge) {
		this.pos = pos;
		this.charge = charge;
	}
	
	@Override
	public String toString() {
		return pos.x + " " + pos.y + " " + charge;
	}
}
