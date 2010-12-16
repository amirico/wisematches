/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.mvc.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.kernel.util.StringComparators;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * This is base controller that processes any requests in <i>/info/*</i>.
 *
 * @author klimese
 */
@Controller
@RequestMapping({"/", "/info"})
public class InformationController {
	private String defaultEncoding = "UTF-8";
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

	private final Map<String, Map<Locale, InformationHolder>> cachedFilenames = new HashMap<String, Map<Locale, InformationHolder>>();

	private static final Log log = LogFactory.getLog("wisematches.server.web.controller.info");

	@RequestMapping("/{pageName}")
	public String infoPages(@PathVariable String pageName, Model model, Locale locale) {
		final InformationHolder informationHolder = getInfoHolder(pageName, locale);
		model.addAttribute("pageName", "/content/common/info/" + pageName + ".ftl");
		model.addAttribute("informationHolder", informationHolder);
		return "/content/login/layout";
	}

	@RequestMapping("/index")
	public String mainPage(Model model) {
		model.addAttribute("pageName", "/content/login/panel/index.ftl");
		return "/content/login/layout";
	}

	protected InformationHolder getInfoHolder(String name, Locale locale) {
		synchronized (cachedFilenames) {
			Map<Locale, InformationHolder> holder = cachedFilenames.get(name);
			if (holder == null) {
				holder = new HashMap<Locale, InformationHolder>();
				cachedFilenames.put(name, holder);
			}

			InformationHolder informationHolder = holder.get(locale);
			if (informationHolder == null) {
				informationHolder = loadInfoHolder(name, locale);
				holder.put(locale, informationHolder);
			}
			return informationHolder;
		}
	}

	protected InformationHolder loadInfoHolder(String name, Locale locale) {
		final List<String> filenames = new ArrayList<String>();
		filenames.add(name);
		filenames.addAll(calculateFilenamesForLocale(name, locale));

		for (int j = filenames.size() - 1; j >= 0; j--) {
			final String filename = filenames.get(j);
			final Resource resource = resourceLoader.getResource("classpath:/i18n/info/" + filename + ".properties");
			if (resource.exists()) {
				return parseInfoHolder(resource);
			}
		}
		return new InformationHolder(null);
	}

	protected InformationHolder parseInfoHolder(Resource resource) {
		try {
			final Properties p = new Properties();
			propertiesPersister.load(p, new InputStreamReader(resource.getInputStream(), defaultEncoding));

			final Set<String> keys = new TreeSet<String>(StringComparators.getNaturalComparatorAscii());
			for (Object o : p.keySet()) {
				keys.add((String) o);
			}

			final Map<String, InformationHolder> cache = new HashMap<String, InformationHolder>();
			for (String key : keys) {
				final int i = key.lastIndexOf(".");
				final String cacheKey = i < 0 ? null : key.substring(0, i);
				final String propertyName = i < 0 ? key : key.substring(i + 1);

				InformationHolder informationHolder = cache.get(cacheKey);
				if (informationHolder == null) {
					informationHolder = new InformationHolder(cacheKey);
					cache.put(cacheKey, informationHolder);

					if (cacheKey != null) {
						String name = cacheKey;
						InformationHolder holder = informationHolder;
						while (name != null) {
							int k = name.lastIndexOf(".");
							name = k < 0 ? null : name.substring(0, k);
							InformationHolder h = cache.get(name);
							if (h == null) {
								h = new InformationHolder(name);
								cache.put(name, h);
								h.addItem(holder);
								holder = h;
							} else {
								h.addItem(holder);
								name = null;
							}
						}
					}
				}

				final String value = p.getProperty(key);
				if ("label".equals(propertyName)) {
					informationHolder.setLabel(value);
				} else if ("description".equals(propertyName)) {
					informationHolder.setDescription(value);
				} else if ("image".equals(propertyName)) {
					informationHolder.setImage(value);
				}
			}
			final InformationHolder root = cache.get(null);
			cache.clear();
			return root;
		} catch (IOException ex) {
			log.warn("Properties can't be loaded from resource " + resource, ex);
			return new InformationHolder(null);
		}
	}

	/**
	 * This method taken from {@code org.springframework.context.support.ReloadableResourceBundleMessageSource} class
	 * <p/>
	 * Calculate the filenames for the given bundle basename and Locale,
	 * appending language code, country code, and variant code.
	 * E.g.: basename "messages", Locale "de_AT_oo" -> "messages_de_AT_OO",
	 * "messages_de_AT", "messages_de".
	 * <p>Follows the rules defined by {@link java.util.Locale#toString()}.
	 *
	 * @param basename the basename of the bundle
	 * @param locale   the locale
	 * @return the List of filenames to check
	 */
	protected List<String> calculateFilenamesForLocale(String basename, Locale locale) {
		List<String> result = new ArrayList<String>(3);
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();
		StringBuilder temp = new StringBuilder(basename);

		temp.append('_');
		if (language.length() > 0) {
			temp.append(language);
			result.add(0, temp.toString());
		}

		temp.append('_');
		if (country.length() > 0) {
			temp.append(country);
			result.add(0, temp.toString());
		}

		if (variant.length() > 0 && (language.length() > 0 || country.length() > 0)) {
			temp.append('_').append(variant);
			result.add(0, temp.toString());
		}

		return result;
	}

	/**
	 * Set the PropertiesPersister to use for parsing properties files.
	 * <p>The default is a DefaultPropertiesPersister.
	 *
	 * @param propertiesPersister the properties persister. If null default properties persister will be used.
	 * @see org.springframework.util.DefaultPropertiesPersister
	 */
	@Autowired(required = false)
	public void setPropertiesPersister(PropertiesPersister propertiesPersister) {
		this.propertiesPersister = (propertiesPersister != null ? propertiesPersister : new DefaultPropertiesPersister());
	}

	/**
	 * Set the ResourceLoader to use for loading bundle properties files.
	 * <p>The default is a DefaultResourceLoader. Will get overridden by the
	 * ApplicationContext if running in a context, as it implements the
	 * ResourceLoaderAware interface. Can be manually overridden when
	 * running outside of an ApplicationContext.
	 *
	 * @param resourceLoader the resource loaded. If null default resource loaded will be used
	 * @see org.springframework.core.io.DefaultResourceLoader
	 * @see org.springframework.context.ResourceLoaderAware
	 */
	@Autowired(required = false)
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

}