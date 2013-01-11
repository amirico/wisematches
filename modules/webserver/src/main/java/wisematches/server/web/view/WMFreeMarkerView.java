package wisematches.server.web.view;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.template.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import wisematches.personality.Language;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMFreeMarkerView extends FreeMarkerView {
	private Map<String, TemplateModel> exposeEnums;

	public WMFreeMarkerView() {
	}

	@Override
	protected void processTemplate(Template template, SimpleHash model, HttpServletResponse response) throws IOException, TemplateException {
		model.put("locale", template.getLocale());
		model.put("language", Language.byLocale(template.getLocale()));

		if (exposeEnums != null) {
			for (Map.Entry<String, TemplateModel> entry : exposeEnums.entrySet()) {
				model.put(entry.getKey(), entry.getValue());
			}
		}

		final HttpRequestParametersHashModel sm2 = (HttpRequestParametersHashModel) model.get(FreemarkerServlet.KEY_REQUEST_PARAMETERS);
		if ("true".equalsIgnoreCase(String.valueOf(sm2.get("plain"))) ||
				(model.get("plain") != null && ((TemplateBooleanModel) model.get("plain")).getAsBoolean())) {
			super.processTemplate(template, model, response);
		} else {
			model.put("originalTemplateName", getUrl());
			super.processTemplate(getTemplate("/content/wisematches.ftl", template.getLocale()), model, response);
		}
	}

	@Override
	protected FreeMarkerConfig autodetectConfiguration() throws BeansException {
		try {
			return getApplicationContext().getBean("freemarkerConfig", FreeMarkerConfig.class);
		} catch (NoSuchBeanDefinitionException ex) {
			throw new ApplicationContextException(
					"Must define a single FreeMarkerConfig bean in this web application context " +
							"(may be inherited): FreeMarkerConfigurer is the usual implementation. " +
							"This bean may be given any name.", ex);
		}
	}

	public void setExposeEnums(Map<String, Class<? extends Enum>> exposeEnums) throws TemplateModelException {
		if (exposeEnums != null) {
			this.exposeEnums = new HashMap<>(exposeEnums.size());
			final TemplateHashModel enumModels = BeansWrapper.getDefaultInstance().getEnumModels();
			for (Map.Entry<String, Class<? extends Enum>> entry : exposeEnums.entrySet()) {
				this.exposeEnums.put(entry.getKey(), enumModels.get(entry.getValue().getName()));
			}
		}
	}
}
