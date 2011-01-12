package wisematches.server.web;

import wisematches.server.player.Language;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Deprecated class {@link org.springframework.context.MessageSource} class must be used instead
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 * @deprecated Spring classes should be used: MessageSource
 */
@Deprecated
public class ResourceManager {
	private static DynamicBundle commonBundle = null;

	private static String activeModule = null;

	private static final String COMMON_PACKAGE_PREFIX = "i18n.";
	private static final String DEFAULT_BUNDLE_NAME = "common";
	private static final String BUNDLE_NAME_PROPERTY = "resource.manager.common.bundle";

	private ResourceManager() {
	}

	private static String getCommonResourceName() {
		return System.getProperty(BUNDLE_NAME_PROPERTY, DEFAULT_BUNDLE_NAME);
	}

	public static synchronized DynamicBundle getCommonResource(Language locale) {
		if (commonBundle == null) {
			final String commonResourceName = getCommonResourceName();
			commonBundle = new DynamicBundle(getUTFBundle(commonResourceName, locale));


			if (!DEFAULT_BUNDLE_NAME.equals(commonResourceName)) {
				commonBundle.setParent(getUTFBundle(DEFAULT_BUNDLE_NAME, locale));
			}
		}
		return commonBundle;
	}

	private static ResourceBundle getUTFBundle(String commonResourceName, Language locale) {
		return new UTF8ResourceBundle(ResourceBundle.getBundle(COMMON_PACKAGE_PREFIX + commonResourceName, locale.locale()));
	}

	/**
	 * You can activate a module with specified name in manager. It means that specified name will be automatical added
	 * in start of all required resources. If specified resources dosn't exist in activated module manager gets module
	 * name from specified resource key with the help of <code>getModuleName</code> mehtod. <br> If resource doesn't
	 * exist again, the common module will be used to find the resource.
	 *
	 * @param name the module name.
	 */
	public static void activateModule(String name) {
		activeModule = name;
	}

	/**
	 * It's a good practic to call this method then you finished work with module's resources. It increases speed of
	 * looking resources for other modules.
	 */
	public static void deactivateModule() {
		activeModule = null;
	}

	/**
	 * Returns value of resource for specified key.<br> 1. If you have activated module, first the resource will be
	 * looking for in this module (you don't have to specified module prefic for these resources).<br> 2. If you don't
	 * have activated module, module name will be getted from resource key (see <code>getModuleName</code>)<br> 3.
	 * Resource is looking for in common resources.
	 *
	 * @param key	the resource key
	 * @param defalt the default value that will be returned if resource doesn't exist.
	 * @param locale locale
	 * @return the resource value or <code>defalt</code> if one doesn't exist.
	 */
	public static String getString(String key, String defalt, Language locale) {
		try {
			String value = null;
			if (key == null) {// added by NM
				final String msg = "Wrong key: null";
				throw new MissingResourceException(msg, ResourceManager.class.getName(), key);
			}
			if (activeModule != null) {
				value = getModuleResource(activeModule, key, locale);
			}
			if (value == null) {
				value = getModuleResource(getModuleName(key), getModuleKey(key), locale);
			}
			if (value == null) {
				value = getCommonResource(locale).getString(key);
			}
			return value;
		} catch (MissingResourceException e) {
			return defalt;
		}
	}

	/**
	 * Returns value of resource for specified key.<br> 1. If you have activated module, first the resource will be
	 * looking for in this module (you don't have to specified module prefic for these resources).<br> 2. If you don't
	 * have activated module, module name will be getted from resource key (see <code>getModuleName</code>)<br> 3.
	 * Resource is looking for in common resources.
	 *
	 * @param key	the resource key
	 * @param locale locale
	 * @return the resource value or <code>???<i>key</i>???</code> if one doesn't exist.
	 */
	public static String getString(String key, Language locale) {
		return getString(key, "???" + key + "???", locale);
	}

	public static String getFormatString(String key, Language locale, String... values) {
		Map<String, Object> model = new HashMap<String, Object>();
		for (String value : values) {
			final int i = value.indexOf("=");
			if (i == -1) {
				throw new IllegalArgumentException("Value must be in format: a=b");
			}
			model.put(value.substring(0, i), value.substring(i + 1));
		}
		return getFormatString(key, locale, model);
	}

	public static String getFormatString(String key, Language locale, Map<String, Object> model) {
		final String s = getString(key, locale);

		final StringBuilder b = new StringBuilder();
		int lastIndex = 0;
		do {
			int beginIndex = s.indexOf("${", lastIndex);
			if (beginIndex == -1) {
				break;
			}
			int endIndex = s.indexOf("}", beginIndex);
			if (endIndex == -1) {
				break;
			}

			b.append(s.substring(lastIndex, beginIndex));
			b.append(model.get(s.substring(beginIndex + 2, endIndex)));
			lastIndex = endIndex + 1;
		} while (true);

		if (lastIndex != s.length()) {
			b.append(s.substring(lastIndex));
		}
		return b.toString();
	}

	private static String getModuleName(String key) {
		int index = key.indexOf(".");
		if (index == -1) {
			return null;
		}
		return key.substring(0, index);
	}

	private static String getModuleKey(String key) {
		int index = key.indexOf(".");
		if (index == -1) {
			return key;
		}
		return key.substring(index + 1);
	}

	private static String getModuleResource(String module, String key, Language locale) {
		try {
			ResourceBundle bundle = getUTFBundle(module, locale);
			return bundle.getString(key);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Deactivate active module and removes <code>commonBundle</code>. So in next time new <code>commonBundle</code>
	 * will be created.
	 */
	public static synchronized void clear() {
		deactivateModule();
		commonBundle = null;
	}

	/**
	 * @author SKlimenko
	 */
	public static class DynamicBundle extends CompositeBundle {
		private Map<String, Object> dynamicResources = new HashMap<String, Object>(0);

		DynamicBundle(ResourceBundle bundle) {
			super(bundle);
		}

		public void addResource(String key, Object res) {
			dynamicResources.put(key, res);
		}

		protected Object handleGetObject(String key) {
			return (dynamicResources.containsKey(key)) ? dynamicResources.get(key) : super.handleGetObject(key);
		}
	}

	private static class CompositeBundle extends ResourceBundle {
		private final ResourceBundle bundle;

		CompositeBundle(ResourceBundle bundle) {
			this.bundle = bundle;
		}

		public Enumeration<String> getKeys() {
			return bundle.getKeys();
		}

		protected Object handleGetObject(String key) {
			Object res;
			if (parent == null) {
				res = bundle.getObject(key);
			} else {
				try {
					res = bundle.getObject(key);
				} catch (MissingResourceException e) {
					res = parent.getObject(key);
				}
			}
			return res;
		}

		public void setParent(ResourceBundle parent) {
			super.setParent(parent);
		}
	}

	private static class UTF8ResourceBundle extends ResourceBundle {
		private final ResourceBundle bundle;

		private UTF8ResourceBundle(ResourceBundle bundle) {
			this.bundle = bundle;
		}

		protected Object handleGetObject(String key) {
			String value = bundle.getString(key);
			if (value == null) return null;
			try {
				return new String(value.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}

		public Enumeration<String> getKeys() {
			return bundle.getKeys();
		}
	}
}
