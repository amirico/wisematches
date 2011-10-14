package wisematches.server.web.services.thesaurus.impl;

import wisematches.server.web.services.thesaurus.Thesaurus;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultThesaurus implements Thesaurus {
	private final String name;
	private final String urlTemplate;

	public DefaultThesaurus(String name, String urlTemplate) {
		this.name = name;
		this.urlTemplate = urlTemplate;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUrlTemplate() {
		return urlTemplate;
	}
}
