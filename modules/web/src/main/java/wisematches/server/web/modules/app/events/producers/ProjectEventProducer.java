package wisematches.server.web.modules.app.events.producers;

import wisematches.server.testimonial.rating.PlayerRatingsManager;
import wisematches.server.web.modules.app.events.EventNotificator;
import wisematches.server.web.modules.app.events.EventProducer;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ProjectEventProducer implements EventProducer {
	private EventNotificator notificator;

	public void activateProducer(EventNotificator notificator) {
		this.notificator = notificator;
	}

	public void deactivateProducer() {
		this.notificator = null;
	}

	public void setRatingsManager(PlayerRatingsManager ratingsManager) {
	}
}
