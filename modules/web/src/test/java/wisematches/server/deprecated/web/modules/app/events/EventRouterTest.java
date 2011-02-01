package wisematches.server.deprecated.web.modules.app.events;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class EventRouterTest {
/*
    private EventRouter eventRouter;

    private EventNotificator eventNotificator;

    @Before
    public void init() {
        eventRouter = new EventRouter();
    }

    @Test
    public void manyEventsInQueue() throws InterruptedException {
        final int eventsCount = 500;

        final CountDownLatch firedEvents = new CountDownLatch(eventsCount);
        final CountDownLatch processedEvents = new CountDownLatch(eventsCount);

        final PlayerEventsQueueBean bean = createMock(PlayerEventsQueueBean.class);
        bean.offerEvent(isA(Event.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                processedEvents.countDown();
                firedEvents.await();
                return null;
            }
        });
        bean.offerEvent(isA(Event.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                processedEvents.countDown();
                return null;
            }
        }).times(eventsCount - 1);
        replay(bean);

        final EventProducer producer = createStrictMock(EventProducer.class);
        producer.activateProducer(isA(EventNotificator.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                eventNotificator = (EventNotificator) getCurrentArguments()[0];
                return null;
            }
        });
        replay(producer);

        final PlayerSessionsManager playerSessionsManager = createMock(PlayerSessionsManager.class);
        expect(playerSessionsManager.getPlayerSessionBeans()).andReturn(Arrays.<PlayerSessionBean>asList(bean)).times(eventsCount);
        replay(playerSessionsManager);

        eventRouter.setEventEnginesCount(1);
        eventRouter.setEventProducers(Arrays.asList(producer));
        eventRouter.setPlayerSessionsManager(playerSessionsManager);

        for (int i = 0; i < eventsCount; i++) {
            eventNotificator.fireEvent(createNiceMock("Event" + i, Event.class));
            firedEvents.countDown();
        }
        processedEvents.await();

        verify(producer, playerSessionsManager, bean);
    }

    @Test
    public void checkMultipleEngines() throws InterruptedException {
        final Set<String> eventsSenderThreads = new HashSet<String>();

        final PlayerEventsQueueBean bean = createMock(PlayerEventsQueueBean.class);
        bean.offerEvent(isA(Event.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                eventsSenderThreads.add(Thread.currentThread().getName());
                Thread.sleep(200);
                return null;
            }
        }).times(3);
        makeThreadSafe(bean, true);
        replay(bean);

        final EventProducer producer = createStrictMock(EventProducer.class);
        producer.activateProducer(isA(EventNotificator.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                eventNotificator = (EventNotificator) getCurrentArguments()[0];
                return null;
            }
        });
        replay(producer);

        final PlayerSessionsManager playerSessionsManager = createMock(PlayerSessionsManager.class);
        expect(playerSessionsManager.getPlayerSessionBeans()).andReturn(Arrays.<PlayerSessionBean>asList(bean)).times(3);
        makeThreadSafe(playerSessionsManager, true);
        replay(playerSessionsManager);

        eventRouter.setEventEnginesCount(3);
        eventRouter.setEventProducers(Arrays.asList(producer));
        eventRouter.setPlayerSessionsManager(playerSessionsManager);

        eventNotificator.fireEvent(createNiceMock("Event1", Event.class));
        eventNotificator.fireEvent(createNiceMock("Event2", Event.class));
        eventNotificator.fireEvent(createNiceMock("Event3", Event.class));

        Thread.sleep(700);

        assertEquals(3, eventsSenderThreads.size());
        assertTrue(eventsSenderThreads.contains("EventRouterEngine#0"));
        assertTrue(eventsSenderThreads.contains("EventRouterEngine#1"));
        assertTrue(eventsSenderThreads.contains("EventRouterEngine#2"));

        verify(producer, playerSessionsManager, bean);
    }
*/
}
