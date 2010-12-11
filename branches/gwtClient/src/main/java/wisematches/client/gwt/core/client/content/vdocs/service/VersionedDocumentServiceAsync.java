package wisematches.client.gwt.core.client.content.vdocs.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import wisematches.client.gwt.core.client.content.vdocs.VersionedDocument;

public interface VersionedDocumentServiceAsync {
    void getVersionedDocument(String documentName, String documentPath, String documentExtension, AsyncCallback<VersionedDocument> async);
}
