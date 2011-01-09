package wisematches.server.core.sessions.impl;

import wisematches.server.core.sessions.ImplementationBean;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@ImplementationBean(NotBeanInterfaceImpl.class)
public interface NotBeanInterface {
    void addItem(String s);

    void removeItem(String s);

    Collection<String> getItems();
}
