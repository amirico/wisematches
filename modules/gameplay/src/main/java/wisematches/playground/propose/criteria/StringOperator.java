package wisematches.playground.propose.criteria;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum StringOperator {
	EQ() {
		@Override
		boolean test(String a, String b) {
			return a.equals(b);
		}
	},
	EQ_NO_CASE() {
		@Override
		boolean test(String a, String b) {
			return a.equalsIgnoreCase(b);
		}
	},
	MATCH() {
		@Override
		boolean test(String a, String b) {
			return a.matches(b);
		}
	};

	abstract boolean test(String a, String b);
}
