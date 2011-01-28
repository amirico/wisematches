package wisematches.server.utils.sessions.impl;

import wisematches.server.utils.sessions.PlayerSessionBean;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ValidInterface2 extends PlayerSessionBean, NotBeanInterface {
	void setString(String s);

	String getString();
}
