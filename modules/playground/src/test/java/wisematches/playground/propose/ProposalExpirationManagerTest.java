package wisematches.playground.propose;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import wisematches.core.expiration.ExpirationListener;
import wisematches.playground.MockGameSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProposalExpirationManagerTest {
	private ProposalExpirationManager<MockGameSettings> expirationManager;

	private static final int MILLIS_IN_DAY = 86400000;//24 * 60 * 60 * 1000;

	public ProposalExpirationManagerTest() {
	}

	@Before
	public void setUp() {
		final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.afterPropertiesSet();

		expirationManager = new ProposalExpirationManager<MockGameSettings>();
		expirationManager.setTaskScheduler(taskScheduler);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testTerminatorInitialization() throws Exception {
		final long time = System.currentTimeMillis();

		final GameProposalManager<MockGameSettings> proposalManager = createMock(GameProposalManager.class);

		final GameProposal<MockGameSettings> proposal1 = createMock(GameProposal.class);
		expect(proposal1.getId()).andReturn(1L).anyTimes();
		expect(proposal1.getProposalType()).andReturn(ProposalType.CHALLENGE);
		expect(proposal1.getCreationDate()).andReturn(new Date(time - 4 * MILLIS_IN_DAY + 200));
		replay(proposal1);

		final GameProposal<MockGameSettings> proposal2 = createMock(GameProposal.class);
		expect(proposal2.getId()).andReturn(2L).anyTimes();
		expect(proposal2.getProposalType()).andReturn(ProposalType.CHALLENGE);
		expect(proposal2.getCreationDate()).andReturn(new Date(time - 6 * MILLIS_IN_DAY + 200));
		replay(proposal2);

		final GameProposal<MockGameSettings> proposal3 = createMock(GameProposal.class);
		expect(proposal3.getId()).andReturn(3L).anyTimes();
		expect(proposal3.getProposalType()).andReturn(ProposalType.CHALLENGE);
		expect(proposal3.getCreationDate()).andReturn(new Date(time - 7 * MILLIS_IN_DAY + 200)); // expired
		replay(proposal3);

		proposalManager.addGameProposalListener(isA(GameProposalListener.class));
		expect(proposalManager.searchEntities(null, ProposalRelation.AVAILABLE, null, null, null)).andReturn(Arrays.asList(proposal1, proposal2, proposal3));
		expect(proposalManager.terminate(3L)).andReturn(proposal3);
		replay(proposalManager);

		final ExpirationListener<Long, ProposalExpirationType> l = createMock(ExpirationListener.class);
		l.expirationTriggered(1L, ProposalExpirationType.THREE_DAYS);
		l.expirationTriggered(2L, ProposalExpirationType.ONE_DAY);
		replay(l);

		expirationManager.addExpirationListener(l);
		expirationManager.setProposalManager(proposalManager);
		expirationManager.setChallengeExpirationMillis(7 * MILLIS_IN_DAY);
		expirationManager.afterPropertiesSet();

		Thread.sleep(500);

		verify(l, proposal1, proposalManager);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testListeners() throws Exception {
		final long time = System.currentTimeMillis();

		final Capture<GameProposalListener> proposalListenerCapture = new Capture<GameProposalListener>();

		final GameProposalManager<MockGameSettings> proposalManager = createMock(GameProposalManager.class);

		proposalManager.addGameProposalListener(capture(proposalListenerCapture));
		expect(proposalManager.searchEntities(null, ProposalRelation.AVAILABLE, null, null, null)).andReturn(Collections.<GameProposal<MockGameSettings>>emptyList());
		replay(proposalManager);

		final ExpirationListener<Long, ProposalExpirationType> l = createMock(ExpirationListener.class);
		replay(l);

		final GameProposal<MockGameSettings> proposal1 = createMock(GameProposal.class);
		expect(proposal1.getId()).andReturn(1L).anyTimes();
		expect(proposal1.getProposalType()).andReturn(ProposalType.INTENTION).anyTimes();
		expect(proposal1.getCreationDate()).andReturn(new Date(time - 4 * MILLIS_IN_DAY + 100));
		replay(proposal1);

		expirationManager.addExpirationListener(l);
		expirationManager.setProposalManager(proposalManager);
		expirationManager.setIntentionExpirationMillis(7 * MILLIS_IN_DAY);
		expirationManager.afterPropertiesSet();

		proposalListenerCapture.getValue().gameProposalInitiated(proposal1);
		proposalListenerCapture.getValue().gameProposalUpdated(proposal1, null, ProposalDirective.ACCEPTED);
		proposalListenerCapture.getValue().gameProposalFinalized(proposal1, null, ProposalResolution.READY);

		Thread.sleep(500);

		verify(l, proposalManager, proposal1);
	}
}
