package wisematches.server.web.view;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMFreeMarkerView extends FreeMarkerView {
	public WMFreeMarkerView() {
	}

	@Override
	protected void processTemplate(Template template, SimpleHash model, HttpServletResponse response) throws IOException, TemplateException {
		final HttpRequestParametersHashModel sm2 = (HttpRequestParametersHashModel) model.get(FreemarkerServlet.KEY_REQUEST_PARAMETERS);
		if ("true".equalsIgnoreCase(String.valueOf(sm2.get("plain"))) ||
				(model.get("plain") != null && ((TemplateBooleanModel) model.get("plain")).getAsBoolean())) {
			super.processTemplate(template, model, response);
		} else {
			model.put("originalTemplateName", getUrl());
			super.processTemplate(getTemplate("/content/wisematches.ftl", template.getLocale()), model, response);
		}
	}
}
