package wisematches.playground.propose.impl.memory;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.guest.GuestPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.GameSettings;
import wisematches.playground.MockGameSettings;
import wisematches.playground.propose.ProposalResolution;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.GameProposalListener;
import wisematches.playground.propose.impl.DefaultGameProposal;
import wisematches.playground.propose.impl.memory.MemoryProposalManager;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemoryProposalManagerTest {
	public MemoryProposalManagerTest() {
	}

	@Test
	public void test() {
		final MemoryProposalManager<GameSettings> proposalManager = new MemoryProposalManager<GameSettings>();
		assertEquals(0, proposalManager.loadGameProposals().size());

		final MockGameSettings mockGameSettings = new MockGameSettings("asd", 2);
		final DefaultGameProposal<GameSettings> p1 = new DefaultGameProposal<GameSettings>(1, mockGameSettings, Personality.person(1), new Personality[]{Personality.person(2), Personality.person(12), null});
		proposalManager.storeGameProposal(p1);
		assertEquals(1, proposalManager.loadGameProposals().size());

		final DefaultGameProposal<GameSettings> p2 = new DefaultGameProposal<GameSettings>(2, mockGameSettings, Personality.person(3), new Personality[]{Personality.person(6), Personality.person(12)});
		proposalManager.storeGameProposal(p2);
		assertEquals(2, proposalManager.loadGameProposals().size());

		proposalManager.removeGameProposal(p1);
		assertEquals(1, proposalManager.loadGameProposals().size());

		proposalManager.removeGameProposal(p1);
		assertEquals(1, proposalManager.loadGameProposals().size());

		proposalManager.removeGameProposal(p2);
		assertEquals(0, proposalManager.loadGameProposals().size());
	}
}
