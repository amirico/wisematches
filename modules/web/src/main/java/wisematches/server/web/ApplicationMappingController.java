package wisematches.server.web;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import wisematches.server.deprecated.web.server.sessions.WebSessionCustomHouse;
import wisematches.server.player.Player;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ApplicationMappingController implements Controller {
	private String signinPageUrl;
	private String applicationPageUrl;

	private WebSessionCustomHouse sessionCustomHouse;

	protected static final String APP_MAPPING = "/app";
	protected static final String SIGNIN_MAPPING = "/signin";

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String uri = request.getRequestURI();
		final HttpSession session = request.getSession(true); // Create new session if not exist
		final Player player = sessionCustomHouse.getLoggedInPlayer(session);

		if (uri.startsWith(APP_MAPPING)) {
			if (player == null) {
				sendRedirect(request, response, SIGNIN_MAPPING);
			} else {
				writePageContent(applicationPageUrl, session.getServletContext(), request, response);
			}
		} else if (uri.startsWith(SIGNIN_MAPPING)) {
			if (player != null) {
				sessionCustomHouse.performLogout(player, session);
			}
			writePageContent(signinPageUrl, session.getServletContext(), request, response);
		} else {
			if (player != null) {
				sendRedirect(request, response, APP_MAPPING);
			} else {
				sendRedirect(request, response, SIGNIN_MAPPING);
			}
		}
		return null;
	}

	private void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		response.sendRedirect(url);
	}

	private void writePageContent(String pageName, ServletContext context,
								  HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");

		final InputStream asStream = context.getResourceAsStream(pageName);
		if (asStream == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Internal HTML page not found: " + pageName);
			return;
		}

		final BufferedReader reader = new BufferedReader(new InputStreamReader(asStream));
		final PrintWriter writer = response.getWriter();
		do {
			final String line = reader.readLine();
			if (line == null) {
				break;
			}

			writer.println(line);

		} while (true);
		writer.flush();
	}

	public void setSigninPageUrl(String signinPageUrl) {
		this.signinPageUrl = signinPageUrl;
	}

	public void setApplicationPageUrl(String applicationPageUrl) {
		this.applicationPageUrl = applicationPageUrl;
	}

	public void setSessionCustomHouse(WebSessionCustomHouse sessionCustomHouse) {
		this.sessionCustomHouse = sessionCustomHouse;
	}
}