package wisematches.core.personality.member.account;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AccountNicknameLockInfo extends Serializable {
	String getUsername();

	String getReason();
}
