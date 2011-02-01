package wisematches.server.deprecated.web.rpc;

/**
 * This is implementation of Spring {@code HandlerAdapter} which also is GWT {@code RemoteServiceServlet}.
 * <p/>
 * Handler receives invocation from GWT client and retransmit it to appropriate bean.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RemoteServiceHandlerAdapter { //extends RemoteServiceServlet implements HandlerAdapter {
/*
    private PlayerSessionsManager playerSessionsManager;

    private final ThreadLocal<RemoteService> handlerService = new ThreadLocal<RemoteService>();

    private static final RemoteServiceContext remoteServiceContext = new RemoteServiceContext();

    private static final Log log = LogFactory.getLog("wisematches.server.deprecated.web.rpc.handler");

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof RemoteService);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final RemoteService remoteService = (RemoteService) handler;
        try {
            handlerService.set(remoteService);
            remoteServiceContext.initSimpleSession(request, response);

            doPost(request, response);
        } finally {
            handlerService.set(null);
            remoteServiceContext.destroySession();
        }
        return null;
    }

    @Override
    public String processCall(String payload) throws SerializationException {
        final RemoteService remoteService = handlerService.get();
        // This code taken from source code of GWT 1.6.4 but 'this' replaced to RemoteService
        try {
            // First check security
            initSecurityRequest(remoteService);

            final RPCRequest rpcRequest = RPC.decodeRequest(payload, remoteService.getClass(), this);
            onAfterRequestDeserialized(rpcRequest);
            return RPC.invokeAndEncodeResponse(remoteService, rpcRequest.getMethod(), rpcRequest.getParameters(), rpcRequest.getSerializationPolicy());
        } catch (IncompatibleRemoteServiceException ex) {
            log.error("An IncompatibleRemoteServiceException was thrown while processing this call: " + remoteService, ex);
            log("An IncompatibleRemoteServiceException was thrown while processing this call.", ex);
            return RPC.encodeResponseForFailure(null, ex);
        } catch (GWTRuntimeException ex) {
            return RPC.encodeResponseForFailure(null, ex);
        }
    }

    private void initSecurityRequest(RemoteService remoteService) throws PlayerSessionException {
        if (remoteService instanceof SecureRemoteService) {
            final HttpServletRequest request = remoteServiceContext.getRequest();
            final HttpServletResponse response = remoteServiceContext.getResponse();

            final PlayerSessionBean bean = getPlayerSessionBean(request);
            remoteServiceContext.initSecureSession(request, response, bean);
        }
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return -1;
    }


    private PlayerSessionBean getPlayerSessionBean(HttpServletRequest request) throws PlayerSessionException {
        final Language locale = WebUtils.getRequestAttrbiute(request, Language.class);
        if (request == null) {
            throw new PlayerSessionException(ResourceManager.getString("app.session.expired", locale));
        }
        final HttpSession session = request.getSession();
        final PlayerSessionBean bean = playerSessionsManager.getPlayerSessionBean(session.getId());
        if (bean == null) {
            throw new PlayerSessionException(ResourceManager.getString("app.session.expired", locale));
        }
        return bean;
    }

    @Override
    public ServletContext getServletContext() {
        final HttpSession session = remoteServiceContext.getRequest().getSession();
        return session.getServletContext();
    }

    @Override
    public String getServletName() {
        return getServletContext().getServletContextName();
    }

    @Override
    public String getServletInfo() {
        return getServletContext().getServerInfo();
    }

    @Override
    protected void doUnexpectedFailure(Throwable throwable) {
        log.fatal("Fatal error during process client RPC call", throwable);
    }


    public void setPlayerSessionsManager(PlayerSessionsManager playerSessionsManager) {
        this.playerSessionsManager = playerSessionsManager;
    }

    static RemoteServiceContext getRemoteServiceContext() {
        return remoteServiceContext;
    }
*/
}
