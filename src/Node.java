import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Node {
	private static List<Integer> chargeSequence;
	
	public Grid state;
	public List<Direction> legalMoves;
	public int seqPos;
	
	public Node parent;
	public Map<Direction, Node> children;
	
	public int n;
	public double q;
	
	public Node(Grid state, List<Integer> chargeSequence) {
		Node.chargeSequence = chargeSequence;
				
		state.assign(Direction.RIGHT, chargeSequence.get(0));
		state.assign(Direction.RIGHT, chargeSequence.get(1));
		this.state = state;
		this.seqPos = 2;
		this.legalMoves = state.getLegalMoves(chargeSequence.size() - seqPos);
		
		this.parent = null;
		this.children = new HashMap<>();
		
		this.n = 0;
		this.q = 0;
	}
	
	private Node(Grid state, int seqPos, Node parent) {
		this.state = state;
		this.seqPos = seqPos;
		this.legalMoves = state.getLegalMoves(chargeSequence.size() - seqPos);
		
		this.parent = parent;
		this.children = new HashMap<>();
		
		this.n = 0;
		this.q = 0;
	}
	
	public List<Integer> getRemainingChargeSequence() {
		return Node.chargeSequence.subList(seqPos, chargeSequence.size());
	}
	
	public boolean isTerminal() {
		return legalMoves.size() <= 0 || seqPos >= chargeSequence.size();
	}
	
	public boolean isFullyExpanded() {
		return legalMoves.size() == children.size();
	}
	
	public Node expand() {
		List<Direction> dirs = new ArrayList<>(legalMoves);
//		System.out.println("Legal moves: " + dirs);
		dirs.removeAll(children.keySet());
		Direction dir = dirs.get(0);
		
		Grid childState = new Grid(state);
		childState.assign(dir, chargeSequence.get(seqPos));
		
		Node childNode = new Node(childState, seqPos + 1, this);
		children.put(dir, childNode);
//		System.out.println("Expand " + dir);
		return childNode;		
	}
	
	public Entry<Direction, Node> bestChild(double c) {
		double maxScore = -Double.MAX_VALUE;
		Entry<Direction, Node> bestChild = null;
		
		for (Entry<Direction, Node> child : children.entrySet()) {
			double score = child.getValue().q / (double) child.getValue().n
					+ c * Math.sqrt(2.0 * Math.log(n) / (double) child.getValue().n);
			if (score > maxScore) {
				maxScore = score;
				bestChild = child;
			}
		}
		return bestChild;
	}
	
	@Override
	public String toString() {
		String s = state + "n: " + this.n + " q: " + this.q + " children: {";
		for (Entry<Direction, Node> child : this.children.entrySet()) {
			s += " " + child.getKey() + " " + child.getValue().q / child.getValue().n + " ";
		}
		s += "}";
		return s;
	}
}
