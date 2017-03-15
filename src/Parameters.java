
public class Parameters {
	public static final int DEADEND_LIMIT = 10;
	public static final double C = 0.2;

	public static int getBudget(int seqLen) {
		int budget = (int) Math.pow(seqLen, 2);
		System.out.println("Budget: " + budget);
		return budget;
	}
}
