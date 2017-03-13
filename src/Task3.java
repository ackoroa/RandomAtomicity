import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class Task3 {
	private static final double C = 0.2;
	private static final int BUDGET = 100; //TODO make this algorithmic
	
	public static void main(String args[]) throws FileNotFoundException {
		if (args.length != 2) {
			System.out.println("Usage: java Task1 <input file> <output file>");
			System.exit(1);
		}
		Input input = new Input(args[0]);;
		
		Grid grid = new Grid(input.grid);
		List<Integer> chargeSequence = new LinkedList<>(input.chargeSequence);
		while (chargeSequence.size() > 0) {
			Direction dir = uctSearch(grid, chargeSequence, BUDGET);
			grid.assign(dir, chargeSequence.get(0));
			chargeSequence.remove(0);
		}
		
		System.out.println("Score:");
		System.out.println(grid.computeScore());
		System.out.println("Configuration:");
		System.out.println(grid);
		
		System.out.println("Verifying configuration...");
		ConfigurationVerifier.verifyConfiguration(input, grid.getAssignments());
		System.out.println("Configuration verified");

		System.out.println("Saving configuration...");
		ConfigurationWriter.writeConfigFile(args[1], input, grid.getAssignments(), grid.computeScore());
		System.out.println("Saved configuration to " + args[1]);
	}
	
	public static Direction uctSearch(Grid grid, List<Integer> chargeSequence, int budget) {
		Node v0 = new Node(new Grid(grid), new LinkedList<Integer>(chargeSequence));
		for (int i = 0; i < budget; i++) {
			System.out.println("UCTSearch step " + i);
			Node v1 = treePolicy(v0);
			int delta = defaultPolicy(v1);
			backup(v1, delta);
		}
		return v0.bestAction();
	}
	
	public static Node treePolicy(Node v) {
		System.out.println("Tree policy on:\n" + v);
		while (!v.isTerminal()) {
			if (!v.isFullyExpanded()) {
				System.out.println("Expanding");
				return v.expand();
			} else {
				System.out.println("Fully expanded -> move to best child");
				v = v.bestChild(C);
			}
		}
		return v;
	}
	
	public static void backup(Node v, int delta) {
		System.out.println("Backup " + delta + " on\n" + v);
		while (v != null) {
			v.n += 1;
			v.q += delta;
			v = v.parent;
		}
	}
	
	private static int defaultPolicy(Node v) {
		System.out.println("Default policy on\n" + v);
		Grid randomGrid = RandomAssignmentGenerator.randomAssignment(v.state, v.getRemainingChargeSequence());
		return randomGrid.computeScore();
	}
}

