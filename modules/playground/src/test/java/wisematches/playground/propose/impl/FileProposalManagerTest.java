package wisematches.playground.propose.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.GameSettings;
import wisematches.playground.MockGameSettings;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ViolatedRestrictionException;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileProposalManagerTest {
	private File file;
	private FileProposalManager<GameSettings> fileProposalManager;

	public FileProposalManagerTest() {
	}

	@Before
	public void setUp() throws IOException {
		file = File.createTempFile("proposal-", "-unit-test");
		if (file.exists()) {
			file.delete();
		}

		fileProposalManager = new FileProposalManager<GameSettings>();
		fileProposalManager.setProposalsResource(file);
	}

	@After
	public void cleanUp() {
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	public void storeGameProposal() throws ViolatedRestrictionException {
		System.out.println(file.getAbsolutePath());
		final GameSettings settings = new MockGameSettings("Mock", 3);

		long currentSize = file.length();
		fileProposalManager.initiateWaitingProposal(settings, RobotPlayer.DULL, 3, null);
		assertTrue(currentSize < (currentSize = file.length()));

		fileProposalManager.initiateWaitingProposal(settings, RobotPlayer.DULL, 3, null);
		assertTrue(currentSize < (currentSize = file.length()));
	}

	@Test
	public void loadGameProposals() throws Exception {
		System.out.println(file.getAbsolutePath());
		final GameSettings settings = new MockGameSettings("Mock", 3);

		final GameProposal<GameSettings> p1 = fileProposalManager.initiateWaitingProposal(settings, RobotPlayer.DULL, 3, null);
		final GameProposal<GameSettings> p2 = fileProposalManager.initiateWaitingProposal(settings, RobotPlayer.DULL, 3, null);
		fileProposalManager.close();

		fileProposalManager = new FileProposalManager<GameSettings>();
		fileProposalManager.setProposalsResource(file);
		fileProposalManager.afterPropertiesSet();
		assertEquals(2, fileProposalManager.getActiveProposals().size());

		Object[] objects = fileProposalManager.getActiveProposals().toArray();
		@SuppressWarnings("unchecked")
		GameProposal<GameSettings> pl1 = (GameProposal<GameSettings>) objects[0];
		@SuppressWarnings("unchecked")
		GameProposal<GameSettings> pl2 = (GameProposal<GameSettings>) objects[0];

		assertTrue(pl1.getId() != 0);
		assertTrue(pl2.getId() != 0);

		final GameProposal<GameSettings> p3 = fileProposalManager.initiateWaitingProposal(settings, RobotPlayer.DULL, 3, null);
		assertTrue(p3.getId() > pl1.getId());
		assertTrue(p3.getId() > pl2.getId());
	}
}
