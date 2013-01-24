package wisematches.playground.propose.impl.file;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wisematches.core.personality.machinery.RobotPlayer;
import wisematches.playground.GameSettings;
import wisematches.playground.MockGameSettings;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ProposalRelation;

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
	public void storeGameProposal() throws Exception {
		System.out.println(file.getAbsolutePath());
		final GameSettings settings = new MockGameSettings("Mock", 3);

		long currentSize = file.length();
		fileProposalManager.initiate(settings, RobotPlayer.DULL, 3);
		assertTrue(currentSize < (currentSize = file.length()));

		fileProposalManager.initiate(settings, RobotPlayer.DULL, 3);
		assertTrue(currentSize < (currentSize = file.length()));
	}

	@Test
	public void loadGameProposals() throws Exception {
		System.out.println(file.getAbsolutePath());
		final GameSettings settings = new MockGameSettings("Mock", 3);

		final GameProposal<GameSettings> p1 = fileProposalManager.initiate(settings, RobotPlayer.DULL, 3);
		final GameProposal<GameSettings> p2 = fileProposalManager.initiate(settings, RobotPlayer.DULL, 3);
		fileProposalManager.close();

		fileProposalManager = new FileProposalManager<GameSettings>();
		fileProposalManager.setProposalsResource(file);
		fileProposalManager.afterPropertiesSet();
		assertEquals(2, fileProposalManager.searchEntities(null, ProposalRelation.AVAILABLE, null, null, null).size());

		Object[] objects = fileProposalManager.searchEntities(null, ProposalRelation.AVAILABLE, null, null, null).toArray();
		@SuppressWarnings("unchecked")
		GameProposal<GameSettings> pl1 = (GameProposal<GameSettings>) objects[0];
		@SuppressWarnings("unchecked")
		GameProposal<GameSettings> pl2 = (GameProposal<GameSettings>) objects[0];

		assertTrue(pl1.getId() != 0);
		assertTrue(pl2.getId() != 0);

		final GameProposal<GameSettings> p3 = fileProposalManager.initiate(settings, RobotPlayer.DULL, 3);
		assertTrue(p3.getId() > pl1.getId());
		assertTrue(p3.getId() > pl2.getId());
	}
}
