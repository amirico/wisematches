package wisematches.playground.propose.impl.memory;

import org.junit.Test;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.playground.GameSettings;
import wisematches.playground.MockGameSettings;
import wisematches.playground.propose.impl.DefaultPrivateProposal;
import wisematches.playground.propose.impl.DefaultPublicProposal;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemoryProposalManagerTest {
	private static final Player P1 = new DefaultMember(901, null, null, null, null, null);
	private static final Player P2 = new DefaultMember(902, null, null, null, null, null);
	private static final Player P3 = new DefaultMember(903, null, null, null, null, null);

	public MemoryProposalManagerTest() {
	}

	@Test
	public void test() {
		final MemoryProposalManager<GameSettings> proposalManager = new MemoryProposalManager<>();
		assertEquals(0, proposalManager.loadGameProposals().size());

		final MockGameSettings mockGameSettings = new MockGameSettings("asd", 2);
		final DefaultPrivateProposal<GameSettings> p1 = new DefaultPrivateProposal<GameSettings>(1, "mock", mockGameSettings, P1, Arrays.asList(P2, P3));
		proposalManager.storeGameProposal(p1);
		assertEquals(1, proposalManager.loadGameProposals().size());

		final DefaultPublicProposal<GameSettings> p2 = new DefaultPublicProposal<GameSettings>(2, mockGameSettings, P1, 2, null);
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
