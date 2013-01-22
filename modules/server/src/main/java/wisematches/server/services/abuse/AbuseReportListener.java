package wisematches.server.services.abuse;

import wisematches.server.services.message.Message;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AbuseReportListener {
	void abuseMessage(Message message);
}
