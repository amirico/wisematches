package wisematches.server.web.security.spring.fixes;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Internal issue: http://code.google.com/p/wisematches/issues/detail?id=107
 * <p/>
 * This filter must be after SESSION_MANAGEMENT_FILTER
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SessionRegistryFixFilter extends GenericFilterBean {
	private SessionRegistry sessionRegistry;

	public SessionRegistryFixFilter() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;

		final HttpSession session = request.getSession(false);
		if (session != null) {
			final SessionInformation info = sessionRegistry.getSessionInformation(session.getId());
			if (info == null) {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication != null && authentication.getPrincipal() != null) {
					sessionRegistry.registerNewSession(session.getId(), authentication.getPrincipal());
				}
			}
		}
		chain.doFilter(req, res);
	}

	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}
}
