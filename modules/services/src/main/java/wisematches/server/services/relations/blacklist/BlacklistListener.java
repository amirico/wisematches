package wisematches.server.services.relations.blacklist;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BlacklistListener {
	void playerAdded(BlacklistRecord record);

	void playerRemoved(BlacklistRecord record);
}
