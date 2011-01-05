package wisematches.client.gwt.app.client.content.i18n;

import com.google.gwt.core.client.GWT;
import wisematches.client.gwt.core.client.content.i18n.CommonResources;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface AppRes extends CommonResources {
    ApplicationConstants APP = GWT.create(ApplicationConstants.class);
    ApplicationMessages MAPP = GWT.create(ApplicationMessages.class);
    PlayboardConstants PB = GWT.create(PlayboardConstants.class);
    PlayboardMessages MPB = GWT.create(PlayboardMessages.class);
}
