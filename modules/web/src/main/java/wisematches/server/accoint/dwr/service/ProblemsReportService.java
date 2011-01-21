/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.accoint.dwr.service;

import wisematches.server.accoint.dwr.bean.ProblemsReportForm;
import wisematches.server.web.controllers.ServiceResponse;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProblemsReportService {
	public ServiceResponse reportProblem(ProblemsReportForm report) {
		System.out.println(report);
		if (report.getEmail().startsWith("test")) {
			return ServiceResponse.failure("adasdasdadad", "email", "asd.qwe.asdf.wer");
		}
		return ServiceResponse.SUCCESS;
	}
}
