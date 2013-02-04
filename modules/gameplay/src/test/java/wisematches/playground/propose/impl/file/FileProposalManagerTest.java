package wisematches.playground.propose.impl.file;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wisematches.core.Player;
import wisematches.core.personality.DefaultPlayer;
import wisematches.playground.GameSettings;
import wisematches.playground.MockGameSettings;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ProposalRelation;

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

	private static final Player P1 = new DefaultPlayer(901, null, null, null, null, null);
	private static final Player P2 = new DefaultPlayer(902, null, null, null, null, null);
	private static final Player P3 = new DefaultPlayer(903, null, null, null, null, null);

	public FileProposalManagerTest() {
	}

	@Before
	public void setUp() throws IOException {
		file = File.createTempFile("proposal-", "-unit-test");
		if (file.exists()) {
			file.delete();
		}

		fileProposalManager = new FileProposalManager<>();
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
		fileProposalManager.initiate(settings, P1, 3);
		assertTrue(currentSize < (currentSize = file.length()));

		fileProposalManager.initiate(settings, "mock", P1, Arrays.asList(P2, P3));
		assertTrue(currentSize < (currentSize = file.length()));
	}

	@Test
	public void loadGameProposals() throws Exception {
		System.out.println(file.getAbsolutePath());
		final GameSettings settings = new MockGameSettings("Mock", 3);

		fileProposalManager.initiate(settings, P1, 3);
		fileProposalManager.initiate(settings, "mock", P1, Arrays.asList(P2, P3));
		fileProposalManager.close();

		fileProposalManager = new FileProposalManager<>();
		fileProposalManager.setProposalsResource(file);
		fileProposalManager.afterPropertiesSet();
		assertEquals(2, fileProposalManager.searchEntities(null, ProposalRelation.AVAILABLE, null, null).size());

		Object[] objects = fileProposalManager.searchEntities(null, ProposalRelation.AVAILABLE, null, null).toArray();
		@SuppressWarnings("unchecked")
		GameProposal<GameSettings> pl1 = (GameProposal<GameSettings>) objects[0];
		@SuppressWarnings("unchecked")
		GameProposal<GameSettings> pl2 = (GameProposal<GameSettings>) objects[0];

		assertTrue(pl1.getId() != 0);
		assertTrue(pl2.getId() != 0);

		final GameProposal<GameSettings> p3 = fileProposalManager.initiate(settings, P1, 3);
		assertTrue(p3.getId() > pl1.getId());
		assertTrue(p3.getId() > pl2.getId());
	}
}
