import java.util.List;

public class ConfigurationVerifier {
	public static void verifyConfiguration(final Input input, final List<Assignment> config) {
		if (config.size() != input.chargeSequence.size()) {
			throw new IllegalArgumentException("Configuration length != input length");
		}
		
		Grid verifyGrid = new Grid(input.grid);
		for (int i = 0; i < config.size(); i++) {
			if (config.get(i).charge != input.chargeSequence.get(i)) {
				throw new IllegalArgumentException("Configuration charge order != input charge order");
			}
			
			boolean assigned = verifyGrid.assignCoordinate(config.get(i).pos, config.get(i).charge);
			if (!assigned) {
				throw new DeadendConfigurationException("Line " + i + ": " + config.get(i));				
			}
		}
	}
}
