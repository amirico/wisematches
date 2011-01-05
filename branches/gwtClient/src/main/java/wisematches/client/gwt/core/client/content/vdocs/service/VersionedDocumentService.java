package wisematches.client.gwt.core.client.content.vdocs.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import wisematches.client.gwt.core.client.content.vdocs.VersionedDocument;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface VersionedDocumentService extends RemoteService {
    VersionedDocument getVersionedDocument(String documentName, String documentPath, String documentExtension);

    /**
     * Utility/Convenience class.
     * Use VersionedDocumentService.App.getInstance() to access static instance of VersionedDocumentServiceAsync
     */
    public static class App {
        private static VersionedDocumentServiceAsync ourInstance = null;

        public static synchronized VersionedDocumentServiceAsync getInstance() {
            if (ourInstance == null) {
                ourInstance = (VersionedDocumentServiceAsync) GWT.create(VersionedDocumentService.class);
                ((ServiceDefTarget) ourInstance).setServiceEntryPoint("/rpc/VersionedDocumentService");
            }
            return ourInstance;
        }
    }
}
