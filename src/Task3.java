import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Task3 {
	private static final double C = 0.2;
	private static final int BUDGET = 100; //TODO make this algorithmic
	
	public static void main(String args[]) throws FileNotFoundException {
		if (args.length != 1) {
			System.out.println("Usage: java Task1 <input file>");
			System.exit(1);
		}
		Input input = new Input(args[0]);;
		
		Grid grid = new Grid(input.grid);
		List<Integer> chargeSequence = new LinkedList<>(input.chargeSequence);
		int norm = input.norm;
		
		Node v0 = new Node(grid, chargeSequence);
		while (v0.getRemainingChargeSequence().size() > 0) {
			v0 = uctSearch(v0, norm, BUDGET);
		}
		Grid endGrid = v0.state;
		
		System.out.println("Score:");
		System.out.println(endGrid.computeScore());
		System.out.println("Configuration:");
		System.out.println(endGrid);
		
		System.out.println("Verifying configuration...");
		ConfigurationVerifier.verifyConfiguration(input, endGrid.getAssignments());
		System.out.println("Configuration verified");

		System.out.println("Saving configuration...");
		ConfigurationWriter.writeConfigFile(input, endGrid.getAssignments(), endGrid.computeScore());
		System.out.println("Saved configuration to " + input.filename + ".out");
	}
	
	public static Node uctSearch(final Node v0, int norm, int budget) {
		for (int i = 0; i < budget; i++) {
			Node v1 = treePolicy(v0);
			double delta = defaultPolicy(v1, norm);
			backup(v1, delta);
		}
		System.out.println("UCT search on\n" + v0);
		System.out.println("Remaining sequence: " + v0.getRemainingChargeSequence());
		Entry<Direction, Node> bestChild = v0.bestChild(0);
		System.out.println("Best Child: " + bestChild.getKey());
		return bestChild.getValue();
	}
	
	public static Node treePolicy(Node v) {
//		System.out.println("Tree policy on:\n" + v);
		while (!v.isTerminal()) {
			if (!v.isFullyExpanded()) {
//				System.out.println("Expanding");
				return v.expand();
			} else {
//				System.out.println("Fully expanded -> move to best child");
				v = v.bestChild(C).getValue();
			}
		}
		return v;
	}
	
	public static void backup(Node v, double delta) {
//		System.out.println("Backup " + delta + " on\n" + v);
		while (v != null) {
			v.n += 1;
			v.q += delta;
			v = v.parent;
		}
	}
	
	private static double defaultPolicy(Node v, int norm) {
//		System.out.println("Default policy on\n" + v);
		try {
			Grid randomGrid = RandomAssignmentGenerator.randomAssignment(v.state, v.getRemainingChargeSequence());
			return (randomGrid.computeScore() + norm) / (2.0 * norm);
		} catch (DeadendConfigurationException e) {
			return 0;
		}
	}
}

