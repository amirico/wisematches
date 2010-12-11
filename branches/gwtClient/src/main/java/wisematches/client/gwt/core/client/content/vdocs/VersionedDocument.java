package wisematches.client.gwt.core.client.content.vdocs;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Arrays;

/**
 * <code>VersionedDocument</code> is structure that contains path to versioned files, name of document
 * and collection of revisions.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class VersionedDocument implements IsSerializable {
    private String documentName;
    private String documentPath;
    private String documentExtension;
    private DocumentRevision[] revisions;

    /**
     * Creates new empty versioned document.
     * <p/>
     * <b>WARNING:</b> this constructor is used only for GWT serialization. Please don't use it.
     *
     * @see #VersionedDocument(String, String, String, DocumentRevision[])
     */
    public VersionedDocument() {
    }

    /**
     * Creates new document info with specified parameters.
     * <p/>
     * Each document has three parameters: <i>name</i>, <i>path</i> and <i>extension</i>.
     *
     * @param documentName      the name of the document without extension and path.
     * @param documentPath      the path to all versioned documents.
     * @param documentExtension the extension of documents.
     * @param revisions         the array of all revisions of this document.
     */
    public VersionedDocument(String documentName, String documentPath, String documentExtension,
                             DocumentRevision[] revisions) {
        this.documentName = documentName;
        this.documentPath = documentPath;
        this.documentExtension = documentExtension;
        this.revisions = revisions;

        Arrays.sort(this.revisions);
        for (DocumentRevision revision : this.revisions) {
            revision.setDocument(this);
        }
    }

    /**
     * Returns name of the document.
     *
     * @return the name of the document.
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * Returns relative path to the document.
     *
     * @return the path to the document.
     */
    public String getDocumentPath() {
        return documentPath;
    }

    /**
     * Returns extension of the document.
     *
     * @return the extension of the document.
     */
    public String getDocumentExtension() {
        return documentExtension;
    }

    /**
     * Returns copy of revisions.
     *
     * @return the copy of original revisiones array.
     */
    public DocumentRevision[] getRevisions() {
        return revisions;
    }
}