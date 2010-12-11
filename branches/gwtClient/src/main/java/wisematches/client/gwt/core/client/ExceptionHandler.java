package wisematches.client.gwt.core.client;

import com.google.gwt.core.client.GWT;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ExceptionHandler implements GWT.UncaughtExceptionHandler {
//    private long currentPlayerId;
//    private final GWT.UncaughtExceptionHandler defaultExceptionHandler;
//
//    private static final Collection<ExceptionHandlerListener> exceptionHandlerListeners = new HashSet<ExceptionHandlerListener>();
//
//    private static final DateTimeFormat DATE_TIME_FORMAT = DateTimeFormat.getMediumDateTimeFormat();
//
//    static {
//        //Loading ProblemsReportWindow class to unitialize static initialization block.
//        final Class<ProblemsReportWindow> reportWindowClass = ProblemsReportWindow.class;
//    }
//
//    public ExceptionHandler() {
//        this.defaultExceptionHandler = GWT.getUncaughtExceptionHandler();
//    }
//
//    public static void addExceptionHandlerListener(ExceptionHandlerListener listener) {
//        exceptionHandlerListeners.add(listener);
//    }
//
//    public static void removeExceptionHandlerListener(ExceptionHandlerListener listener) {
//        exceptionHandlerListeners.remove(listener);
//    }
//
//    public void setCurrentPlayerId(long currentPlayerId) {
//        this.currentPlayerId = currentPlayerId;
//    }
//
//    public void onUncaughtException(Throwable throwable) {
//        showSystemError(throwable);
//    }
//
//    private static void fireSessionExpired() {
//        for (ExceptionHandlerListener listener : exceptionHandlerListeners) {
//            listener.applicationSessionExpired();
//        }
//    }
//
//    public static void showSystemError(Throwable throwable) {
//        try {
//            throw throwable;
//        } catch (PlayerSecurityException ex) {
//            processSecureException(ex);
//        } catch (IncompatibleRemoteServiceException ex) {
//            fireSessionExpired();
//
//            MessageBox.alert(MCOMMON.tltSessionExpired(), MCOMMON.msgSessionExpired(), new MessageBox.AlertCallback() {
//                public void execute() {
//                    Window.Location.reload();
//                }
//            });
//        } catch (StatusCodeException ex) {
//            processStatusCodeException(ex);
//        } catch (Throwable th) {
//            ExceptionReportWindow.showException(th);
//        }
//    }
//
//    private static void processStatusCodeException(StatusCodeException exception) {
//        final int statusCode = exception.getStatusCode();
//
//        switch (statusCode) {
//            case 500:
//                MessageBox.alert(MCOMMON.errorStatusCodeTitle(), MCOMMON.errorStatusCode500() + " " + MCOMMON.lblContactWithAdministrator());
//                break;
//            case 501:
//                MessageBox.alert(MCOMMON.errorStatusCodeTitle(), MCOMMON.errorStatusCode501() + " " + MCOMMON.lblContactWithAdministrator());
//                break;
//            case 502:
//                MessageBox.alert(MCOMMON.errorStatusCodeTitle(), MCOMMON.errorStatusCode502() + " " + MCOMMON.lblContactWithAdministrator());
//                break;
//            case 503:
//                MessageBox.alert(MCOMMON.errorStatusCodeTitle(), MCOMMON.errorStatusCode503() + " " + MCOMMON.lblContactWithAdministrator());
//                break;
//            default:
//                MessageBox.alert(MCOMMON.errorStatusCodeTitle(), MCOMMON.errorStatusCode(statusCode) + " " + MCOMMON.lblContactWithAdministrator());
//        }
//    }
//
//    private static void processSecureException(PlayerSecurityException exception) {
//        try {
//            throw exception;
//        } catch (PlayerLockedException ex) {
//            if (ex.getUnlockDate() != null) {
//                MessageBox.alert(MCOMMON.tltAccountLocked(),
//                        MCOMMON.msgAccountLocked(ex.getReason(), DATE_TIME_FORMAT.format(ex.getUnlockDate())));
//            } else {
//                MessageBox.alert(MCOMMON.tltAccountLocked(), MCOMMON.msgAccountLockedNever(ex.getReason()));
//            }
//        } catch (PlayerSessionException ex) {
//            fireSessionExpired();
//
//            MessageBox.alert(MCOMMON.tltSessionExpired(), MCOMMON.msgSessionExpired(), new MessageBox.AlertCallback() {
//                public void execute() {
//                    Window.Location.reload();
//                }
//            });
//        } catch (GuestRestrictionException ex) {
//            MessageBox.alert(MCOMMON.tltGuestRestrictionException(), MCOMMON.msgGuestRestrictionException());
//        } catch (PlayerRestrictionException ex) {
//            MessageBox.alert(MCOMMON.tltRestrictionException(), MCOMMON.msgRestrictionException());
//        } catch (PlayerSecurityException ex) {
//            ExceptionReportWindow.showException(ex);
//        }
//    }

	@Override
	public void onUncaughtException(Throwable throwable) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
