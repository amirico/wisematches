package wisematches.playground.tournament.impl.announcement;

import wisematches.playground.tournament.Announcement;
import wisematches.playground.tournament.AnnouncementListener;
import wisematches.playground.tournament.AnnouncementManager;
import wisematches.playground.tournament.AnnouncementSubscription;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractAnnouncementManager implements AnnouncementManager {
	protected final Lock lock = new ReentrantLock();

	private final Set<AnnouncementListener> announcementListeners = new CopyOnWriteArraySet<AnnouncementListener>();

	protected AbstractAnnouncementManager() {
	}

	@Override
	public void addAnnouncementListener(AnnouncementListener l) {
		if (l != null) {
			announcementListeners.add(l);
		}
	}

	@Override
	public void removeAnnouncementListener(AnnouncementListener l) {
		announcementListeners.remove(l);
	}

	protected void firePlayerSubscribed(AnnouncementSubscription request) {
		for (AnnouncementListener announcementListener : announcementListeners) {
			announcementListener.playerSubscribed(request);
		}
	}

	protected void firePlayerUnsubscribed(AnnouncementSubscription request) {
		for (AnnouncementListener announcementListener : announcementListeners) {
			announcementListener.playerUnsubscribed(request);
		}
	}

	protected void fireTournamentAnnounced(Announcement closedAnnouncement, Announcement newAnnouncement) {
		for (AnnouncementListener announcementListener : announcementListeners) {
			announcementListener.tournamentAnnounced(closedAnnouncement, newAnnouncement);
		}
	}
}
