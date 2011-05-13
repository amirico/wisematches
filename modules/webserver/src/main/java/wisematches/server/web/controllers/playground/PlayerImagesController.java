package wisematches.server.web.controllers.playground;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.server.web.services.images.PlayerImageType;
import wisematches.server.web.services.images.PlayerImagesManager;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * TODO: there is no Unit test for this class
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Controller
@RequestMapping("/game/player/image")
public class PlayerImagesController {
	private Resource noPlayerImageResource;

	private ResourceLoader resourceLoader;
	private PlayerImagesManager playerImagesManager;

	private static final int BUFFER_SIZE = 1000;
	private static final int SIZE_THRESHOLD = 1204;

	private static final Log log = LogFactory.getLog(PlayerImagesController.class);

	public PlayerImagesController() {
	}

	@RequestMapping("view")
	public void getPlayerImage(@RequestParam("pid") String playerId, HttpServletResponse response) throws IOException {
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
			stream = noPlayerImageResource.getInputStream();
		}

		ServletOutputStream outputStream = response.getOutputStream();
		FileCopyUtils.copy(stream, outputStream);
		outputStream.flush();
		stream.close();
	}

/*
	@RequestMapping("image")
	public void handleRequest(HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		final String action = request.getParameter("action");
//		final Language language = WebUtils.getRequestAttrbiute(request, Language.class);
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
*/

	@Autowired
	public void setPlayerImagesManager(PlayerImagesManager playerImagesManager) {
		this.playerImagesManager = playerImagesManager;
	}

	@Autowired
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
		noPlayerImageResource = resourceLoader.getResource("/resources/images/player/noPlayerIcon.png");
	}
}
