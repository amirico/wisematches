package wisematches.server.web.services.notify.publisher.impl;

import java.util.concurrent.Future;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MessageNotificationPublisher extends FreeMarkerNotificationPublisher {
	protected MessageNotificationPublisher() {
		super("message");
	}

	@Override
	protected Future<Void> asd() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}
}
