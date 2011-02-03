package wisematches.server.deprecated.web.modules.core.problems;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ProblemsReportDaoTest extends AbstractTransactionalDataSourceSpringContextTests {
/*
    private ProblemsReportDao problemsReportDao;

    protected String[] getConfigLocations() {
        return new String[]{"classpath:/config/test-application-settings.xml"};
    }

    public void test() throws InterruptedException {
        int count = jdbcTemplate.queryForInt("select count(*) from rp_problems");

        ProblemsReportForm report = new ProblemsReportForm();
        report.setNickname("username");
        report.setAccount("account");
        report.setSubject("subject");
        report.setMessage("Message");
        report.setEmail("email");
        report.setBrowser(Browser.EXPLORER_6);
        report.setOs(OperationSystems.INTEL_MAC_OS);

        problemsReportDao.signalProblem(report);

        int newCount = jdbcTemplate.queryForInt("select count(*) from rp_problems");
        assertEquals(count + 1, newCount);

        final ProblemsReportForm problemsReport = problemsReportDao.getProblemReport(report.getId());
        assertNotNull(problemsReport);

        problemsReportDao.deleteProblemReport(report);

        int newCount2 = jdbcTemplate.queryForInt("select count(*) from rp_problems");
        assertEquals(count, newCount2);
    }

    public void test2() {
        int count = jdbcTemplate.queryForInt("select count(*) from rp_problems");

        ProblemsReportForm report = new ProblemsReportForm();
        report.setNickname("username");
        report.setAccount("account");
        report.setSubject("subject");
        report.setMessage("Message");
        report.setEmail("email");
        report.setBrowser(null);
        report.setOs(null);

        problemsReportDao.signalProblem(report);

        int newCount = jdbcTemplate.queryForInt("select count(*) from rp_problems");
        assertEquals(count + 1, newCount);

        final ProblemsReportForm problemsReport = problemsReportDao.getProblemReport(report.getId());
        assertNotNull(problemsReport);

        problemsReportDao.deleteProblemReport(report);

        int newCount2 = jdbcTemplate.queryForInt("select count(*) from rp_problems");
        assertEquals(count, newCount2);
    }

    public ProblemsReportDao getProblemsReportDao() {
        return problemsReportDao;
    }

    public void setProblemsReportDao(ProblemsReportDao problemsReportDao) {
        this.problemsReportDao = problemsReportDao;
    }
*/
}
