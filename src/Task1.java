import java.io.FileNotFoundException;

public class Task1 {
	public static void main(String args[]) throws FileNotFoundException {
		if (args.length != 1) {
			System.out.println("Usage: java Task1 <input file>");
			System.exit(1);
		}
		Input input = new Input(args[0]);
		
		System.out.println("Generating random configuration...");
		Grid endGrid = RandomAssignmentGenerator.randomAssignment(input.grid, input.chargeSequence);
		
		System.out.println("Configuration:");
		for (Assignment assign : endGrid.getAssignments()) {
			System.out.println(assign);
		}
		System.out.println("Configuration:");
		System.out.println(endGrid);
		System.out.println("Score: " + endGrid.computeScore());
		
		System.out.println("Verifying configuration...");
		ConfigurationVerifier.verifyConfiguration(input, endGrid.getAssignments());
		System.out.println("Configuration verified");

		System.out.println("Saving configuration...");
		ConfigurationWriter.writeConfigFile(input, endGrid.getAssignments(), endGrid.computeScore());
		System.out.println("Saved configuration to " + input.filename + ".out");
	}
}
