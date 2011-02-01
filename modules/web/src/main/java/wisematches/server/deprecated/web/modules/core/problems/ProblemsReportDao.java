package wisematches.server.deprecated.web.modules.core.problems;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ProblemsReportDao extends HibernateDaoSupport {
	public void signalProblem(ProblemsReport report) {
		final HibernateTemplate template = getHibernateTemplate();
		template.save(report);
		template.flush();
	}

	public ProblemsReport getProblemReport(long id) {
		return (ProblemsReport) getHibernateTemplate().get(ProblemsReport.class, id);
	}

	public void deleteProblemReport(ProblemsReport report) {
		final HibernateTemplate template = getHibernateTemplate();
		template.delete(report);
		template.flush();
	}
}
