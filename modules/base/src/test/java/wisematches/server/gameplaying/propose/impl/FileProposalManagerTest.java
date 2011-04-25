package wisematches.server.gameplaying.propose.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wisematches.server.gameplaying.board.GameSettings;
import wisematches.server.gameplaying.board.MockGameSettings;
import wisematches.server.gameplaying.propose.GameProposal;
import wisematches.server.personality.Personality;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
	public void storeGameProposal() {
		System.out.println(file.getAbsolutePath());
		final GameSettings settings = new MockGameSettings("Mock", 3);

		long currentSize = file.length();
		fileProposalManager.initiateGameProposal(settings, 3, Arrays.asList(Personality.person(2)));
		assertTrue(currentSize < (currentSize = file.length()));

		fileProposalManager.initiateGameProposal(settings, 3, Arrays.asList(Personality.person(2), Personality.person(3)));
		assertTrue(currentSize < (currentSize = file.length()));
	}

	@Test
	public void loadGameProposals() throws Exception {
		System.out.println(file.getAbsolutePath());
		final GameSettings settings = new MockGameSettings("Mock", 3);

		final GameProposal<GameSettings> p1 = fileProposalManager.initiateGameProposal(settings, 3, Arrays.asList(Personality.person(2)));
		final GameProposal<GameSettings> p2 = fileProposalManager.initiateGameProposal(settings, 3, Arrays.asList(Personality.person(2), Personality.person(3)));
		fileProposalManager.close();

		fileProposalManager = new FileProposalManager<GameSettings>();
		fileProposalManager.setProposalsResource(file);
		fileProposalManager.afterPropertiesSet();
		assertEquals(2, fileProposalManager.getActiveProposals().size());
	}
}
