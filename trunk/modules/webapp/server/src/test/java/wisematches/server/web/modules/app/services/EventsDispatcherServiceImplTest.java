package wisematches.server.web.modules.app.services;

import static org.easymock.EasyMock.*;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import wisematches.server.web.modules.app.sessions.PlayerEventsQueueBean;
import wisematches.server.web.rpc.RemoteServiceContextAccessor;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class EventsDispatcherServiceImplTest {
/*
    @Test
    public void getEvents() throws SerializationException {
        Event e1 = createNiceMock(Event.class);
        Event e2 = createNiceMock(Event.class);

        final EventsDispatcherServiceImpl impl = new EventsDispatcherServiceImpl();

        final PlayerEventsQueueBean bean = RemoteServiceContextAccessor.expectPlayerSessionBean(PlayerEventsQueueBean.class);
        expect(bean.poolEvents()).andReturn(new LinkedList<Event>(Arrays.asList(e1, e2)));
        replay(bean);

        assertEquals(Arrays.asList(e1, e2), impl.getEvents());
        verify(bean);
    }

    @After
    public void thearDown() {
        RemoteServiceContextAccessor.destroy();
    }
*/
}
