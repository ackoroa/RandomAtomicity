import java.util.List;

public class ConfigurationVerifier {
	public static void verifyConfiguration(final Input input, final List<Assignment> config) {
		verifyConfiguration(input.grid, input.chargeSequence, config);
	}
	
	public static void verifyConfiguration(final Config config) {
		int score = verifyConfiguration(config.grid, config.chargeSequence, config.assignments);
		if (score != config.score) {
			throw new IllegalArgumentException("Configuration score != computed score");
		}
	}
	
	private static int verifyConfiguration(Grid grid, List<Integer> chargeSequence, List<Assignment> config) {
		if (config.size() != chargeSequence.size()) {
			throw new IllegalArgumentException("Configuration length != input length");
		}
		
		Grid verifyGrid = new Grid(grid);
		for (int i = 0; i < config.size(); i++) {
			if (config.get(i).charge != chargeSequence.get(i)) {
				throw new IllegalArgumentException("Configuration charge order != input charge order");
			}
			
			boolean assigned = verifyGrid.assignCoordinate(config.get(i).pos, config.get(i).charge);
			if (!assigned) {
				throw new DeadendConfigurationException("Line " + i + ": " + config.get(i));				
			}
		}
		
		return verifyGrid.computeScore();
	}
}
