package wisematches.playground.scribble;

import wisematches.core.PersonalityManager;
import wisematches.playground.AbstractBoardDescription;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "scribble_board")
public class ScribbleDescription extends AbstractBoardDescription<ScribbleSettings, ScribblePlayerHand> {
	public ScribbleDescription() {
	}

	@Override
	protected void initializePlayers(PersonalityManager playerManager) {
		super.initializePlayers(playerManager);
	}
}
