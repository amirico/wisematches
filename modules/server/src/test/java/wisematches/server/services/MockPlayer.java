package wisematches.server.services;

import wisematches.core.Language;
import wisematches.core.Player;
import wisematches.core.PlayerType;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MockPlayer extends Player {
	public MockPlayer(long id) {
		super(id);
	}

	@Override
	public String getNickname() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Language getLanguage() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public PlayerType getPlayerType() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}
}
