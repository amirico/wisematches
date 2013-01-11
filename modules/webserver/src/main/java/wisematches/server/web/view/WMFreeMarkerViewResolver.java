package wisematches.server.web.view;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMFreeMarkerViewResolver extends FreeMarkerViewResolver {
	private boolean exposeRedirectModelAttributes = false;
	private Map<String, Class<? extends Enum>> exposeEnums = new HashMap<>();

	public WMFreeMarkerViewResolver() {
	}

	@Override
	protected View createView(String viewName, Locale locale) throws Exception {
		View view = super.createView(viewName, locale);
		if (view instanceof RedirectView) {
			((RedirectView) view).setExposeModelAttributes(exposeRedirectModelAttributes);
		}

		if (view instanceof WMFreeMarkerView) {
			final WMFreeMarkerView mv = (WMFreeMarkerView) view;
			mv.setExposeEnums(exposeEnums);
		}
		return view;
	}

	public boolean isExposeRedirectModelAttributes() {
		return exposeRedirectModelAttributes;
	}

	public void setExposeRedirectModelAttributes(boolean exposeRedirectModelAttributes) {
		this.exposeRedirectModelAttributes = exposeRedirectModelAttributes;
	}

	public void setExposeEnums(Map<String, Class<? extends Enum>> exposeEnums) {
		this.exposeEnums = exposeEnums;
	}
}
