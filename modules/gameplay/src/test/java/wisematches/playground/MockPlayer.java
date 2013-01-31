package wisematches.playground;

import wisematches.core.Language;
import wisematches.core.Player;
import wisematches.core.PlayerType;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MockPlayer extends Player {
	private PlayerType membership = PlayerType.GUEST;

	public MockPlayer(long id) {
		super(id);
	}

	public MockPlayer(Long id, PlayerType membership) {
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
	public PlayerType getPlayerType() {
		return membership;
	}
}
