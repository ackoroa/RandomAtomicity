import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class RandomAssignmentGenerator {
	
	public static Grid randomAssignment(final Grid grid, List<Integer> chargeSequence, boolean nullable) {
		Random rand = new Random(Calendar.getInstance().getTimeInMillis());
		Grid newGrid = new Grid(grid);
		
		int deadendCount = 0;
		for (int i = 0; i < Math.min(chargeSequence.size(), Parameters.MAX_LOOKAHEAD); i++) {
			List<Direction> dirs = newGrid.getLegalMoves(chargeSequence.size() - i);
			if (dirs.isEmpty()) {
				deadendCount++;
				if (deadendCount > Parameters.DEADEND_LIMIT) {
					if (nullable) {
						return null;
					} else { 
						throw new DeadendConfigurationException("Deadend configuration reached after " + Parameters.DEADEND_LIMIT + " tries.\n" 
								+ newGrid + chargeSequence.subList(i, chargeSequence.size()));
					}
				}

				newGrid = new Grid(grid);
				i = -1;
				dirs = newGrid.getLegalMoves(chargeSequence.size() - i);
				continue;
			}
			
			int charge = chargeSequence.get(i);
			newGrid.assign(dirs.get(rand.nextInt(dirs.size())), charge);
		}
		
		return newGrid;
	}
}
