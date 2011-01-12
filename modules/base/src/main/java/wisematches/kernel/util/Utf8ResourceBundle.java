package wisematches.kernel.util;

import wisematches.server.player.Language;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * {@code ResourceBundle} that support UTF8 encoding.
 * <p/>
 * This class code taken from here <a href="http://www.thoughtsabout.net/blog/archives/000044.html">
 * http://www.thoughtsabout.net/blog/archives/000044.html</a>
 *
 * @deprecated spring has such classes. Should be replaced to Spring code
 */
@Deprecated
public final class Utf8ResourceBundle {
	private Utf8ResourceBundle() {
	}

	public static ResourceBundle getBundle(String baseName) {
		final ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES);
		final ResourceBundle bundle = ResourceBundle.getBundle(baseName, control);
		return createUtf8PropertyResourceBundle(bundle);
	}

	public static ResourceBundle getBundle(String baseName, Language locale) {
		final ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES);
		final ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale.locale(), control);
		return createUtf8PropertyResourceBundle(bundle);
	}

	public static ResourceBundle getBundle(String baseName, Language locale, ClassLoader loader) {
		final ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES);
		final ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale.locale(), loader, control);
		return createUtf8PropertyResourceBundle(bundle);
	}

	private static ResourceBundle createUtf8PropertyResourceBundle(ResourceBundle bundle) {
		if (!(bundle instanceof PropertyResourceBundle)) {
			return bundle;
		}
		return new Utf8PropertyResourceBundle((PropertyResourceBundle) bundle);
	}

	private static class Utf8PropertyResourceBundle extends ResourceBundle {
		PropertyResourceBundle bundle;

		private Utf8PropertyResourceBundle(PropertyResourceBundle bundle) {
			this.bundle = bundle;
		}

		/* (non-Javadoc)
				* @see java.util.ResourceBundle#getKeys()
				*/
		@Override
		public Enumeration<String> getKeys() {
			return bundle.getKeys();
		}

		@Override
		protected Object handleGetObject(String key) {
			String value = bundle.getString(key);
			if (value == null) {
				return null;
			}
			try {
				return new String(value.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// Shouldn't fail - but should we still add logging message?
				return null;
			}
		}
	}
}