package wisematches.server.deprecated.web.rpc;

public class RemoteServiceContextAccessor {
/*
    public static void setRequest(HttpServletRequest request) {
        final RemoteServiceContext context = RemoteServiceHandlerAdapter.getRemoteServiceContext();
        context.initSecureSession(request, context.getResponse(), context.getSessionBean());
    }

    public static void setResponse(HttpServletResponse response) {
        final RemoteServiceContext context = RemoteServiceHandlerAdapter.getRemoteServiceContext();
        context.initSecureSession(context.getRequest(), response, context.getSessionBean());
    }

    public static void setSessionBean(PlayerSessionBean sessionBean) {
        final RemoteServiceContext context = RemoteServiceHandlerAdapter.getRemoteServiceContext();
        context.initSecureSession(context.getRequest(), context.getResponse(), sessionBean);
    }

    public static void destroy() {
        final RemoteServiceContext context = RemoteServiceHandlerAdapter.getRemoteServiceContext();
        context.destroySession();
    }

    public static PlayerSessionBean expectPlayerSessionBean() {
        return expectPlayerSessionBean(PlayerSessionBean.class);
    }

    public static <T extends PlayerSessionBean> T expectPlayerSessionBean(Class<T> type) {
        final T bean = createStrictMock(type);
        setSessionBean(bean);
        return bean;
    }

    public static void expectPlayer(Player player) {
        final PlayerSessionBean bean = expectPlayerSessionBean();
        expect(bean.getPlayer()).andReturn(player);
        replay(bean);
    }
*/
}
