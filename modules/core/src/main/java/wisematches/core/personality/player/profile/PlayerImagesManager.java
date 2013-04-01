package wisematches.core.personality.player.profile;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@code PlayerImagesManager} allows get and set image for players.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerImagesManager {
	void addPlayerImagesListener(PlayerImagesListener l);

	void removePlayerImagesListener(PlayerImagesListener l);

	/**
	 * Returns image for specified source or {@code null} if image for player is not specified.
	 *
	 * @param playerId the player whose image should be returned.
	 * @param type     the type of image that should be returned.
	 * @return image for specified player or {@code null}
	 * @throws NullPointerException if {@code type} is {@code null}
	 */
	InputStream getPlayerImage(long playerId, PlayerImageType type);

	/**
	 * Updates image for specified player.
	 * <p/>
	 * If {@code type} of image is not {@code PlayerImageType#REAL} when image will be automatical resized to
	 * specified dimension.
	 *
	 * @param playerId the player whose image should be updated.
	 * @param stream   the stream with new image.
	 * @param type     the type of updating image.
	 * @throws NullPointerException      if {@code stream} is {@code null}
	 *                                   or {@code type} is {@code null}.
	 * @throws UnsupportedImageException if specified stream does not contains image data.
	 * @throws java.io.IOException       if {@code stream} throw {@code java.io.IOException}
	 */
	void setPlayerImage(long playerId, InputStream stream, PlayerImageType type) throws UnsupportedImageException, IOException;

	/**
	 * Removes image of specified type from specified player.
	 * <p/>
	 * This method doesn't do anything if player does not have image of specified type.
	 *
	 * @param playerId the player whose image should be removed
	 * @param type     the type of removed image.
	 * @throws NullPointerException if {@code type} is {@code null}
	 */
	void removePlayerImage(long playerId, PlayerImageType type);
}