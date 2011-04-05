package wisematches.server.personality.player;

import org.junit.Before;
import org.junit.Test;
import wisematches.server.personality.account.*;
import wisematches.server.personality.player.computer.ComputerPlayer;
import wisematches.server.personality.player.computer.guest.GuestPlayer;
import wisematches.server.personality.player.computer.robot.RobotPlayer;
import wisematches.server.personality.player.member.MemberPlayer;
import wisematches.server.standing.rating.PlayerRatingManager;
import wisematches.server.standing.rating.RatingChange;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.PlayerStatisticManager;

import java.util.ArrayList;
import java.util.Collection;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class StandingPlayerManagerTest {
	private AccountManager accountManager;
	private PlayerRatingManager ratingManager;
	private PlayerStatisticManager statisticsManager;
	private StandingPlayerManager standingPlayerManager;

	public StandingPlayerManagerTest() {
	}

	@Before
	public void setUp() {
		ratingManager = createStrictMock(PlayerRatingManager.class);
		accountManager = createStrictMock(AccountManager.class);
		statisticsManager = createStrictMock(PlayerStatisticManager.class);

		standingPlayerManager = new StandingPlayerManager();
		standingPlayerManager.setRatingManager(ratingManager);
		standingPlayerManager.setAccountManager(accountManager);
		standingPlayerManager.setStatisticsManager(statisticsManager);
	}

	@Test
	public void testComputerPlayers() {
		assertSame(RobotPlayer.DULL, standingPlayerManager.getPlayer(RobotPlayer.DULL.getId()));
		assertSame(RobotPlayer.EXPERT, standingPlayerManager.getPlayer(RobotPlayer.EXPERT.getId()));
		assertSame(RobotPlayer.TRAINEE, standingPlayerManager.getPlayer(RobotPlayer.TRAINEE.getId()));
		assertSame(GuestPlayer.GUEST, standingPlayerManager.getPlayer(GuestPlayer.GUEST.getId()));
	}

	@Test
	public void testMemberPlayers() {
		final Account a = new AccountEditor("a", "dsf", "wer").setLanguage(Language.RU).setMembership(Membership.PLATINUM).createAccount();

		expect(accountManager.getAccount(a.getId())).andReturn(a).times(2);
		replay(accountManager);

		final MemberPlayer player = (MemberPlayer) standingPlayerManager.getPlayer(a);
		assertSame(a, player.getAccount());
		assertEquals("dsf", player.getNickname());
		assertEquals(Language.RU, player.getLanguage());
		assertEquals(Membership.PLATINUM, player.getMembership());

		// must be in cache
		final MemberPlayer player2 = (MemberPlayer) standingPlayerManager.getPlayer(a);
		assertSame(a, player2.getAccount());
		assertEquals("dsf", player2.getNickname());
		assertEquals(Language.RU, player2.getLanguage());
		assertEquals(Membership.PLATINUM, player2.getMembership());

		verify(accountManager);
	}

	@Test
	public void testRatingsDelegation() {
		final Account a = new AccountEditor("a", "dsf", "wer").createAccount();

		expect(accountManager.getAccount(a.getId())).andReturn(a);
		replay(accountManager);

		final Collection<RatingChange> changes = new ArrayList<RatingChange>();

		expect(ratingManager.getRating(a)).andReturn((short) 123);
		expect(ratingManager.getPosition(a)).andReturn(321L);
		expect(ratingManager.getRatingChanges(a)).andReturn(changes);
		replay(ratingManager);

		// robot player
		final ComputerPlayer rp = (ComputerPlayer) standingPlayerManager.getPlayer(RobotPlayer.DULL);
		assertEquals(RobotPlayer.DULL.getRating(), rp.getRating());
		assertEquals(0, rp.getPosition());
		assertNull(rp.getRatingChanges());

		// real player
		final MemberPlayer mp = (MemberPlayer) standingPlayerManager.getPlayer(a);
		assertEquals(123, mp.getRating());
		assertEquals(321, mp.getPosition());
		assertSame(changes, mp.getRatingChanges());

		verify(accountManager);
	}

	@Test
	public void testStatisticDelegation() {
		final Account a = new AccountEditor("a", "dsf", "wer").createAccount();

		expect(accountManager.getAccount(a.getId())).andReturn(a);
		replay(accountManager);

		final PlayerStatistic ps = createMock(PlayerStatistic.class);

		expect(statisticsManager.getPlayerStatistic(a)).andReturn(ps);
		replay(statisticsManager);

		// robot player
		final ComputerPlayer rp = (ComputerPlayer) standingPlayerManager.getPlayer(RobotPlayer.DULL);
		assertNull(rp.getPlayerStatistic());

		// real player
		final MemberPlayer mp = (MemberPlayer) standingPlayerManager.getPlayer(a);
		assertSame(ps, mp.getPlayerStatistic());

		verify(statisticsManager);
	}
}
