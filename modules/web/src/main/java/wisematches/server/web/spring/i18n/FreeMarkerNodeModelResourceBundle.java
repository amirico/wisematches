/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.spring.i18n;

import freemarker.ext.dom.NodeModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import wisematches.core.utils.StringComparators;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;

/**
 * TODO: java docs
 *
 * @author klimese
 */
public class FreeMarkerNodeModelResourceBundle {
	private String[] resourcesPaths;
	private String defaultEncoding = "UTF-8";
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

	private final Map<String, Map<Locale, NodeModel>> cachedFilenames = new HashMap<String, Map<Locale, NodeModel>>();

	private static final Log log = LogFactory.getLog("wisematches.server.web.spring.info");

	public FreeMarkerNodeModelResourceBundle() {
	}

	public NodeModel getInfoHolder(String name, Locale locale) {
		synchronized (cachedFilenames) {
			Map<Locale, NodeModel> holder = cachedFilenames.get(name);
			if (holder == null) {
				holder = new HashMap<Locale, NodeModel>();
				cachedFilenames.put(name, holder);
			}

			NodeModel informationHolder = holder.get(locale);
			if (informationHolder == null) {
				informationHolder = loadInfoHolder(name, locale);
				holder.put(locale, informationHolder);
			}
			return informationHolder;
		}
	}

	protected NodeModel loadInfoHolder(String name, Locale locale) {
		final List<String> filenames = new ArrayList<String>();
		filenames.add(name);
		filenames.addAll(calculateFilenamesForLocale(name, locale));

		for (int j = filenames.size() - 1; j >= 0; j--) {
			final String filename = filenames.get(j);
			for (String path : resourcesPaths) {
				if (!path.endsWith(File.separator)) {
					path = path + File.separator;
				}
				final Resource resource = resourceLoader.getResource(path + filename + ".properties");
				if (resource.exists()) {
					return parseInfoHolder(resource);
				}
			}
		}
		return null;
	}

	protected NodeModel parseInfoHolder(Resource resource) {
		try {
			final Properties p = new Properties();
			propertiesPersister.load(p, new InputStreamReader(resource.getInputStream(), defaultEncoding));

			final DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			final DocumentBuilder bd = fact.newDocumentBuilder();
			final org.w3c.dom.Document document = bd.newDocument();

			final Set<String> keys = new TreeSet<String>(StringComparators.getNaturalComparatorAscii());
			for (Object o : p.keySet()) {
				keys.add((String) o);
			}

			final Map<String, Element> cache = new HashMap<String, Element>();

			final Element root = document.createElement("item");
			cache.put(null, root);
			for (String key : keys) {
				final int i = key.lastIndexOf(".");
				final String cacheKey = i < 0 ? null : key.substring(0, i);
				final String propertyName = i < 0 ? key : key.substring(i + 1);

				Element informationHolder = cache.get(cacheKey);
				if (informationHolder == null) {
					informationHolder = document.createElement("item");
					informationHolder.setAttribute("id", cacheKey);
					cache.put(cacheKey, informationHolder);

					if (cacheKey != null) {
						String name = cacheKey;
						Element holder = informationHolder;
						while (name != null) {
							int k = name.lastIndexOf(".");
							name = k < 0 ? null : name.substring(0, k);

							Element h = cache.get(name);
							if (h == null) {
								h = document.createElement("item");
								h.setAttribute("id", name);
								cache.put(name, h);
							} else {
								name = null;
							}

							Node items = h.getElementsByTagName("items").item(0);
							if (items == null) {
								items = h.appendChild(document.createElement("items"));
							}
							items.appendChild(holder);
							holder = h;
						}
					}
				}

				informationHolder.appendChild(document.createElement(propertyName)).appendChild(document.createTextNode(p.getProperty(key)));
			}

			NodeModel.simplify(root);
			NodeModel m = NodeModel.wrap(root);
			cache.clear();
			return m;
		} catch (Exception ex) {
			log.warn("Properties can't be loaded from resource " + resource, ex);
			return null;
		}
	}

	public void setResourcesPaths(String... resourcesPaths) {
		this.resourcesPaths = resourcesPaths;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
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
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
	}

	/**
	 * Set the PropertiesPersister to use for parsing properties files.
	 * <p>The default is a DefaultPropertiesPersister.
	 *
	 * @param propertiesPersister the properties persister. If null default properties persister will be used.
	 * @see org.springframework.util.DefaultPropertiesPersister
	 */
	public void setPropertiesPersister(PropertiesPersister propertiesPersister) {
		this.propertiesPersister = (propertiesPersister != null ? propertiesPersister : new DefaultPropertiesPersister());
	}

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
}
