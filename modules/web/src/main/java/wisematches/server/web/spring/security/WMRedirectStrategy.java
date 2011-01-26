package wisematches.server.web.spring.security;

import org.springframework.security.web.DefaultRedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMRedirectStrategy extends DefaultRedirectStrategy {
	private final Set<String> redirectParameters = new HashSet<String>();

	public WMRedirectStrategy() {
	}

	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		final StringBuilder b = new StringBuilder();
		b.append(url);
		for (String p : redirectParameters) {
			b.append("&").append(p).append("=").append(request.getParameter(p));
		}
		super.sendRedirect(request, response, b.toString());
	}

	public Set<String> getRedirectParameters() {
		return new HashSet<String>(redirectParameters);
	}

	public void setRedirectParameters(Set<String> redirectParameters) {
		this.redirectParameters.clear();

		if (redirectParameters != null) {
			this.redirectParameters.addAll(redirectParameters);
		}
	}
}
