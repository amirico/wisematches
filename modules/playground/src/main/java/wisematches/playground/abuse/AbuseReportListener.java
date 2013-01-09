package wisematches.playground.abuse;

import wisematches.playground.message.Message;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AbuseReportListener {
	void abuseMessage(Message message);
}
