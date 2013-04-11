package wisematches.server.services.abuse;

import wisematches.server.services.message.Message;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AbuseReportManager {
	void addAbuseReportListener(AbuseReportListener listener);

	void removeAbuseReportListener(AbuseReportListener listener);


	void reportAbuseMessage(Message message);
}
