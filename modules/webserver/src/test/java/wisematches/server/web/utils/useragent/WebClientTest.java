package wisematches.server.web.utils.useragent;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNull;

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

	@Test
	public void testJava() {
		assertClient(UserAgent.JAVA, 5, "Java/1.5.0_22");
	}

	@Test
	public void testGoogleAdSense() {
		assertClient(UserAgent.GOOGLEADSENSE, 0, "Mediapartners-Google");
		assertClient(UserAgent.GOOGLEADSENSE, 2, "Mediapartners-Google/2.1");
	}

	@Test
	public void testRobots() {
		assertNull(WebClient.detect("Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)"));
		assertNull(WebClient.detect("Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)"));
		assertNull(WebClient.detect("Mozilla/5.0 (compatible; news bot /2.1)"));
	}

	private void assertClient(UserAgent u, int v, String a) {
		final WebClient detect = WebClient.detect(a);
		Assert.assertEquals(u, detect.getUserAgent());
		Assert.assertEquals(v, detect.getMajorVersion());
	}
}
