import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Config {
	final public int lX, lY;
	final public Grid grid;
	final public List<Integer> chargeSequence;
	final public List<Assignment> assignments;
	final public int score;
		
	public Config(String configFile) throws FileNotFoundException {
		Scanner sc = new Scanner(new BufferedReader(
				new InputStreamReader(new FileInputStream(
						new File(configFile)))));
		String[] input = sc.nextLine().split(" ");
		
		this.lX = Integer.valueOf(input[0].trim());
		this.lY = Integer.valueOf(input[1].trim());
		this.grid = new Grid(this.lX, this.lY);
		
		this.chargeSequence = new LinkedList<>();
		for (int i = 2; i < input.length; i++) {
			chargeSequence.add(Integer.valueOf(input[i].trim()));
		}
		
		this.assignments = new ArrayList<>();
		input = sc.nextLine().split(" ");
		while (input.length == 3) {
			int x = Integer.valueOf(input[0].trim());
			int y = Integer.valueOf(input[1].trim());
			int q = Integer.valueOf(input[2].trim());
			assignments.add(new Assignment(new Coordinate(x, y), q));
			input = sc.nextLine().split(" ");
		}
		this.score = Integer.valueOf(input[0].trim());
		
		sc.close();
	}
}
