package wisematches.playground;

import wisematches.core.Language;
import wisematches.core.Membership;
import wisematches.core.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MockPlayer extends Player {
	private Membership membership = Membership.BASIC;

	public MockPlayer(long id) {
		super(id);
	}

	public MockPlayer(long id, Membership membership) {
		super(id);
		this.membership = membership;
	}

	@Override
	public String getNickname() {
		return "mock";
	}

	@Override
	public Language getLanguage() {
		return Language.DEFAULT;
	}

	@Override
	public Membership getMembership() {
		return membership;
	}
}
