package wisematches.playground.abuse.impl;

import wisematches.playground.abuse.AbuseReportListener;
import wisematches.playground.abuse.AbuseReportManager;
import wisematches.playground.message.Message;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DummyAbuseReportManager implements AbuseReportManager {
	private final Set<AbuseReportListener> listeners = new CopyOnWriteArraySet<>();

	public DummyAbuseReportManager() {
	}

	@Override
	public void addAbuseReportListener(AbuseReportListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeAbuseReportListener(AbuseReportListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void reportAbuseMessage(Message message) {
		for (AbuseReportListener listener : listeners) {
			listener.abuseMessage(message);
		}
	}
}
