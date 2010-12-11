package wisematches.client.gwt.core.client.content.i18n;

import com.google.gwt.core.client.GWT;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface CommonResources {
    CommonConstants COMMON = GWT.create(CommonConstants.class);
    CommonMessages MCOMMON = GWT.create(CommonMessages.class);
}
