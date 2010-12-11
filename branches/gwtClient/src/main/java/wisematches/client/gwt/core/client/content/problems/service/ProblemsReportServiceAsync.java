package wisematches.client.gwt.core.client.content.problems.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import wisematches.client.gwt.core.client.content.problems.Browser;
import wisematches.client.gwt.core.client.content.problems.OperationSystems;

public interface ProblemsReportServiceAsync {
    void reportProblem(String username, String email, String accountName, OperationSystems os,
                       Browser browser, String subject, String message, AsyncCallback<Void> async);

    void bugReport(Throwable throwable, AsyncCallback<Void> async);
}
