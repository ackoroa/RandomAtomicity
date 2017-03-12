import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class RandomAssignmentGenerator {
	private static final int DEADEND_LIMIT = 10;
	private static Random rand = new Random(Calendar.getInstance().getTimeInMillis());
	
	public static Grid randomAssignment(final Grid grid, List<Integer> chargeSequence) {
		Grid newGrid = new Grid(grid);
		
		int failedCount = 0;
		for (int i = 0; i < chargeSequence.size(); i++) {
			if (newGrid.isDeadEnd()) {
				failedCount++;
				if (failedCount >= DEADEND_LIMIT) {
					throw new DeadendConfigurationException("Deadend configuration for " + DEADEND_LIMIT + " tries");
				}
				newGrid = new Grid(grid);
				i = 0;
			}
			
			List<Direction> dirs = newGrid.getLegalMoves();
			int charge = chargeSequence.get(i);
			newGrid.assign(dirs.get(rand.nextInt(dirs.size())), charge);
		}
		
		return newGrid;
	}
}
