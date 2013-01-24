package wisematches.client.android;

import android.app.Application;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WisematchesApplication extends Application {
	private final HttpClient client;
	private final HttpContext localContext = new BasicHttpContext();
	private final CookieStore cookieStore = new BasicCookieStore();

	private static final int DEFAULT_TIMEOUT = 3000;
	private static final HttpHost HOST = new HttpHost("10.0.2.2", 8080);
//	private static final HttpHost HOST = new HttpHost("www.wisematches.net");

	public WisematchesApplication() {
		final HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.USER_AGENT, "Wisematches/1.0");
		HttpClientParams.setRedirecting(params, false);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);

		client = new DefaultHttpClient(params);
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public HttpResponse execute(HttpRequest request) throws IOException {
		HttpConnectionParams.setSoTimeout(request.getParams(), DEFAULT_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(request.getParams(), DEFAULT_TIMEOUT);
		return client.execute(HOST, request, localContext);
	}
}
