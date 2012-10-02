package wisematches.playground.propose;

import wisematches.playground.expiration.ExpirationType;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum ProposalExpirationType implements ExpirationType {
    THREE_DAYS("playground.challenge.expiration.days", 3 * 24 * 60 * 60 * 1000),

    ONE_DAY("playground.challenge.expiration.day", 24 * 60 * 60 * 1000);

    private final String code;
    private final long remainedMillis;

    ProposalExpirationType(String code, long remainedMillis) {
        this.code = code;
        this.remainedMillis = remainedMillis;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public long getRemainedTime() {
        return remainedMillis;
    }

    @Override
    public long getTriggerTime(long extinctionTime) {
        return extinctionTime - remainedMillis;
    }
}
