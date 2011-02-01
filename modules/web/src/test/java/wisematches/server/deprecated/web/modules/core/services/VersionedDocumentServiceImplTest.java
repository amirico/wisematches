package wisematches.server.deprecated.web.modules.core.services;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class VersionedDocumentServiceImplTest extends TestCase {
/*
    private HttpServletRequest request;
    private ServletContext servletContext;

    private VersionedDocumentServiceImpl documentService = new VersionedDocumentServiceImpl();

    private static final Calendar CALENDAR = Calendar.getInstance();

    @Override
    protected void setUp() throws Exception {
        final File markerFile = new File(getClass().getResource("marker.xml").getFile());
        assertTrue("Marker exists", markerFile.exists());

        servletContext = createMock(ServletContext.class);
        expect(servletContext.getRealPath(isA(String.class))).andAnswer(new IAnswer<String>() {
            public String answer() throws Throwable {
                String name = (String) getCurrentArguments()[0];
                return new File(markerFile.getParentFile(), name).getAbsolutePath();
            }
        }).anyTimes();
        replay(servletContext);

        documentService.setDocumentsPath("docs");

        request = createMock(HttpServletRequest.class);

        RemoteServiceContextAccessor.setRequest(request);

        CALENDAR.set(0, 0, 0, 0, 0, 0);
        CALENDAR.set(Calendar.MILLISECOND, 0);
    }

    @Override
    protected void tearDown() throws Exception {
        RemoteServiceContextAccessor.destroy();
    }

    public void testGetVersionedDocument_Errors() {
        expectLocale(Language.ENGLISH);
        assertNull(documentService.getVersionedDocument("doc1", "f1", "asd"));

        expectLocale(Language.ENGLISH);
        assertNull(documentService.getVersionedDocument("doc1", "f1/f2", "xml"));

        expectLocale(Language.ENGLISH);
        assertNull(documentService.getVersionedDocument("doc1", "f1", "properties"));
    }

    public void testGetVersionedDocument1() {
        expectLocale(Language.ENGLISH);

        final VersionedDocument versionedDocument = documentService.getVersionedDocument("doc1", "f1", "xml");
        assertEquals("doc1", versionedDocument.getDocumentName());
        assertEquals("f1", versionedDocument.getDocumentPath());
        assertEquals("xml", versionedDocument.getDocumentExtension());

        final DocumentRevision[] documentRevisions = versionedDocument.getRevisions();
        assertEquals(2, documentRevisions.length);

        CALENDAR.set(2008, 11 - 1, 9);
        assertEquals(CALENDAR.getTime(), documentRevisions[0].getUpdateDate());
        assertEquals("/docs/f1/doc1_09112008_en.xml", documentRevisions[0].getRevisionUrl());

        CALENDAR.set(2008, 11 - 1, 4);
        assertEquals(CALENDAR.getTime(), documentRevisions[1].getUpdateDate());
        assertEquals("/docs/f1/doc1_04112008_en.xml", documentRevisions[1].getRevisionUrl());
    }

    public void testGetVersionedDocument2() {
        //English locale
        expectLocale(Language.ENGLISH);

        VersionedDocument versionedDocument = documentService.getVersionedDocument("doc1", "f2/f3", "xml");
        assertEquals("doc1", versionedDocument.getDocumentName());
        assertEquals("f2/f3", versionedDocument.getDocumentPath());
        assertEquals("xml", versionedDocument.getDocumentExtension());

        DocumentRevision[] documentRevisions = versionedDocument.getRevisions();
        assertEquals(2, documentRevisions.length);

        CALENDAR.set(2008, 11 - 1, 10);
        assertEquals(CALENDAR.getTime(), documentRevisions[0].getUpdateDate());
        assertEquals("/docs/f2/f3/doc1_10112008_en.xml", documentRevisions[0].getRevisionUrl());

        CALENDAR.set(2008, 11 - 1, 5);
        assertEquals(CALENDAR.getTime(), documentRevisions[1].getUpdateDate());
        assertEquals("/docs/f2/f3/doc1_05112008_en.xml", documentRevisions[1].getRevisionUrl());

        //Russian locale
        expectLocale(Language.RUSSIAN);
        versionedDocument = documentService.getVersionedDocument("doc1", "f2/f3", "xml");
        assertEquals("doc1", versionedDocument.getDocumentName());
        assertEquals("f2/f3", versionedDocument.getDocumentPath());
        assertEquals("xml", versionedDocument.getDocumentExtension());

        documentRevisions = versionedDocument.getRevisions();
        assertEquals(2, documentRevisions.length);

        CALENDAR.set(2008, 11 - 1, 10);
        assertEquals(CALENDAR.getTime(), documentRevisions[0].getUpdateDate());
        assertEquals("/docs/f2/f3/doc1_10112008_ru.xml", documentRevisions[0].getRevisionUrl());

        CALENDAR.set(2008, 11 - 1, 5);
        assertEquals(CALENDAR.getTime(), documentRevisions[1].getUpdateDate());
        assertEquals("/docs/f2/f3/doc1_05112008_ru.xml", documentRevisions[1].getRevisionUrl());
    }

    public void testGetVersionedDocument3() {
        expectLocale(Language.ENGLISH);

        final VersionedDocument versionedDocument = documentService.getVersionedDocument("doc2", "f1", "xml");
        assertEquals("doc2", versionedDocument.getDocumentName());
        assertEquals("f1", versionedDocument.getDocumentPath());
        assertEquals("xml", versionedDocument.getDocumentExtension());

        final DocumentRevision[] documentRevisions = versionedDocument.getRevisions();
        assertEquals(1, documentRevisions.length);

        CALENDAR.set(2008, 11 - 1, 4);
        assertEquals(CALENDAR.getTime(), documentRevisions[0].getUpdateDate());
        assertEquals("/docs/f1/doc2_04112008_en.xml", documentRevisions[0].getRevisionUrl());
    }

    public void testGetVersionedDocument4() {
        expectLocale(Language.ENGLISH);

        final VersionedDocument versionedDocument = documentService.getVersionedDocument("doc2", "f1", "xml");
        assertEquals("doc2", versionedDocument.getDocumentName());
        assertEquals("f1", versionedDocument.getDocumentPath());
        assertEquals("xml", versionedDocument.getDocumentExtension());

        final DocumentRevision[] documentRevisions = versionedDocument.getRevisions();
        assertEquals(1, documentRevisions.length);

        CALENDAR.set(2008, 11 - 1, 4);
        assertEquals(CALENDAR.getTime(), documentRevisions[0].getUpdateDate());
        assertEquals("/docs/f1/doc2_04112008_en.xml", documentRevisions[0].getRevisionUrl());
    }

    private void expectLocale(Language l) {
        HttpSession s = createMock(HttpSession.class);
        expect(s.getServletContext()).andReturn(servletContext);
        replay(s);

        reset(request);
        expect(request.getSession()).andReturn(s);
        expect(request.getAttribute(Language.class.getSimpleName())).andReturn(l);
        replay(request);
    }
*/
}
