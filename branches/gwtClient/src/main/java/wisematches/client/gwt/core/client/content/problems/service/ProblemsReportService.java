package wisematches.client.gwt.core.client.content.problems.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import wisematches.client.gwt.core.client.content.problems.Browser;
import wisematches.client.gwt.core.client.content.problems.OperationSystems;

/**
 * <code>ProblemsReportService</code> this service can be used to report problems from users.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface ProblemsReportService extends RemoteService {
    void reportProblem(String username, String email, String accountName, OperationSystems os,
                       Browser browser, String subject, String message);

    void bugReport(Throwable throwable);

    /**
     * Utility/Convenience class.
     * Use ProblemsReportService.App.getInstance() to access static instance of ProblemsReportServiceAsync
     */
    public static class App {
        private static ProblemsReportServiceAsync ourInstance = null;

        public static synchronized ProblemsReportServiceAsync getInstance() {
            if (ourInstance == null) {
                ourInstance = (ProblemsReportServiceAsync) GWT.create(ProblemsReportService.class);
                ((ServiceDefTarget) ourInstance).setServiceEntryPoint("/rpc/ProblemsReportService");
            }
            return ourInstance;
        }
    }
}
