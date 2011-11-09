package wisematches.client.android;

import android.app.Application;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WisematchesAndroid extends Application {
	private final HttpContext localContext = new BasicHttpContext();
	private final CookieStore cookieStore = new BasicCookieStore();
	private final DefaultHttpClient client = new DefaultHttpClient();

	private static final HttpHost HOST = new HttpHost("10.0.2.2", 8080);

	public WisematchesAndroid() {
	}

	@Override
	public void onCreate() {
		super.onCreate();

		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		final HttpParams httpParameters = client.getParams();
		httpParameters.setParameter(CoreProtocolPNames.USER_AGENT, "Wisematches/1.0");
		HttpClientParams.setRedirecting(httpParameters, false);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public HttpResponse execute(HttpRequest request) throws IOException {
		return client.execute(HOST, request, localContext);
	}
}
