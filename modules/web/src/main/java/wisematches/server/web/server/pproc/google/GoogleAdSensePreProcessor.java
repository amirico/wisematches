package wisematches.server.web.server.pproc.google;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.server.player.Language;
import wisematches.server.web.ResourceManager;
import wisematches.server.web.server.pproc.PagePreProcessor;
import wisematches.server.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GoogleAdSensePreProcessor implements PagePreProcessor {
	private String clientId;
	private String dashboardSlot;
	private String gameboardSlot;
	private String playboardSlot;
	private String searchId;

	private static final Log log = LogFactory.getLog("wisematches.server.web.server.pproc.adsense");

	public boolean isPreProcessorEnabled(String line) {
		return line.trim().startsWith("<!-- Google AdSense -->");
	}

	public void processPageContent(PrintWriter writer, HttpServletRequest request, HttpServletResponse response) {
		if (clientId == null || clientId.length() == 0) {
			return;
		}

		if (dashboardSlot != null && dashboardSlot.length() != 0) {
			writer.println("<div id=\"sponsors-dashboard\" style=\"display: none;\">");
			writer.println("  <script type=\"text/javascript\"><!--");
			writer.println("  google_ad_client = \"" + clientId + "\";");
			writer.println("  /* DASHBOARD */");
			writer.println("  google_ad_slot = \"" + dashboardSlot + "\";");
			writer.println("  google_ad_width = 300;");
			writer.println("  google_ad_height = 250;");
			writer.println("  //-->");
			writer.println("  </script>");
			writer.println("  <script type=\"text/javascript\"");
			writer.println("  src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\">");
			writer.println("  </script>");
			writer.println("</div>");
		}

		if (gameboardSlot != null && gameboardSlot.length() != 0) {
			writer.println("<div id=\"sponsors-gameboard\" style=\"display: none;\">");
			writer.println("  <script type=\"text/javascript\"><!--");
			writer.println("  google_ad_client = \"" + clientId + "\";");
			writer.println("  /* GAMEBOARD */");
			writer.println("  google_ad_slot = \"" + gameboardSlot + "\";");
			writer.println("  google_ad_width = 160;");
			writer.println("  google_ad_height = 600;");
			writer.println("  //-->");
			writer.println("  </script>");
			writer.println("  <script type=\"text/javascript\"");
			writer.println("  src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\">");
			writer.println("  </script>");
			writer.println("</div>");
		}
/*

        if (playboardSlot != null && playboardSlot.length() != 0) {
            writer.println("<div id=\"sponsors-playboard\" style=\"display: none;\">");
            writer.println("  <script type=\"text/javascript\"><!--");
            writer.println("  google_ad_client = \"" + clientId + "\";");
            writer.println("  */
/* PLAYBOARD *//*
");
            writer.println("  google_ad_slot = \"" + playboardSlot + "\";");
            writer.println("  google_ad_width = 160;");
            writer.println("  google_ad_height = 120;");
            writer.println("  //-->");
            writer.println("  </script>");
            writer.println("  <script type=\"text/javascript\"");
            writer.println("  src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\">");
            writer.println("  </script>");
            writer.println("</div>");
        }
*/

		if (searchId != null && searchId.length() != 0) {
			final Language language = WebUtils.getRequestAttrbiute(request, Language.class);

			writer.println("<div id=\"sponsors-search\" style=\"display: none;\">");
			writer.println("  <form action=\"http://www.google.com/cse\" id=\"cse-search-box\" target=\"_blank\">");
			writer.println("  <div>");
			writer.println("  <input type=\"hidden\" name=\"cx\" value=\"" + searchId + "\" />");
			writer.println("  <input type=\"hidden\" name=\"ie\" value=\"UTF-8\" />");
			writer.println("  <input type=\"text\" name=\"q\" size=\"31\" />");
			writer.println("  <input type=\"submit\" name=\"sa\" value=\"" + ResourceManager.getString("app.search", "Search", language) + "\" />");
			writer.println("  </div>");
			writer.println("</form>");
			writer.print("  <script type=\"text/javascript\" src=\"http://www.google.com/coop/cse/brand?form=cse-search-box&amp;lang=");
			writer.print(language.code());
			writer.println("\"></script>");
			writer.println("</div>");
		}
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setDashboardSlot(String dashboardSlot) {
		this.dashboardSlot = dashboardSlot;
	}

	public void setGameboardSlot(String gameboardSlot) {
		this.gameboardSlot = gameboardSlot;
	}

	public void setPlayboardSlot(String playboardSlot) {
		this.playboardSlot = playboardSlot;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
}
