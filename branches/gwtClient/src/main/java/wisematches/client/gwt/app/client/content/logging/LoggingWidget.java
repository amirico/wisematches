package wisematches.client.gwt.app.client.content.logging;

import com.smartgwt.client.widgets.Canvas;
import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationFrameView;
import wisematches.client.gwt.app.client.Parameters;
import wisematches.client.gwt.app.client.settings.ParameterInfo;
import wisematches.client.gwt.app.client.settings.Settings;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class LoggingWidget extends Canvas implements ApplicationFrameView {
//    public static final String ITEM_ID = "logging";
//
//    private final HTMLPanel html = new HTMLPanel("<div id=\"logger-panel\"/>");
//
//    public LoggingWidget() {
//        Log.addLogger(new TheLogger());
//    }
//
//    public void initialize(ApplicationFrame applicationFrame, Settings frameViewSettings) {
//        final Toolbar toolbar = new Toolbar();
//        toolbar.addButton(new ToolbarButton("Clear", new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                html.clear();
//            }
//        }));
//
//        setLayout(new FitLayout());
//        setAutoScroll(true);
//        setTopToolbar(toolbar);
//
//        add(html);
//    }
//
//    public Component getFrameViewComponent() {
//        return this;
//    }
//
//    public ParameterInfo[] getParametersInfos() {
//        return new ParameterInfo[0];
//    }
//
//    public void activate(Parameters parameters) {
//    }
//
//    public void deactivate() {
//    }
//
//    private class TheLogger implements Logger {
//        public void debug(String message, Throwable throwable) {
//            log(Priority.DEBUG, message, throwable);
//        }
//
//        public void error(String message, Throwable throwable) {
//            log(Priority.ERROR, message, throwable);
//        }
//
//        public void fatal(String message, Throwable throwable) {
//            log(Priority.FATAL, message, throwable);
//        }
//
//        public void info(String message, Throwable throwable) {
//            log(Priority.INFO, message, throwable);
//        }
//
//        public void trace(String message, Throwable throwable) {
//            log(Priority.TRACE, message, throwable);
//        }
//
//        public void warn(String message, Throwable throwable) {
//            log(Priority.WARN, message, throwable);
//        }
//
//        private void log(Priority level, String message, Throwable th) {
//            html.add(new HTML("<div class=\"logger-" + level.name().toLowerCase() + "\">" + message + "</div>"), "logger-panel");
//        }
//
//        public void clear() {
//            html.clear();
//        }
//
//        public void setCurrentLogLevel(int level) {
//        }
//
//        public void diagnostic(String message, Throwable throwable) {
//        }
//
//        public boolean isSupported() {
//            return true;
//        }
//    }

	@Override
	public void initialize(ApplicationFrame applicationFrame, Settings frameViewSettings) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Canvas getFrameViewComponent() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public ParameterInfo[] getParametersInfos() {
		return new ParameterInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void activate(Parameters parameters) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void deactivate() {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
