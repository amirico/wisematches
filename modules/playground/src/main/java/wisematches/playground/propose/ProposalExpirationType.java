package wisematches.playground.propose;

import wisematches.playground.expiration.ExpirationType;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum ProposalExpirationType implements ExpirationType {
	THREE_DAYS("proposal.expiring.days.three", 3 * 24 * 60 * 60 * 1000),

	ONE_DAY("proposal.expiring.days.one", 24 * 60 * 60 * 1000);

	private final String code;
	private final long triggerDiff;

	ProposalExpirationType(String code, long triggerDiff) {
		this.code = code;
		this.triggerDiff = triggerDiff;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public long getTriggerTime(long extinctionTime) {
		return extinctionTime - triggerDiff;
	}
}
