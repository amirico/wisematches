package wisematches.server.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.server.security.WMSecurityContext;
import wisematches.server.web.i18n.GameMessageSource;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
public abstract class WisematchesController {
	protected GameMessageSource gameMessageSource;

	protected WisematchesController() {
	}

	@ModelAttribute("principal")
	public Player getPrincipal() {
		return WMSecurityContext.getPlayer();
	}

	@ModelAttribute("personality")
	public Personality getPersonality() {
		return WMSecurityContext.getPersonality();
	}

	@ModelAttribute("headerTitle")
	public String getHeaderTitle() {
		return "title.playboard";
	}

	@ModelAttribute("requestQueryString")
	public String getRequestQueryString() {
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		return request.getQueryString();
	}

	@Autowired
	public void setGameMessageSource(GameMessageSource gameMessageSource) {
		this.gameMessageSource = gameMessageSource;
	}
}
