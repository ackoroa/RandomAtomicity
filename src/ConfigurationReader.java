import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConfigurationReader {
	public static List<Assignment> readConfigFile(String configFile) throws FileNotFoundException {
		Scanner sc = new Scanner(new BufferedReader(
				new InputStreamReader(new FileInputStream(
						new File(configFile)))));
		
		List<Assignment> assignments = new ArrayList<>();
		while (sc.hasNext()) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			int q = sc.nextInt();
			assignments.add(new Assignment(new Coordinate(x, y), q));
		}
		sc.close();
		
		return assignments;
	}
}
