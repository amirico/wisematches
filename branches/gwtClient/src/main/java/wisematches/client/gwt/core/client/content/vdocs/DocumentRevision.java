package wisematches.client.gwt.core.client.content.vdocs;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Revision of <code>VerionedDocument</code>. Contains date of this revision and relative path to revision
 * of the document.
 * <p/>
 * <code>DocumentRevision</code> is comparable. One revision is less than other if it's date is after the other's date.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class DocumentRevision implements IsSerializable, Comparable<DocumentRevision> {
    private VersionedDocument document;
    private String revisionUrl;
    private Date updateDate;

    /**
     * Crates new empty revision.
     * <p/>
     * <b>WARNING:</b> this constructor is used only for GWT serialization. Please don't use it.
     *
     * @see #DocumentRevision(java.util.Date, String)
     */
    public DocumentRevision() {
    }

    /**
     * Creates new revision with specfied date and relative url to document
     *
     * @param updateDate  the update date.
     * @param revisionUrl the relative url to document.
     */
    public DocumentRevision(Date updateDate, String revisionUrl) {
        this.updateDate = updateDate;
        this.revisionUrl = revisionUrl;
    }

    /**
     * Returns document that contains this resivion.
     *
     * @return the document
     */
    public VersionedDocument getDocument() {
        return document;
    }

    /**
     * Changes document for this revision. This is package visible method and invoked only
     * from <code>VersionedDocument</code> class during it's construction.
     *
     * @param document the document that contains this revision.
     */
    void setDocument(VersionedDocument document) {
        this.document = document;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getRevisionUrl() {
        return revisionUrl;
    }

    public int compareTo(DocumentRevision o) {
        return -updateDate.compareTo(o.updateDate);
    }
}