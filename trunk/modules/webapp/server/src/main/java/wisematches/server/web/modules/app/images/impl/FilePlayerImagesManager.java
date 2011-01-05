package wisematches.server.web.modules.app.images.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import wisematches.server.web.modules.app.images.PlayerImageType;
import wisematches.server.web.modules.app.images.PlayerImagesListener;
import wisematches.server.web.modules.app.images.PlayerImagesManager;
import wisematches.server.web.modules.app.images.UnsupportedImageException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Realization of {@code PlayerImagesManager} that saves images into files.
 * <p/>
 * A name of the file is composed by following rule: {@code <PlayerID>_<ImageType>.image} where:
 * {@code <PlayerID>} is player id, {@code <ImageType>} the image type (see {@code PlayerImageType}).
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 * @see PlayerImageType
 */
public class FilePlayerImagesManager implements PlayerImagesManager {
    private Resource imagesFolder;

    private final Collection<PlayerImagesListener> listeners = new CopyOnWriteArraySet<PlayerImagesListener>();

    private static final Log log = LogFactory.getLog(FilePlayerImagesManager.class);

    public FilePlayerImagesManager() {
    }

    public void addPlayerImagesListener(PlayerImagesListener l) {
        listeners.add(l);
    }

    public void removePlayerImagesListener(PlayerImagesListener l) {
        listeners.remove(l);
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getPlayerImage(long playerId, PlayerImageType type) {
        if (type == null) {
            throw new NullPointerException("Type can't be null");
        }

        try {
            final File file = new File(imagesFolder.getFile(), getImageFilename(playerId, type));
            if (!file.exists()) {
                return null;
            }

            return new FileInputStream(file);
        } catch (IOException e) {
            log.warn("We checked that file exist but 'FileNotFoundException' exception received", e);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removePlayerImage(long playerId, PlayerImageType type) {
        if (type == null) {
            throw new NullPointerException("Type can't be null");
        }

        try {
            final File f = new File(imagesFolder.getFile(), getImageFilename(playerId, type));
            if (f.exists()) {
                if (f.delete()) {
                    for (PlayerImagesListener listener : listeners) {
                        listener.playerImageRemoved(playerId, type);
                    }
                } else {
                    log.warn("Player image can't be deleted by system error");
                }
            }
        } catch (IOException ex) {
            log.error("Image file can't be deleted", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setPlayerImage(long playerId, InputStream stream, PlayerImageType type)
            throws UnsupportedImageException, IOException {
        if (stream == null) {
            throw new NullPointerException("Stream can't be null");
        }
        if (type == null) {
            throw new NullPointerException("Type can't be null");
        }

        final File f = new File(imagesFolder.getFile(), getImageFilename(playerId, type));
        if (log.isDebugEnabled()) {
            log.debug("Update player image file: " + f + " (" + f.getAbsolutePath() + ")");
        }

        boolean added = false;
        if (!f.exists()) {
            if (!f.createNewFile()) {
                throw new IOException("Image file " + f + " can't be created");
            }
            added = true;
        }

        if (log.isDebugEnabled()) {
            log.debug("Update image for player " + playerId + " of type " + type);
        }
        final ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        final ImageInputStream iis = ImageIO.createImageInputStream(stream);
        try {
            type.scaleImage(iis, ios);

            if (added) {
                for (PlayerImagesListener listener : listeners) {
                    listener.playerImageAdded(playerId, type);
                }
            } else {
                for (PlayerImagesListener listener : listeners) {
                    listener.playerImageUpdated(playerId, type);
                }
            }
        } finally {
            ios.close();
            iis.close();
        }
    }

    private String getImageFilename(long playerId, PlayerImageType type) {
        return playerId + "_" + type.name() + ".image";
    }

    public void setImagesFolder(Resource imagesFolder) throws IOException {
        this.imagesFolder = imagesFolder;

        if (log.isDebugEnabled()) {
            log.debug("Change images folder to " + imagesFolder + ". Absolute path: " + imagesFolder.getFile().getAbsolutePath());
        }
        if (imagesFolder != null && !imagesFolder.exists()) {
            if (imagesFolder.getFile().mkdirs()) {
                if (log.isDebugEnabled()) {
                    log.debug("images folder created");
                }
            } else {
                log.warn("Images folder can't be created by system error");
            }
        }
    }
}
