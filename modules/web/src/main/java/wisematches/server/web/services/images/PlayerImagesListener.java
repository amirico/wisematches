package wisematches.server.web.services.images;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerImagesListener {
	void playerImageAdded(long playerId, PlayerImageType type);

	void playerImageUpdated(long playerId, PlayerImageType type);

	void playerImageRemoved(long playerId, PlayerImageType type);
}
