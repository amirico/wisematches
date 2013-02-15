package wisematches.server.web.security.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is mix of SimpleUrlAuthenticationFailureHandler and ExceptionMappingAuthenticationFailureHandler because
 * both of them can't be extended.
 * <p/>
 * See https://jira.springsource.org/browse/SEC-1821?focusedCommentId=74587#comment-74587 to get more info.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @see org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
 * @see org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler
 */
public class WMAuthenticationFailureHandler implements AuthenticationFailureHandler {
	private String defaultFailureUrl;
	private boolean allowSessionCreation = true;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private final Map<String, String> failureUrlMap = new HashMap<>();

	private static final Log log = LogFactory.getLog("wisematches.server.web.security");

	public WMAuthenticationFailureHandler() {
	}

	/**
	 * Performs the redirect or forward to the {@code defaultFailureUrl} if set, otherwise returns a 401 error code.
	 * <p/>
	 * If redirecting or forwarding, {@code saveException} will be called to cache the exception for use in
	 * the target view.
	 */
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
										AuthenticationException exception) throws IOException, ServletException {
		String url = getMappingByClass(exception.getClass());
		if (url != null) {
			redirect(request, response, url, exception);
		} else if (defaultFailureUrl == null) {
			if (log.isDebugEnabled()) {
				log.debug("No failure URL set, sending 401 Unauthorized error");
			}
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
		} else {
			redirect(request, response, defaultFailureUrl, exception);
		}
	}

	private String getMappingByClass(Class clazz) {
		final String s = failureUrlMap.get(clazz.getName());
		if (s == null) {
			return getMappingByClass(clazz.getSuperclass());
		}
		return s;
	}

	protected void redirect(HttpServletRequest request, HttpServletResponse response, String url, AuthenticationException exception) throws IOException {
		saveException(request, exception);
		if (log.isDebugEnabled()) {
			log.debug("Redirecting to " + url);
		}
		redirectStrategy.sendRedirect(request, response, url);
	}

	protected void saveException(HttpServletRequest request, AuthenticationException exception) {
		HttpSession session = request.getSession(false);

		if (session != null || allowSessionCreation) {
			request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
		}
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl),
				"'" + defaultFailureUrl + "' is not a valid redirect URL");
		this.defaultFailureUrl = defaultFailureUrl;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	protected boolean isAllowSessionCreation() {
		return allowSessionCreation;
	}

	public void setAllowSessionCreation(boolean allowSessionCreation) {
		this.allowSessionCreation = allowSessionCreation;
	}

	/**
	 * Sets the map of exception types (by name) to URLs.
	 *
	 * @param failureUrlMap the map keyed by the fully-qualified name of the exception class, with the corresponding
	 *                      failure URL as the value.
	 * @throws IllegalArgumentException if the entries are not Strings or the URL is not valid.
	 */
	public void setExceptionMappings(Map<?, ?> failureUrlMap) {
		this.failureUrlMap.clear();
		for (Map.Entry<?, ?> entry : failureUrlMap.entrySet()) {
			Object exception = entry.getKey();
			Object url = entry.getValue();
			Assert.isInstanceOf(String.class, exception, "Exception key must be a String (the exception classname).");
			Assert.isInstanceOf(String.class, url, "URL must be a String");
			Assert.isTrue(UrlUtils.isValidRedirectUrl((String) url), "Not a valid redirect URL: " + url);
			this.failureUrlMap.put((String) exception, (String) url);
		}
	}
}
