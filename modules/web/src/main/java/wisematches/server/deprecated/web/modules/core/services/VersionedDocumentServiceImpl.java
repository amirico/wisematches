package wisematches.server.deprecated.web.modules.core.services;

import wisematches.server.deprecated.web.rpc.GenericRemoteService;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class VersionedDocumentServiceImpl extends GenericRemoteService {//implements VersionedDocumentService {
/*    private String documentsPath;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("ddMMyyyy");

    private static final Logger log = Logger.getLogger(VersionedDocumentServiceImpl.class);

    public VersionedDocument getVersionedDocument(String documentName, String documentPath, String documentExtension) {
        if (log.isDebugEnabled()) {
            log.debug("Get versioned document from " + documentsPath + ": path " + documentPath + ", name " +
                    documentName + ", extension " + documentExtension);
        }


        final String relativeFolderPath = documentsPath + '/' + documentPath;
        final File absoluteFolderPath = new File(getServletContext().getRealPath(relativeFolderPath));
        if (!absoluteFolderPath.exists() || !absoluteFolderPath.isDirectory()) {
            if (log.isInfoEnabled()) {
                log.info("Path " + documentPath + " of requested document " + documentName + "." +
                        documentExtension + " not found. File " +
                        absoluteFolderPath.getAbsolutePath() + " not exist or not a directory.");
            }
            return null;
        }

        final Language locale = getLanguage();
        final TheFilenameFilter filenameFilter = new TheFilenameFilter(documentName, documentExtension, locale);
        final File[] files = absoluteFolderPath.listFiles(filenameFilter);
        if (files.length == 0) {
            if (log.isInfoEnabled()) {
                log.info("No versioned files found for document '" + documentName + "' with path '" +
                        documentPath + "' and extension '" + documentExtension +
                        "' for locale '" + locale + "'. Absolute searching path: " + absoluteFolderPath);
            }
            return null;
        }

        final DocumentRevision[] revisions = new DocumentRevision[files.length];
        for (int i = 0; i < files.length; i++) {
            final File file = files[i];
            final Date date;
            try {
                date = filenameFilter.extractDate(file.getName());
            } catch (Exception e) { // ParseException or NumberFormatException here...
                log.error("Versioned document has incorrect date format: " + file.getAbsolutePath(), e);
                return null;
            }

            String url = relativeFolderPath + '/' + file.getName();
            if (url.charAt(0) != '/') {
                url = '/' + url;
            }
            revisions[i] = new DocumentRevision(date, url);
        }
        return new VersionedDocument(documentName, documentPath, documentExtension, revisions);
    }

    public void setDocumentsPath(String documentsPath) {
        this.documentsPath = documentsPath;
    }

    public String getDocumentsPath() {
        return documentsPath;
    }

    private static class TheFilenameFilter implements FilenameFilter {
        private final String prefix;
        private final String postfix;

        private TheFilenameFilter(String prefix, String postfix, Language locale) {
            this.prefix = prefix;
            this.postfix = "_" + locale.code() + "." + postfix;
        }

        public boolean accept(File dir, String name) {
            return name.startsWith(prefix) && name.endsWith(postfix);
        }

        public Date extractDate(String name) throws ParseException {
            final String maybeDate = name.substring(prefix.length() + 1, name.length() - postfix.length());
            return DATE_FORMAT.parse(maybeDate);
        }
    }*/
}