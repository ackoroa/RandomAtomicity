import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Task3 {
	private static int bestScore = 0;
	private static Grid bestGrid = null;
	
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
			if (v0.legalMoves.size() == 1) {
				v0 = v0.bestChild(0).getValue();
			} else {
				v0 = uctSearch(v0, norm, Parameters.getBudget(v0.getRemainingChargeSequence().size()));
			}
		}
		Grid endGrid = v0.state;
		
		if (bestScore > endGrid.computeScore()) {
			System.out.println("Random walk encountered better state.");
			endGrid = bestGrid;
		}

		int score = endGrid.computeScore();
		System.out.println("Score: " + score + " (" + (score + norm) / (2.0 * norm) + ")");
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
		System.out.println("Remaining sequence: " 
				+ v0.getRemainingChargeSequence().size() + " " 
				+ v0.getRemainingChargeSequence());
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
				v = v.bestChild(Parameters.C).getValue();
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
			int score = randomGrid.computeScore();
			
			if (score > Task3.bestScore) {
				bestGrid = randomGrid;
			}
			
			return (score + norm) / (2.0 * norm);
		} catch (DeadendConfigurationException e) {
			return 0;
		}
	}
}

