package wisematches.core.expiration.impl;

import org.slf4j.LoggerFactory;
import wisematches.core.expiration.MockExpirationType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class MockExpirationManager extends AbstractExpirationManager<Long, MockExpirationType> {
	private List<Long> terminated = new ArrayList<>();

	protected MockExpirationManager() {
		super(MockExpirationType.class, LoggerFactory.getLogger("wisematches.expiration.MockExpirationManager"));
	}

	@Override
	protected boolean executeTermination(Long id) {
		terminated.add(id);
		return true;
	}

	public List<Long> getTerminated() {
		return terminated;
	}
}
