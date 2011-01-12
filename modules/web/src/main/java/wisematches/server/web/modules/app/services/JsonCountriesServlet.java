package wisematches.server.web.modules.app.services;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import wisematches.server.player.Language;
import wisematches.server.player.profile.counties.CountriesManager;
import wisematches.server.player.profile.counties.Country;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class JsonCountriesServlet implements Controller {
	private CountriesManager countriesManager;

	public JsonCountriesServlet() {
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String locale = request.getParameter("locale");
		sendResult(locale, response);
		return null;
	}

	private void sendResult(String localeName, HttpServletResponse response) throws IOException {
		response.setContentType("application/jsonrequest; charset=utf-8");

		Language language = null;
		if (localeName != null) {
			language = Language.byCode(localeName);
		}

		final List<Country> countries = countriesManager.getCountries(language);
		final PrintWriter writer = response.getWriter();
		writer.println("{ \"countries\": [");
		for (Country country : countries) {
			writer.print("{ \"code\": \"");
			writer.print(country.getCode());
			writer.print("\", \"name\": \"");
			writer.print(country.getName());
			writer.print("\"}");
			writer.print(",");
			writer.println();
		}
		writer.println("]}");
		writer.flush();
	}

	public void setCountriesManager(CountriesManager countriesManager) {
		this.countriesManager = countriesManager;
	}
}
