package wisematches.server.web.view;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMFreeMarkerViewResolver extends FreeMarkerViewResolver {
	private boolean exposeRedirectModelAttributes = false;

	public WMFreeMarkerViewResolver() {
	}

	@Override
	protected View createView(String viewName, Locale locale) throws Exception {
		View view = super.createView(viewName, locale);
		if (view instanceof RedirectView) {
			((RedirectView) view).setExposeModelAttributes(exposeRedirectModelAttributes);
		}
		return view;
	}

	public boolean isExposeRedirectModelAttributes() {
		return exposeRedirectModelAttributes;
	}

	public void setExposeRedirectModelAttributes(boolean exposeRedirectModelAttributes) {
		this.exposeRedirectModelAttributes = exposeRedirectModelAttributes;
	}
}
