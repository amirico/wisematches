package wisematches.server.services.relations.blacklist;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BlacklistRecord {
	long getPerson();

	long getWhom();

	Date getSince();

	String getMessage();
}