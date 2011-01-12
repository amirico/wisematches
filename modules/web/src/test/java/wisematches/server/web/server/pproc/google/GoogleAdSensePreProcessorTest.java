/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.server.pproc.google;

import org.junit.Test;
import wisematches.server.player.Language;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GoogleAdSensePreProcessorTest {
	@Test
	public void test_GenerateString() {
		GoogleAdSensePreProcessor processor = new GoogleAdSensePreProcessor();
		processor.setClientId("myclient-123455");
		processor.setDashboardSlot("dashboard-1234");
		processor.setGameboardSlot("gameboard-1234");
		processor.setPlayboardSlot("playboard-1234");
		processor.setSearchId("search:123");

		final HttpServletRequest request = createNiceMock(HttpServletRequest.class);
		expect(request.getAttribute(Language.class.getSimpleName())).andReturn(Language.RUSSIAN);
		replay(request);

		final HttpServletResponse response = createNiceMock(HttpServletResponse.class);
		final StringWriter w = new StringWriter();
		processor.processPageContent(new PrintWriter(w), request, response);

		System.out.println(w.getBuffer().toString());
	}
}
