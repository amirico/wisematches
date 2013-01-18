package wisematches.core.personality.proprietary.guest;

import wisematches.core.personality.PlayerType;
import wisematches.core.personality.proprietary.ProprietaryPlayer;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GuestPlayer extends ProprietaryPlayer {
	public static final GuestPlayer GUEST = new GuestPlayer(1, "guest", PlayerType.GUEST);

	private GuestPlayer(long id, String nickname, PlayerType playerType) {
		super(id, nickname, playerType);
	}
}
