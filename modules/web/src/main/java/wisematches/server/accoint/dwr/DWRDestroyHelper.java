/*
 * Copyright (c) 2011, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.accoint.dwr;

import org.directwebremoting.util.SharedObjects;
import org.springframework.beans.factory.DisposableBean;

/**
 * TODO: java docs
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DWRDestroyHelper implements DisposableBean {
	@Override
	public void destroy() throws Exception {
		SharedObjects.getScheduledThreadPoolExecutor().shutdown();
	}
}
