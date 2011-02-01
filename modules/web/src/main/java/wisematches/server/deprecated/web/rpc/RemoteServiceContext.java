package wisematches.server.deprecated.web.rpc;

import wisematches.server.utils.sessions.PlayerSessionBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class RemoteServiceContext {
	private final ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	private final ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();
	private final ThreadLocal<PlayerSessionBean> sessionBean = new ThreadLocal<PlayerSessionBean>();

	void initSimpleSession(HttpServletRequest request, HttpServletResponse response) {
		this.request.set(request);
		this.response.set(response);
	}

	void initSecureSession(HttpServletRequest request, HttpServletResponse response, PlayerSessionBean sessionBean) {
		initSimpleSession(request, response);
		this.sessionBean.set(sessionBean);
	}

	void destroySession() {
		this.request.set(null);
		this.response.set(null);
		this.sessionBean.set(null);
	}

	HttpServletRequest getRequest() {
		return request.get();
	}

	HttpServletResponse getResponse() {
		return response.get();
	}

	PlayerSessionBean getSessionBean() {
		return sessionBean.get();
	}
}
