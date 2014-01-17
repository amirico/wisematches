package wisematches.server.web.servlet.view.freemarker;

import wisematches.server.web.security.context.PersonalityContext;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SpringSecurityContext {
	public SpringSecurityContext() {
	}

	public boolean hasRole(String role) {
		return PersonalityContext.hasRole(role);
	}
}
