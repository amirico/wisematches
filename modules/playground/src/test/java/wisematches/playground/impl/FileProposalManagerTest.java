package wisematches.playground.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wisematches.playground.board.MockGameSettings;
import wisematches.server.playground.board.GameSettings;
import wisematches.server.playground.propose.GameProposal;
import wisematches.server.playground.propose.ViolatedRestrictionException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
		fileProposalManager.initiateGameProposal(settings, 3, null, Arrays.asList(DefaultGameProposalTest.createPlayer(2)));
		assertTrue(currentSize < (currentSize = file.length()));

		fileProposalManager.initiateGameProposal(settings, 3, null, Arrays.asList(DefaultGameProposalTest.createPlayer(2), DefaultGameProposalTest.createPlayer(3)));
		assertTrue(currentSize < (currentSize = file.length()));
	}

	@Test
	public void loadGameProposals() throws Exception {
		System.out.println(file.getAbsolutePath());
		final GameSettings settings = new MockGameSettings("Mock", 3);

		final GameProposal<GameSettings> p1 = fileProposalManager.initiateGameProposal(settings, 3, null, Arrays.asList(DefaultGameProposalTest.createPlayer(2)));
		final GameProposal<GameSettings> p2 = fileProposalManager.initiateGameProposal(settings, 3, null, Arrays.asList(DefaultGameProposalTest.createPlayer(2), DefaultGameProposalTest.createPlayer(3)));
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

		final GameProposal<GameSettings> p3 = fileProposalManager.initiateGameProposal(settings, 3, null, Arrays.asList(DefaultGameProposalTest.createPlayer(2)));
		assertTrue(p3.getId() > pl1.getId());
		assertTrue(p3.getId() > pl2.getId());
	}
}
