package wisematches.server.deprecated.web.modules.app.images.impl;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class FilePlayerImagesManagerTest {
/*
    private File testFolder;
    private File imagesFolder;

    @Before
    public void init() throws IOException {
        testFolder = new File(System.getProperty("java.io.tmpdir"), "wisematches");
        testFolder.deleteOnExit();

        imagesFolder = new File(testFolder, "tests/images/player/mock");
        imagesFolder.deleteOnExit();
    }

    @Test
    public void test_pngImage() throws IOException, UnsupportedImageException {
        doImageTest(new File(getClass().getResource("testImage.png").getFile()));
    }

    @Test
    public void test_gifImage() throws IOException, UnsupportedImageException {
        doImageTest(new File(getClass().getResource("testImage.gif").getFile()));
    }

    @Test
    public void test_jpgImage() throws IOException, UnsupportedImageException {
        doImageTest(new File(getClass().getResource("testImage.jpg").getFile()));
    }

    @Test
    public void test_unknownImage() throws IOException, UnsupportedImageException {
        try {
            doImageTest(new File(getClass().getResource("testImage.xml").getFile()));
            fail("Exception must be here: UnsupportedImageException");
        } catch (UnsupportedImageException ex) {
            ;
        }
    }

    private void doImageTest(final File image) throws IOException, UnsupportedImageException {
        final PlayerImagesListener playerImagesListener = createStrictMock(PlayerImagesListener.class);
        playerImagesListener.playerImageAdded(1L, PlayerImageType.REAL);
        playerImagesListener.playerImageUpdated(1L, PlayerImageType.REAL);
        playerImagesListener.playerImageAdded(1L, PlayerImageType.AVATAR);
        playerImagesListener.playerImageRemoved(1L, PlayerImageType.AVATAR);
        playerImagesListener.playerImageRemoved(1L, PlayerImageType.REAL);
        replay(playerImagesListener);

        final FilePlayerImagesManager manager = new FilePlayerImagesManager();
        manager.setImagesFolder(new FileSystemResource(imagesFolder));
        manager.addPlayerImagesListener(playerImagesListener);

        InputStream ir = manager.getPlayerImage(1L, PlayerImageType.REAL);
        InputStream iv = manager.getPlayerImage(1L, PlayerImageType.AVATAR);
        assertNull(ir);
        assertNull(iv);

        manager.setPlayerImage(1L, new FileInputStream(image), PlayerImageType.REAL);
        ir = manager.getPlayerImage(1L, PlayerImageType.REAL);
        iv = manager.getPlayerImage(1L, PlayerImageType.AVATAR);
        try {
            assertNotNull(ir);
            assertNull(iv);
        } finally {
            ir.close();
        }

        manager.setPlayerImage(1L, new FileInputStream(image), PlayerImageType.REAL); //Update player image
        ir = manager.getPlayerImage(1L, PlayerImageType.REAL);
        iv = manager.getPlayerImage(1L, PlayerImageType.AVATAR);
        try {
            assertNotNull(ir);
            assertNull(iv);
        } finally {
            ir.close();
        }

        manager.setPlayerImage(1L, new FileInputStream(image), PlayerImageType.AVATAR);
        ir = manager.getPlayerImage(1L, PlayerImageType.REAL);
        iv = manager.getPlayerImage(1L, PlayerImageType.AVATAR);
        try {
            assertNotNull(ir);
            assertNotNull(iv);

            final BufferedImage bufferedImage = ImageIO.read(iv);
            assertEquals(PlayerImageType.AVATAR.getWidth(), bufferedImage.getWidth());
            assertEquals(PlayerImageType.AVATAR.getHeight(), bufferedImage.getHeight());
        } finally {
            ir.close();
            iv.close();
        }

        manager.removePlayerImage(1L, PlayerImageType.AVATAR);
        assertNull(manager.getPlayerImage(1L, PlayerImageType.AVATAR));

        manager.removePlayerImage(1L, PlayerImageType.REAL);
        assertNull(manager.getPlayerImage(1L, PlayerImageType.REAL));

        verify(playerImagesListener);
    }

    @After
    public void thearDown() throws IOException {
        FileUtils.deleteDirectory(testFolder);
    }
*/
}
