package wisematches.playground.propose;

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
}
