import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Input {
	final public String filename;
	
	final public int lX, lY;
	final public Grid grid;
	final public List<Integer> chargeSequence;
	
	final public int norm;
	
	public Input(String inputFile) throws FileNotFoundException {
		Scanner sc = new Scanner(new BufferedReader(
				new InputStreamReader(new FileInputStream(
						new File(inputFile)))));
		this.filename = inputFile.split("\\.")[0];
		
		this.lX = sc.nextInt();
		this.lY = sc.nextInt();
		this.grid = new Grid(this.lX, this.lY);
		
		int norm = 0;
		this.chargeSequence = new ArrayList<>();
		while (sc.hasNext()) {
			int q = sc.nextInt();
			chargeSequence.add(q);
			if (q != 0) {
				norm++;
			}
		}
		this.norm = norm;
		
		sc.close();
	}
}
