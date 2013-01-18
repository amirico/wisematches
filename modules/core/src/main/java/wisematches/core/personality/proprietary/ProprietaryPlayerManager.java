package wisematches.core.personality.proprietary;

import wisematches.core.Personality;
import wisematches.core.personality.PlayerManager;
import wisematches.core.personality.proprietary.guest.GuestPlayer;
import wisematches.core.personality.proprietary.robot.RobotPlayer;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProprietaryPlayerManager implements PlayerManager {
	public ProprietaryPlayerManager() {
		GuestPlayer.GUEST.getId();
		RobotPlayer.DULL.getId();
		RobotPlayer.EXPERT.getId();
		RobotPlayer.TRAINEE.getId();
	}

	@Override
	public ProprietaryPlayer getPlayer(long pid) {
		if (pid < 1000) {
			return ProprietaryPlayer.computerPlayerMap.get(pid);
		}
		return null;
	}

	@Override
	public ProprietaryPlayer getPlayer(Personality personality) {
		if (personality instanceof ProprietaryPlayer) {
			return (ProprietaryPlayer) personality;
		}
		return getPlayer(personality.getId());
	}
}
