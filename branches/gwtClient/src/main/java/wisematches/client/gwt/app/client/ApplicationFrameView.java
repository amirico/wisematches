package wisematches.client.gwt.app.client;

import com.smartgwt.client.widgets.Canvas;
import wisematches.client.gwt.app.client.settings.ParameterInfo;
import wisematches.client.gwt.app.client.settings.Settings;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface ApplicationFrameView {
	void initialize(ApplicationFrame applicationFrame, Settings frameViewSettings);

	Canvas getFrameViewComponent();

	ParameterInfo[] getParametersInfos();


	void activate(Parameters parameters);

	void deactivate();
}