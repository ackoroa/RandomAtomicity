import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Input {
	final public int lX, lY;
	final public Grid grid;
	final public List<Integer> chargeSequence;
	
	public Input(String inputFile) throws FileNotFoundException {
		Scanner sc = new Scanner(new BufferedReader(
				new InputStreamReader(new FileInputStream(
						new File(inputFile)))));
		
		this.lX = sc.nextInt();
		this.lY = sc.nextInt();
		this.grid = new Grid(this.lX, this.lY);
		
		this.chargeSequence = new LinkedList<>();
		while (sc.hasNext()) {
			chargeSequence.add(sc.nextInt());
		}
		sc.close();
	}
}
