import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Node {
	private static List<Integer> chargeSequence;
	
	Grid state;
	int seqPos;
	Node parent;
	Map<Direction, Node> children;
	
	int n, q;
	
	public Node(Grid state, List<Integer> chargeSequence) {
		Node.chargeSequence = chargeSequence;
		
		this.state = state;
		this.seqPos = 0;
		
		this.parent = null;
		this.children = new HashMap<>();
		
		this.n = 0;
		this.q = 0;
	}
	
	private Node(Grid state, int seqPos, Node parent) {
		this.state = state;
		this.seqPos = seqPos;
		
		this.parent = parent;
		this.children = new HashMap<>();
		
		this.n = 0;
		this.q = 0;
	}
	
	public List<Integer> getRemainingChargeSequence() {
		return Node.chargeSequence.subList(seqPos, chargeSequence.size());
	}
	
	public boolean isTerminal() {
		return state.isDeadEnd() || seqPos >= chargeSequence.size();
	}
	
	public boolean isFullyExpanded() {
		return state.getLegalMoves().size() == children.size();
	}
	
	public Node expand() {
		List<Direction> dirs = state.getLegalMoves();
		System.out.println("Legal moves: " + dirs);
		dirs.removeAll(children.keySet());
		Direction dir = dirs.get(0);
		
		Grid childState = new Grid(state);
		childState.assign(dir, chargeSequence.get(seqPos));
		
		Node childNode = new Node(childState, seqPos + 1, this);
		children.put(dir, childNode);
		System.out.println("Expand " + dir);
		return childNode;		
	}
	
	public Node bestChild(double c) {
		return getBestChild(c).getValue();		
	}
	
	public Direction bestAction() {
		return getBestChild(0).getKey();
	}
	
	private Entry<Direction, Node> getBestChild(double c) {
		double maxScore = -Double.MAX_VALUE;
		Entry<Direction, Node> bestChild = null;
		
		for (Entry<Direction, Node> child : children.entrySet()) {
			double score = (double) q / (double) child.getValue().n
					+ c * Math.sqrt(2.0 * Math.log(n) / (double) child.getValue().n);
			if (score > maxScore) {
				maxScore = score;
				bestChild = child;
			}
		}
		System.out.println("Best child: " + bestChild.getKey());
		return bestChild;
	}
	
	@Override
	public String toString() {
		return state + "n: " + this.n + " q: " + this.q + " children: " + this.children.keySet();
	}
}
