package wisematches.server.web.view;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import wisematches.server.web.utils.useragent.UserAgent;
import wisematches.server.web.utils.useragent.WebClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BrowserDetectionInterceptor extends HandlerInterceptorAdapter {
	private Map<UserAgent, Integer> supportedBrowsers = new HashMap<UserAgent, Integer>();

	public BrowserDetectionInterceptor() {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			final HttpSession session = request.getSession();

			Object supportedBrowser = null;
			if (session != null) {
				supportedBrowser = session.getAttribute("IS_BROWSED_SUPPORTED");
			}
			if (supportedBrowser == null) {
				supportedBrowser = checkBrowserVersion(WebClient.detect(request));
			}
			if (session != null) {
				session.setAttribute("IS_BROWSED_SUPPORTED", supportedBrowser);
			}
			modelAndView.addObject("supportedBrowser", supportedBrowser);
		}
	}

	private Boolean checkBrowserVersion(WebClient detect) {
		if (detect == null) {
			return Boolean.FALSE;
		}
		Integer integer = supportedBrowsers.get(detect.getUserAgent());
		if (integer == null) {
			return Boolean.FALSE;
		}
		return detect.getMajorVersion() >= integer;
	}

	public void setSupportedBrowsers(Map<UserAgent, Integer> supportedBrowsers) {
		this.supportedBrowsers = supportedBrowsers;
	}
}
