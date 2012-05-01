package wisematches.playground.criteria;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class CriterionViolation implements Serializable {
    private final String code;
    private final Object expected;
    private final Object received;
    private final PlayerCriterion criterion;

    private static final long serialVersionUID = 4808346159699224069L;

    public CriterionViolation(String code, Object received) {
        this(code, received, null);
    }

    public CriterionViolation(String code, Object received, Object expected) {
        this(code, received, expected, null);
    }

    public CriterionViolation(String code, Object received, Object expected, PlayerCriterion criterion) {
        if (code == null) {
            throw new NullPointerException("Code can't be null");
        }
        this.code = code;
        this.expected = expected;
        this.received = received;
        this.criterion = criterion;
    }

    public String getCode() {
        return code;
    }

    public Object getExpected() {
        return expected;
    }

    public Object getReceived() {
        return received;
    }

    public PlayerCriterion getCriterion() {
        return criterion;
    }

    @Override
    public String toString() {
        return "CriterionViolation{" +
                "code='" + code + '\'' +
                ", expected=" + expected +
                ", received=" + received +
                ", criterion=" + criterion +
                '}';
    }
}
