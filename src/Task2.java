import java.io.FileNotFoundException;
import java.util.List;

public class Task2 {
	public static void main(String args[]) throws FileNotFoundException {
		if (args.length != 2) {
			System.out.println("Usage: java Task2 <input file> <config file>");
			System.exit(1);
		}
		Input input = new Input(args[0]);
		List<Assignment> config = ConfigurationReader.readConfigFile(args[1]);
		
		try {
			ConfigurationVerifier.verifyConfiguration(input, config);
		} catch (DeadendConfigurationException e) {
			System.out.println("Deadend Configuration.");
			System.exit(0);
		}
		
		for (Assignment assign : config) {
			input.grid.assignCoordinate(assign.pos, assign.charge);
		}
		System.out.println(input.grid.computeScore());
	}
}
