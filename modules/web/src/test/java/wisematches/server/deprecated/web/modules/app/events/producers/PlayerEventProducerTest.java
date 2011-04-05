package wisematches.server.deprecated.web.modules.app.events.producers;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerEventProducerTest {
/*    private AccountLockListener lockAccountListener;
    private PlayerStatisticListener playerStatisticListener;

    @Test
    public void test_convertPlayer() {
        final Player player = createMockPlayer();

        final PlayerManager manager = createStrictMock(PlayerManager.class);
        expect(manager.getPlayer(13L)).andReturn(player);
        replay(manager);

        final PlayerInfoBean bean = PlayerEventProducer.convertPlayer(13L, manager);
        assertEquals(13L, bean.getPlayerId());
        assertEquals("test", bean.getPlayerName());
        assertEquals(MemberType.GUEST, bean.getMemberType());
        assertEquals(123, bean.getCurrentRating());

        verify(player, manager);
    }

    @Test
    public void test_PlayerStatisticEvent() {
        final PlayerStatisticManager manager = createStrictMock(PlayerStatisticManager.class);
        manager.addPlayerStatisticListener(isA(PlayerStatisticListener.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                playerStatisticListener = (PlayerStatisticListener) getCurrentArguments()[0];
                return null;
            }
        });
        replay(manager);

        final PlayerEventProducer producer = new PlayerEventProducer();
        producer.setPlayerStatisticManager(manager);

        // Nothing to do
        playerStatisticListener.playerStatisticUpdated(12L, new HibernatePlayerStatistic(12L));

        final EventNotificator notificator = createStrictMock(EventNotificator.class);
        notificator.fireEvent(cmp(new PlayerStatisticEvent(12L), new Comparator<PlayerStatisticEvent>() {
            public int compare(PlayerStatisticEvent o1, PlayerStatisticEvent o2) {
                return (int) (o1.getPlayerId() - o2.getPlayerId());
            }
        }, LogicalOperator.EQUAL));
        replay(notificator);

        producer.activateProducer(notificator);
        playerStatisticListener.playerStatisticUpdated(12L, new HibernatePlayerStatistic(12L));

        producer.deactivateProducer();
        playerStatisticListener.playerStatisticUpdated(12L, new HibernatePlayerStatistic(12L));

        verify(manager, notificator);
    }

    @Test
    public void test_lockAccount() {
        final Date date = new Date(System.currentTimeMillis() + 10000000000L);

        final AccountLockManager manager = createStrictMock(AccountLockManager.class);
        manager.addAccountLockListener(isA(AccountLockListener.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                lockAccountListener = (AccountLockListener) getCurrentArguments()[0];
                return null;
            }
        });
        replay(manager);

        final EventNotificator notificator = createStrictMock(EventNotificator.class);
        notificator.fireEvent(isA(PlayerLockedEvent.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                final PlayerLockedEvent e = (PlayerLockedEvent) getCurrentArguments()[0];
                assertEquals(13L, e.getPlayerId());
                assertEquals("public lock", e.getReasone());
                assertEquals(date.getTime(), e.getUnlockTime());
                return null;
            }
        });

        notificator.fireEvent(isA(PlayerUnlockedEvent.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                final PlayerUnlockedEvent e = (PlayerUnlockedEvent) getCurrentArguments()[0];
                assertEquals(13L, e.getPlayerId());
                return null;
            }
        });
        replay(notificator);

        final PlayerEventProducer producer = new PlayerEventProducer();
        producer.setAccountLockManager(manager);

        // nothing to do
        lockAccountListener.accountLocked(createMockPlayer(), "public lock", "private lock", date);
        lockAccountListener.accountUnlocked(createMockPlayer());

        // Events should be fired
        producer.activateProducer(notificator);
        lockAccountListener.accountLocked(createMockPlayer(), "public lock", "private lock", date);
        lockAccountListener.accountUnlocked(createMockPlayer());

        // nothing to do
        producer.deactivateProducer();
        lockAccountListener.accountLocked(createMockPlayer(), "public lock", "private lock", date);
        lockAccountListener.accountUnlocked(createMockPlayer());

        verify(manager);
    }

    private Player createMockPlayer() {
        final Player player = createMock(Player.class);
        expect(player.getId()).andReturn(13L).anyTimes();
        expect(player.getNickname()).andReturn("test").anyTimes();
        expect(player.getRating()).andReturn(123).anyTimes();
        replay(player);
        return player;
    }*/
}