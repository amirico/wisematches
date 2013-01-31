package wisematches.playground.propose;

import wisematches.core.Player;
import wisematches.playground.GameSettings;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PrivateProposal<S extends GameSettings> extends GameProposal<S> {
	/**
	 * Returns associated with the proposal comment
	 *
	 * @return the associated with the proposal comment
	 */
	String getCommentary();

	/**
	 * Returns {@code true} if specified player already joined to the proposal.
	 *
	 * @param player the player to be checked
	 * @return {@code true} if specified player already joined to the proposal; {@code false} - otherwise.
	 */
	boolean isPlayerJoined(Player player);
}
