package wisematches.server.core.account;

import wisematches.server.player.locks.LockAccountInfo;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class AccountLockedException extends AccountException {
	private final LockAccountInfo lockAccountInfo;

	public AccountLockedException(LockAccountInfo lockAccountInfo) {
		this.lockAccountInfo = lockAccountInfo;
	}

	public LockAccountInfo getLockAccountInfo() {
		return lockAccountInfo;
	}
}
