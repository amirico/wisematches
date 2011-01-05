package wisematches.server.web.modules.app.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import wisematches.kernel.player.Player;
import wisematches.kernel.util.Language;
import wisematches.server.core.sessions.PlayerSessionBean;
import wisematches.server.core.sessions.PlayerSessionsManager;
import wisematches.server.web.ResourceManager;
import wisematches.server.web.modules.app.images.PlayerImageType;
import wisematches.server.web.modules.app.images.PlayerImagesManager;
import wisematches.server.web.modules.app.images.UnsupportedImageException;
import wisematches.server.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * TODO: there is no Unit test for this class
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerImagesServiceImpl {//implements Controller {
/*
    private Resource noImageImageUrl;

    private PlayerImagesManager playerImagesManager;
    private PlayerSessionsManager playerSessionsManager;

    private static final int SIZE_THRESHOLD = 1204;

    private static final Log log = LogFactory.getLog(PlayerImagesServiceImpl.class);
    private static final int BUFFER_SIZE = 1000;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String action = request.getParameter("action");
        final Language language = WebUtils.getRequestAttrbiute(request, Language.class);
        if ("remove".equals(action)) {
            final Player player = getPlayer(request);
            if (log.isDebugEnabled()) {
                log.debug("Remove player image: " + player);
            }
            if (player == null) {
                final String msg = ResourceManager.getString("app.session.expired", language);
                sendErrorResponse(response, UpdateImageErrors.SESSION_EXPIRED, msg);
            } else {
                playerImagesManager.removePlayerImage(player.getId(), PlayerImageType.AVATAR);
                sendSuccessResponse(response);
            }
        } else if ("update".equals(action)) {
            final Player player = getPlayer(request);
            if (log.isDebugEnabled()) {
                log.debug("Update player image: " + player);
            }
            if (player == null) {
                final String msg = ResourceManager.getString("app.session.expired", language);
                sendErrorResponse(response, UpdateImageErrors.SESSION_EXPIRED, msg);
            } else {
                try {
                    updatePlayerImage(player, request);
                    sendSuccessResponse(response);
                } catch (FileUploadBase.UnknownSizeException ex) {
                    sendErrorResponse(response, UpdateImageErrors.UNKNOWN_SIZE, null);
                } catch (FileUploadBase.SizeLimitExceededException ex) {
                    sendErrorResponse(response, UpdateImageErrors.TOO_LONG_FILE, null);
                } catch (UnsupportedImageException ex) {
                    sendErrorResponse(response, UpdateImageErrors.UNSUPPORTED_IMAGE, ex.getMessage());
                } catch (Exception ex) {
                    log.error("File can't be uploat by internal error", ex);
                    sendErrorResponse(response, UpdateImageErrors.INTERNAL_ERROR, null);
                }
            }
        } else {
            loadPlayerImage(request, response);
        }
        return null;
    }

    private void sendSuccessResponse(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        final PrintWriter writer = response.getWriter();
        writer.write("success=true");
        writer.flush();
    }

    private void sendErrorResponse(HttpServletResponse response, UpdateImageErrors error, String message) throws IOException {
        response.setContentType("text/html");
        final PrintWriter writer = response.getWriter();
        writer.print("success=false;");
        writer.print("error=");
        writer.print(error.name());
        writer.print(";");
        if (message != null) {
            writer.print("message=");
            writer.print(message);
        }
        writer.flush();
    }

    private Player getPlayer(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final PlayerSessionBean bean = playerSessionsManager.getPlayerSessionBean(session.getId());
        if (bean == null) {
            return null;
        }
        return bean.getPlayer();
    }

    private void updatePlayerImage(Player player, HttpServletRequest request) throws FileUploadException, UnsupportedImageException, IOException {
        final DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(SIZE_THRESHOLD); //1Mb

        final ServletFileUpload upload = new ServletFileUpload(factory);
        if (log.isDebugEnabled()) {
            log.debug("Servlet file upload: " + upload);
        }

        @SuppressWarnings("unchecked")
        final List<FileItem> list = upload.parseRequest(request);
        if (log.isDebugEnabled()) {
            log.debug("FileItems count: " + list.size());
        }
        if (list.size() == 0) {
            throw new FileUploadBase.UnknownSizeException("File item is not attached");
        }
        final FileItem fileItem = list.get(0);
        try {
            if (log.isDebugEnabled()) {
                log.debug("FileItem: " + fileItem.getContentType() + " " + fileItem.getSize());
            }
            playerImagesManager.setPlayerImage(player.getId(), fileItem.getInputStream(), PlayerImageType.AVATAR);
        } finally {
            fileItem.delete();
        }
    }

    private void loadPlayerImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
        response.setDateHeader("Expires", 1);

        final String playerId = request.getParameter("playerId");
        if (log.isDebugEnabled()) {
            log.debug("Load player image: " + playerId);
        }

        if (playerId == null) {
            throw new IllegalArgumentException("PlayerId is not specified");
        }

        final long id;
        try {
            id = Long.valueOf(playerId);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("PlayerId is not specified");
        }

        InputStream stream = playerImagesManager.getPlayerImage(id, PlayerImageType.AVATAR);
        if (stream == null) {
            stream = noImageImageUrl.getInputStream();
        }

        final byte[] buffer = new byte[BUFFER_SIZE];
        final OutputStream outputStream = response.getOutputStream();
        do {
            final int i = stream.read(buffer);
            if (i == -1) {
                break;
            }
            outputStream.write(buffer, 0, i);
        } while (true);
        outputStream.flush();
        stream.close();
    }

    public void setPlayerImagesManager(PlayerImagesManager playerImagesManager) {
        this.playerImagesManager = playerImagesManager;
    }

    public void setNoImageImageUrl(Resource noImageImageUrl) {
        this.noImageImageUrl = noImageImageUrl;
    }

    public void setPlayerSessionsManager(PlayerSessionsManager playerSessionsManager) {
        this.playerSessionsManager = playerSessionsManager;
    }
*/
}
