import java.io.FileNotFoundException;

public class Task2 {
	public static void main(String args[]) throws FileNotFoundException {
		if (args.length != 1) {
			System.out.println("Usage: java Task2 <config file>");
			System.exit(1);
		}
		Config config = new Config(args[0]);
		
		try {
			ConfigurationVerifier.verifyConfiguration(config);
		} catch (DeadendConfigurationException e) {
			System.out.println("Deadend Configuration.");
			System.exit(0);
		}
		
		for (Assignment assign : config.assignments) {
			config.grid.assignCoordinate(assign.pos, assign.charge);
		}
		System.out.println(config.grid.computeScore());
	}
}
