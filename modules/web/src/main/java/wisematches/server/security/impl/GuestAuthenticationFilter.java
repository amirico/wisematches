package wisematches.server.security.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;
import wisematches.server.personality.player.computer.guest.GuestPlayer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GuestAuthenticationFilter extends GenericFilterBean implements InitializingBean {
	private AuthenticationFailureHandler failureHandle;
	private AuthenticationSuccessHandler successHandler;

	private String guestProcessingUrl = DEFAULT_GUEST_PROCESSING_URL;

	private static final String PARAMETER_NAME = "WM_GUEST_SESSION";
	private static final String DEFAULT_GUEST_PROCESSING_URL = "/j_spring_security_guest";

	private static final WMUserDetails USER_DETAILS = new WMUserDetails(GuestPlayer.GUEST, "guest", "guest", true);
	private static final GuestAuthenticationToken GUEST_AUTHORIZATION = new GuestAuthenticationToken("guestAuthorizationKey", USER_DETAILS);

	public GuestAuthenticationFilter() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (applyGuestForThisRequest((HttpServletRequest) request)) {
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				SecurityContextHolder.getContext().setAuthentication(GUEST_AUTHORIZATION);

				if (logger.isDebugEnabled()) {
					logger.debug("Populated SecurityContextHolder with guest token: '"
							+ SecurityContextHolder.getContext().getAuthentication() + "'");
				}
			}
			successHandler.onAuthenticationSuccess((HttpServletRequest) request, (HttpServletResponse) response, GUEST_AUTHORIZATION);
		} else {
			chain.doFilter(request, response);
		}
	}

	protected boolean applyGuestForThisRequest(HttpServletRequest request) {
		final boolean guestSession = guestProcessingUrl.equals(request.getRequestURI());
		if (guestSession) {
			final HttpSession session = request.getSession(true);
			if (session.getAttribute(PARAMETER_NAME) == null) {
				session.setAttribute(PARAMETER_NAME, Boolean.TRUE);
				return true;
			}
		}
		return false;
	}

	public void setGuestProcessingUrl(String guestProcessingUrl) {
		Assert.hasText(guestProcessingUrl, "Guest processing url must not be empty or null");
		this.guestProcessingUrl = guestProcessingUrl;
	}

	public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
		this.successHandler = successHandler;
	}

	public void setFailureHandle(AuthenticationFailureHandler failureHandle) {
		this.failureHandle = failureHandle;
	}
}
