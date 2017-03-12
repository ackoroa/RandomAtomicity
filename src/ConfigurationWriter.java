import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class ConfigurationWriter {
	public static void writeConfigFile(String configFile, List<Assignment> config) throws FileNotFoundException {
		File output = new File(configFile);
		PrintWriter pr = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(output))));
		
		for (Assignment assign : config) {
			pr.println(assign);
		}
		pr.close();
		
	}
}
