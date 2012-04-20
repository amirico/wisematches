package wisematches.playground.activity;

import wisematches.personality.Personality;

/**
 * TODO: very strange interface. Is it required?
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @deprecated #getMessages should be redefined instead
 */
@Deprecated
public interface ActivityManager {
	@Deprecated
	void messagesChecked(Personality person);
}
