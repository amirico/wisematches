package wisematches.core.personality;

import wisematches.core.Personality;

/**
 * TODO: track account & membership changes.
 * <p/>
 * Players manager interface. This
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerManager {
	Player getPlayer(long pid);

	Player getPlayer(Personality personality);
}