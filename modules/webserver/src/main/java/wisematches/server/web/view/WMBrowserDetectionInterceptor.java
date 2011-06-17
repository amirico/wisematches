package wisematches.server.web.view;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import wisematches.server.web.services.agent.WebClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMBrowserDetectionInterceptor extends HandlerInterceptorAdapter {
	public WMBrowserDetectionInterceptor() {
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
		switch (detect.getUserAgent()) {
			case CHROME:
				return detect.getMajorVersion() >= 12;
			case OPERA:
				return detect.getMajorVersion() >= 11;
			case FIREFOX:
				return detect.getMajorVersion() >= 4;
			case SAFARI:
				return detect.getMajorVersion() >= 5;
		}
		return Boolean.FALSE;
	}
}
