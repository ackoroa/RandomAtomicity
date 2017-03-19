
public class Parameters {
	public static final int DEADEND_LIMIT = 10;
	public static final int ENSEMBLE_SIZE = 5;
	public static final double C_EXPLORE = 0.2;
	public static final double C_PARTIAL = 0.1;
	public static final int MAX_LOOKAHEAD = 100;

	public static int getBudget(int seqLen) {
		int budget = Math.min(300, (int) Math.pow(seqLen, 1.2) + 4);
		return budget;
	}
}
