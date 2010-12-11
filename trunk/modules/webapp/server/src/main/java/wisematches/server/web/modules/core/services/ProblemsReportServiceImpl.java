package wisematches.server.web.modules.core.services;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.web.mail.FromTeam;
import wisematches.server.web.mail.MailSender;
import wisematches.server.web.mail.ToTeam;
import wisematches.server.web.modules.core.problems.ProblemsReport;
import wisematches.server.web.modules.core.problems.ProblemsReportDao;
import wisematches.server.web.rpc.GenericRemoteService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProblemsReportServiceImpl extends GenericRemoteService {//implements ProblemsReportService {
/*    private MailSender mailSender;
    private ProblemsReportDao problemsReportDao;

    private static final Logger log = Logger.getLogger(ProblemsReportServiceImpl.class);

    private EnumSet<ToTeam> notificationTeams;

    @Transactional
    public void reportProblem(String username, String email, String accountName,
                              OperationSystems os, Browser browser, String subject, String message) {
        ProblemsReport report = new ProblemsReport();
        report.setUsername(username);
        report.setAccount(accountName);
        report.setBrowser(browser);
        report.setOs(os);
        report.setEmail(email);
        report.setMessage(message);
        report.setSubject(subject);

        if (problemsReportDao != null) {
            problemsReportDao.signalProblem(report);
        }

        if (mailSender != null && notificationTeams != null) {
            try {
                final Map<String, Object> map = new HashMap<String, Object>();
                map.put("report", report);

                mailSender.sendSystemMail(FromTeam.BUG_REPORTER, notificationTeams,
                        "WiseMatches Problem Report", "/templates/mail/internal/problems_report.vm", map);
            } catch (Exception ex) {
                log.error("EMail notification about report " + report + " can't be sent", ex);
            }
        }
    }

    public void bugReport(Throwable throwable) {
        if (log.isInfoEnabled()) {
            log.info("Client bug received: " + throwable);
        }

        final StringWriter w = new StringWriter();
        throwable.printStackTrace(new PrintWriter(w));
        final String stackTrace = w.getBuffer().toString();

        if (mailSender != null && notificationTeams != null) {
            try {
                final Map<String, Object> map = new HashMap<String, Object>();
                map.put("stack-trace", stackTrace);

                mailSender.sendSystemMail(FromTeam.BUG_REPORTER, notificationTeams,
                        "WiseMatches Problem Report", "/templates/mail/internal/bug_report.vm", map);
            } catch (Exception ex) {
                log.error("EMail notification about bug report can't be sent: " + stackTrace, ex);
            }
        }
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setProblemsReportDao(ProblemsReportDao problemsReportDao) {
        this.problemsReportDao = problemsReportDao;
    }

    public void setEmailNotificationTeams(Set<ToTeam> notificationTeams) {
        this.notificationTeams = EnumSet.copyOf(notificationTeams);
    }*/
}