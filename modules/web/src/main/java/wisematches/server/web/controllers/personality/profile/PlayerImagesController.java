package wisematches.server.web.controllers.personality.profile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.core.Personality;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.images.PlayerImageType;
import wisematches.server.web.services.images.PlayerImagesManager;
import wisematches.server.web.services.images.UnsupportedImageException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Controller
@RequestMapping("/playground/profile/image")
public class PlayerImagesController extends WisematchesController {
	private ResourceLoader resourceLoader;
	private GameMessageSource gameMessageSource;
	private PlayerImagesManager playerImagesManager;

	private final Map<PlayerImageType, Resource> noPlayersResources = new HashMap<PlayerImageType, Resource>();

	private static final Log log = LogFactory.getLog(PlayerImagesController.class);
	public static final String PREVIEW_ATTRIBUTE_NAME = "PLAYER_IMAGE_PREVIEW_FILE";

	public PlayerImagesController() {
	}

	@RequestMapping("view")
	public void getPlayerImage(@RequestParam("pid") String playerId,
							   @RequestParam(value = "preview", required = false) String preview,
							   Model model, HttpServletResponse response, HttpSession httpSession) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Load player image: " + playerId);
		}

		if (playerId == null) {
			throw new IllegalArgumentException("PlayerId is not specified");
		}
		model.addAttribute("plain", Boolean.TRUE);

		final long id;
		try {
			id = Long.valueOf(playerId);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("PlayerId is not specified");
		}
		final boolean p = (preview != null && Boolean.parseBoolean(preview));

		final PlayerImageType type = PlayerImageType.PROFILE;

		InputStream stream = null;
		if (p) {
			final File f = (File) httpSession.getAttribute(PREVIEW_ATTRIBUTE_NAME);
			if (f != null) {
				stream = new FileInputStream(f);
			}
		} else {
			stream = playerImagesManager.getPlayerImage(id, type);
		}

		if (stream == null) {
			Resource resource = noPlayersResources.get(type);
			if (resource == null) {
				resource = resourceLoader.getResource("/static/images/player/noPlayer" + type.name() + ".png");
				noPlayersResources.put(type, resource);
			}

			if (resource != null) {
				stream = resource.getInputStream();
			}
		}

		ServletOutputStream outputStream = response.getOutputStream();
		FileCopyUtils.copy(stream, outputStream);
		outputStream.flush();
		stream.close();
	}

	@ResponseBody
	@RequestMapping("preview")
	private ServiceResponse previewPlayerImage(HttpServletRequest request, HttpSession httpSession, Locale locale) {
		try {
			final Personality principal = getPrincipal();
			final ServletInputStream inputStream = request.getInputStream();
			if (request.getContentLength() > 512000) {
				return ServiceResponse.failure(gameMessageSource.getMessage("profile.edit.error.photo.size2", locale, 512000));
			}

			final PlayerImageType type = PlayerImageType.PROFILE;

			final File tempFile = File.createTempFile(createPreviewFileName(principal, type), "img");
			tempFile.deleteOnExit();

			FileOutputStream out = new FileOutputStream(tempFile);
			FileCopyUtils.copy(inputStream, out);
			out.close();

			httpSession.setAttribute(PREVIEW_ATTRIBUTE_NAME, tempFile);
			return ServiceResponse.success();
		} catch (IOException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage("profile.edit.error.system", locale, ex.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping("remove")
	private ServiceResponse removePlayerImage(HttpSession httpSession) {
		final File f = (File) httpSession.getAttribute(PREVIEW_ATTRIBUTE_NAME);
		if (f != null) {
			f.delete();
		}
		httpSession.removeAttribute(PREVIEW_ATTRIBUTE_NAME);
		playerImagesManager.removePlayerImage(getPrincipal().getId(), PlayerImageType.PROFILE);
		return ServiceResponse.success();
	}

	@ResponseBody
	@RequestMapping("set")
	private ServiceResponse setPlayerImage(HttpSession httpSession, Locale locale) {
		try {
			final Personality principal = getPrincipal();
			final File f = (File) httpSession.getAttribute(PREVIEW_ATTRIBUTE_NAME);
			if (f == null) {
				return ServiceResponse.failure("No preview image");
			}

			playerImagesManager.setPlayerImage(principal.getId(), new FileInputStream(f), PlayerImageType.PROFILE);
			httpSession.removeAttribute(PREVIEW_ATTRIBUTE_NAME);
			f.delete();

			return ServiceResponse.success();
		} catch (IOException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage("profile.edit.error.system", locale, ex.getMessage()));
		} catch (UnsupportedImageException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage("profile.edit.error.photo.unsupported", locale));
		}
	}

	private String createPreviewFileName(Personality principal, PlayerImageType type) {
		return "player_image_" + principal.getId() + "_" + type.toString();
	}

	@Autowired
	public void setPlayerImagesManager(PlayerImagesManager playerImagesManager) {
		this.playerImagesManager = playerImagesManager;
	}

	@Autowired
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Autowired
	public void setGameMessageSource(GameMessageSource gameMessageSource) {
		this.gameMessageSource = gameMessageSource;
	}
}
