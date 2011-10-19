package wisematches.server.web.utils.useragent;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WebClientTest {
	public WebClientTest() {
	}

	@Test
	public void testOpera() {
		assertClient(UserAgent.OPERA, 12, "Opera/9.80 (Windows NT 6.1; U; es-ES) Presto/2.9.181 Version/12.00");
		assertClient(UserAgent.OPERA, 11, "Opera/9.80 (Android 2.3.3; Linux; Opera Mobi/ADR-1107051709; U; ru) Presto/2.8.149 Version/11.10");
		assertClient(UserAgent.OPERA, 9, "Opera/9.99 (Windows NT 5.1; U; pl) Presto/9.9.9");
		assertClient(UserAgent.OPERA, 9, "Mozilla/5.0 (Windows NT 5.1; U; en-GB; rv:1.8.1) Gecko/20061208 Firefox/2.0.0 Opera 9.61");
		assertClient(UserAgent.OPERA, 8, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) Opera 8.60 [en]");
	}

	private void assertClient(UserAgent u, int v, String a) {
		final WebClient detect = WebClient.detect(a);
		Assert.assertEquals(u, detect.getUserAgent());
		Assert.assertEquals(v, detect.getMajorVersion());
	}
}
