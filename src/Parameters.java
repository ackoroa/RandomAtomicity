
public class Parameters {
	public static final int DEADEND_LIMIT = 10;
	
	public static final boolean USE_BUDGET_CAP = true;
	public static final int MAX_BUDGET = 300;
	public static final int MAX_LOOKAHEAD = 100;

	public static final boolean USE_ENSEMBLE_SEARCH = true;
	public static final int ENSEMBLE_SIZE = 5;
	
	public static final double C_EXPLORE = 0.2;
	public static final double C_PARTIAL = 0.1;
	
	public static int getBudget(int seqLen) {
		int budget = (int) Math.pow(seqLen, 1.5) + 4;
		if (USE_BUDGET_CAP) {
			budget = Math.min(budget, MAX_BUDGET);
		}
		return budget;
	}
}
