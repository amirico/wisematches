package wisematches.server.web.modules.core.services;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ProblemsReportServiceImplTest extends TestCase {
	/*private ProblemsReportForm report;
	 private Map parametersMap;

	 public void testReportProblem() {
		 ProblemsReportDao dao = createStrictMock(ProblemsReportDao.class);
		 dao.signalProblem(isA(ProblemsReportForm.class));
		 expectLastCall().andAnswer(new IAnswer<Object>() {
			 public Object answer() throws Throwable {
				 report = (ProblemsReportForm) getCurrentArguments()[0];
				 return null;
			 }
		 });
		 replay(dao);

		 ProblemsReportServiceImpl service = new ProblemsReportServiceImpl();
		 service.setProblemsReportDao(dao);

		 service.reportProblem("username", "email", "account",
				 OperationSystems.POWER_MAC_OS, Browser.FIREFOX_2, "subject", "message");

		 assertNotNull(report);
		 assertEquals("username", report.getUsername());
		 assertEquals("email", report.getEmail());
		 assertEquals("account", report.getAccount());
		 assertEquals(OperationSystems.POWER_MAC_OS, report.getOs());
		 assertEquals(Browser.FIREFOX_2, report.getBrowser());
		 assertEquals("subject", report.getSubject());
		 assertEquals("message", report.getMessage());

		 verify(dao);
	 }

	 public void testReportProblem_withMail() throws MessagingException, UnsupportedEncodingException {
		 final String template = "/templates/mail/internal/problems_report.vm";

		 assertNotNull(getClass().getResource(template));

		 ProblemsReportDao dao = createStrictMock(ProblemsReportDao.class);
		 dao.signalProblem(isA(ProblemsReportForm.class));
		 replay(dao);

		 MailSender mailSender = createStrictMock(MailSender.class);
		 mailSender.sendSystemMail(eq(FromTeam.BUG_REPORTER), eq(EnumSet.of(ToTeam.BUGS_TEAM)),
				 eq("WiseMatches Problem Report"),
				 eq(template),
				 isA(Map.class));
		 expectLastCall().andAnswer(new IAnswer<Object>() {
			 public Object answer() throws Throwable {
				 parametersMap = (Map) getCurrentArguments()[4];
				 return null;
			 }
		 });
		 replay(mailSender);

		 ProblemsReportServiceImpl service = new ProblemsReportServiceImpl();
		 service.setProblemsReportDao(dao);
		 service.setMailSender(mailSender);
		 service.setEmailNotificationTeams(EnumSet.of(ToTeam.BUGS_TEAM));

		 service.reportProblem("username", "email", "account",
				 OperationSystems.POWER_MAC_OS, Browser.FIREFOX_2, "subject", "message");

		 verify(mailSender);

		 assertEquals(ProblemsReportForm.class, parametersMap.get("report").getClass());
	 }
 */
}
