package wisematches.server.web.rpc;

import wisematches.kernel.util.Language;
import wisematches.server.web.util.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GenericRemoteService implements RemoteService {
/*
    protected ServletContext getServletContext() {
        return getRequest().getSession().getServletContext();
    }

    protected Language getLanguage() {
        return WebUtils.getRequestAttrbiute(getRequest(), Language.class);
    }

    protected HttpServletRequest getRequest() {
        return RemoteServiceHandlerAdapter.getRemoteServiceContext().getRequest();
    }

    protected HttpServletResponse getResponse() {
        return RemoteServiceHandlerAdapter.getRemoteServiceContext().getResponse();
    }
*/
}
