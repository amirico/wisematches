package wisematches.server.web.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.security.PersonalityContext;
import wisematches.playground.GameMessageSource;
import wisematches.server.web.servlet.view.StaticContentGenerator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
public abstract class WisematchesController {
	private final String title;

	@Autowired
	protected GameMessageSource gameMessageSource;

	@Autowired
	protected StaticContentGenerator staticContentGenerator;

	protected WisematchesController() {
		this("title.playboard");
	}

	protected WisematchesController(String title) {
		this.title = title;
	}

	@Deprecated
	public Player getPrincipal() {
		throw new UnsupportedOperationException("TODO: deprecated");
	}

	@ModelAttribute("personality")
	public Personality getPersonality() {
		return PersonalityContext.getPersonality();
	}

	@ModelAttribute("title")
	public String getHeaderTitle() {
		return title;
	}

	@ModelAttribute("requestQueryString")
	public final String getRequestQueryString() {
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		return request.getQueryString();
	}
}