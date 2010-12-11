package wisematches.client.gwt.login.client.content.i18n;

import com.google.gwt.core.client.GWT;
import wisematches.client.gwt.core.client.content.i18n.CommonResources;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface Resources extends CommonResources {
    LoginConstants LOGIN = GWT.create(LoginConstants.class);
    LoginMessages MLOGIN = GWT.create(LoginMessages.class);
}
