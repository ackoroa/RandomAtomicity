import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Task3 {
	private static int bestRandomScore = 0;
	private static Grid bestRandomGrid = null;
	
	public static void main(String args[]) throws FileNotFoundException {
		if (args.length != 1) {
			System.out.println("Usage: java Task1 <input file>");
			System.exit(1);
		}
		Input input = new Input(args[0]);;
		
		Grid grid = new Grid(input.grid);
		List<Integer> chargeSequence = new ArrayList<>(input.chargeSequence);
		int norm = input.norm;
	
		Node result = ensembleSearch(grid, chargeSequence, norm, Parameters.C_EXPLORE, Parameters.C_PARTIAL);		
		processResult(input, result);
	}
	
	private static Node search(Grid grid, List<Integer> chargeSequence, int norm, double cExplore, double cPartial) {
		Node v0 = new Node(grid, chargeSequence, norm, cExplore, cPartial);
		while (v0.getRemainingChargeSequence().size() > 0) {
			if (v0.legalMoves.size() == 1) {
				v0 = v0.bestChild(0, 0).getValue();
			} else {
				v0 = uctSearch(v0, norm, 
						Parameters.getBudget(v0.getRemainingChargeSequence().size()),
						cExplore, cPartial);
			}
			v0.parent = null;
		}
		return v0;
	}

	//TODO vary Cs for ensemble automatically
	private static Node ensembleSearch(Grid grid, List<Integer> chargeSequence, int norm, double cExplore, double cPartial) {
		List<Node> trees = new ArrayList<>();
		for (int i = 0; i < Parameters.ENSEMBLE_SIZE; i++) {
			trees.add(new Node(new Grid(grid), chargeSequence, norm, cExplore, cPartial));
		}
		
		int i = 0;
		int total = trees.get(0).getRemainingChargeSequence().size();
		while (trees.get(0).getRemainingChargeSequence().size() > 0) {
			i++;
			int budget = Parameters.getBudget(trees.get(0).getRemainingChargeSequence().size());
			System.out.println("Step " + i + "/" + total + " budget: " + budget + " " + new Date());
			
			
			List<Direction> childDirs = new ArrayList<>();
			for (Node v0 : trees) {
				childDirs.add(uctSearchDir(v0, norm, budget, cExplore, cPartial));
			}
			
			Map<Direction, Integer> countDirs = new HashMap<>();
			for (Direction dir : childDirs) {
				 if (!countDirs.containsKey(dir)) {
					 countDirs.put(dir, 0);
				 }
				 countDirs.put(dir, countDirs.get(dir) + 1);
			}
			
			int bestCount = -1;
			Direction bestDir = null;
			for (Entry<Direction, Integer> dirCount : countDirs.entrySet()) {
				if (dirCount.getValue() > bestCount) {
					bestCount = dirCount.getValue();
	 				bestDir = dirCount.getKey();
				}
			}
			
			List<Node> childTrees = new ArrayList<>();
			for (Node tree : trees) {
				Node bestChild = tree.children.get(bestDir);
				bestChild.parent = null;
				childTrees.add(bestChild);
			}
			trees = childTrees;
		}

		return trees.get(0);
	}
	
	private static void processResult(Input input, Node v) throws FileNotFoundException {
		Grid endGrid = v.state;
		int score = endGrid.computeScore();
		
		System.out.println("Best Random Score: " + bestRandomScore + " (" + (bestRandomScore + input.norm) / (2.0 * input.norm) + ")");
		System.out.println("Best UCT Score: " + score + " (" + (score + input.norm) / (2.0 * input.norm) + ")");
				
		if (bestRandomScore > score) {
			System.out.println("Random walk encountered better state.");
			endGrid = bestRandomGrid;
		}
		
		System.out.println("Configuration:");
		System.out.println(endGrid);
		
		System.out.println("Verifying configuration...");
		ConfigurationVerifier.verifyConfiguration(input, endGrid.getAssignments());
		System.out.println("Configuration verified");

		System.out.println("Saving configuration...");
		ConfigurationWriter.writeConfigFile(input, endGrid.getAssignments(), endGrid.computeScore());
		System.out.println("Saved configuration to " + input.filename + ".out");
	}
	
	public static Node uctSearch(final Node v0, int norm, int budget, double cExplore, double cPartial) {
		return uctSearchWithDir(v0, norm, 
				Parameters.getBudget(v0.getRemainingChargeSequence().size()),
				cExplore, cPartial).getValue();
	}
	
	public static Direction uctSearchDir(final Node v0, int norm, int budget, double cExplore, double cPartial) {
		return uctSearchWithDir(v0, norm, 
				Parameters.getBudget(v0.getRemainingChargeSequence().size()),
				cExplore, cPartial).getKey();
	}
	
	public static Entry<Direction, Node> uctSearchWithDir(final Node v0, int norm, int budget, double cExplore, double cPartial) {
//		System.out.println("UCT search on\n" + v0);
//		System.out.println("Remaining sequence: " 
//				+ v0.getRemainingChargeSequence().size() + " " 
//				+ v0.getRemainingChargeSequence());
//		System.out.println("Budget: " + budget);
		
		for (int i = 0; i < budget; i++) {
			Node v1 = treePolicy(v0, cExplore, cPartial);
			double delta = defaultPolicy(v1, norm);
			backup(v1, delta);
		}
		Entry<Direction, Node> bestChild = v0.bestChild(0, 0);
		
//		System.out.println("Best Child: " + bestChild.getKey() + " " 
//				+ v0.computeUCTScore(bestChild.getValue(), 0, 0));
//		System.out.println();
		return bestChild;
	}
	
	public static Node treePolicy(Node v, double cExplore, double cPartial) {
//		System.out.println("Tree policy on:\n" + v);
		while (!v.isTerminal()) {
			if (!v.isFullyExpanded()) {
//				System.out.println("Expanding");
				return v.expand();
			} else {
//				System.out.println("Fully expanded -> move to best child");
				v = v.bestChild(cExplore, cPartial).getValue();
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
		Grid randomGrid = RandomAssignmentGenerator.randomAssignment(v.state, v.getRemainingChargeSequence(), true);
		if (randomGrid == null) {
			return 0;
		}
		
		int score = randomGrid.computeScore();
		if (score > Task3.bestRandomScore) {
			bestRandomScore = score;
			bestRandomGrid = randomGrid;
		}
		return (score + norm) / (2.0 * norm);
	}
}

