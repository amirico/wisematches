package wisematches.server.utils.sessions.impl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotBeanInterfaceImpl implements NotBeanInterface {
	private final Collection<String> items = new ArrayList<String>();

	public void addItem(String s) {
		items.add(s);
	}

	public void removeItem(String s) {
		items.remove(s);
	}

	public Collection<String> getItems() {
		return items;
	}
}
