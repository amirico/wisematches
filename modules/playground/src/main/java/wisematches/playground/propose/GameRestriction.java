package wisematches.playground.propose;

import wisematches.personality.player.Player;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameRestriction extends Serializable {
	void validatePlayer(Player player) throws ViolatedRestrictionException;
}
