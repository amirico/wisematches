package wisematches.client.gwt.app.client.events;

import org.junit.Test;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class EventsDispatcherTest {
	@Test
	public void emptyTest() {
	}
/*
    private final EventsDispatcher dispatcher = new EventsDispatcher();

    @Test
    public void test_fireEventsByListener() {
        final Event e1 = createNiceMock("E1", Event.class);
        final Event e2 = createNiceMock("E2", Event.class);
        final Event e3 = createNiceMock("E3", Event.class);
        final Event e4 = createNiceMock("E4", Event.class);

        final Correlator c1 = createStrictMock(Correlator.class);
        expect(c1.isEventSupported(e1)).andReturn(false);
        expect(c1.isEventSupported(e2)).andReturn(true);
        expect(c1.isEventSupported(e3)).andReturn(true);
        expect(c1.eventsColleration(e3, e2)).andReturn(Correlation.NEUTRAL);
        expect(c1.isEventSupported(e4)).andReturn(false);
        replay(c1);

        final EventsListener el1 = createStrictMock(EventsListener.class);
        el1.eventsReceived(eq(Arrays.asList(e2, e3)));
        replay(el1);

        dispatcher.addEventsListener(c1, el1);

        final Correlator c2 = createStrictMock(Correlator.class);
        expect(c2.isEventSupported(e1)).andReturn(true);
        expect(c2.isEventSupported(e2)).andReturn(true);
        expect(c2.eventsColleration(e2, e1)).andReturn(Correlation.NEUTRAL);
        expect(c2.isEventSupported(e3)).andReturn(true);
        expect(c2.eventsColleration(e3, e1)).andReturn(Correlation.NEUTRAL);
        expect(c2.eventsColleration(e3, e2)).andReturn(Correlation.EXCLUSION);
        expect(c2.isEventSupported(e4)).andReturn(true);
        expect(c2.eventsColleration(e4, e1)).andReturn(Correlation.NEUTRAL);
        expect(c2.eventsColleration(e4, e3)).andReturn(Correlation.MUTUAL_EXCLUSION);
        replay(c2);

        final EventsListener el2 = createStrictMock(EventsListener.class);
        el2.eventsReceived(eq(Arrays.asList(e1)));
        replay(el2);

        dispatcher.addEventsListener(c2, el2);
        dispatcher.fireEventsByListener(Arrays.asList(e1, e2, e3, e4));

        verify(el1, el2);
    }
*/
}
