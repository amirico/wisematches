package wisematches.server.web.security.guest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;
import wisematches.core.Language;
import wisematches.core.security.PersonalityDetailsManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GuestAuthenticationFilter extends GenericFilterBean implements InitializingBean {
	private AuthenticationFailureHandler failureHandle;
	private AuthenticationSuccessHandler successHandler;
	private PersonalityDetailsManager personalityDetailsManager;

	private String guestProcessingUrl = DEFAULT_GUEST_PROCESSING_URL;

	private static final String DEFAULT_GUEST_PROCESSING_URL = "/j_spring_security_guest";

	public GuestAuthenticationFilter() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (applyGuestForThisRequest((HttpServletRequest) request)) {
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				SecurityContextHolder.getContext().setAuthentication(GUEST_AUTHORIZATION);

				final Locale locale = request.getLocale();
				final Language language = Language.byLocale(locale);

				final UserDetails userDetails = personalityDetailsManager.loadVisitorByLanguage(language);
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
		return guestProcessingUrl.equals(request.getRequestURI());
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

	public void setPersonalityDetailsManager(PersonalityDetailsManager personalityDetailsManager) {
		this.personalityDetailsManager = personalityDetailsManager;
	}
}
