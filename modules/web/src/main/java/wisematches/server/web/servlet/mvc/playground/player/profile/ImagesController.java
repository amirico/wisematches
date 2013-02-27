package wisematches.server.web.servlet.mvc.playground.player.profile;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import wisematches.core.personality.player.profile.PlayerImageType;
import wisematches.core.personality.player.profile.PlayerImagesManager;
import wisematches.core.personality.player.profile.UnsupportedImageException;
import wisematches.server.web.servlet.mvc.DeprecatedResponse;
import wisematches.server.web.servlet.mvc.WisematchesController;

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
public class ImagesController extends WisematchesController {
	private ResourceLoader resourceLoader;
	private PlayerImagesManager playerImagesManager;

	private final Map<PlayerImageType, Resource> noPlayersResources = new HashMap<PlayerImageType, Resource>();

	public static final String PREVIEW_ATTRIBUTE_NAME = "PLAYER_IMAGE_PREVIEW_FILE";

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.ImagesController");

	public ImagesController() {
	}

	@RequestMapping("view")
	public void getPlayerImage(@RequestParam("pid") String playerId,
							   @RequestParam(value = "preview", required = false) String preview,
							   Model model, HttpServletResponse response, HttpSession httpSession) throws IOException {
		log.debug("Load player image: {}", playerId);

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
	private DeprecatedResponse previewPlayerImage(HttpServletRequest request, HttpSession httpSession, Locale locale) {
		try {
			final Personality principal = getPrincipal();
			final ServletInputStream inputStream = request.getInputStream();
			if (request.getContentLength() > 512000) {
				return DeprecatedResponse.failure(messageSource.getMessage("profile.edit.error.photo.size2", 512000, locale));
			}

			final PlayerImageType type = PlayerImageType.PROFILE;

			final File tempFile = File.createTempFile(createPreviewFileName(principal, type), "img");
			tempFile.deleteOnExit();

			FileOutputStream out = new FileOutputStream(tempFile);
			FileCopyUtils.copy(inputStream, out);
			out.close();

			httpSession.setAttribute(PREVIEW_ATTRIBUTE_NAME, tempFile);
			return DeprecatedResponse.success();
		} catch (IOException ex) {
			return DeprecatedResponse.failure(messageSource.getMessage("profile.edit.error.system", ex.getMessage(), locale));
		}
	}

	@ResponseBody
	@RequestMapping("remove")
	private DeprecatedResponse removePlayerImage(HttpSession httpSession) {
		final File f = (File) httpSession.getAttribute(PREVIEW_ATTRIBUTE_NAME);
		if (f != null) {
			f.delete();
		}
		httpSession.removeAttribute(PREVIEW_ATTRIBUTE_NAME);
		playerImagesManager.removePlayerImage(getPrincipal().getId(), PlayerImageType.PROFILE);
		return DeprecatedResponse.success();
	}

	@ResponseBody
	@RequestMapping("set")
	private DeprecatedResponse setPlayerImage(HttpSession httpSession, Locale locale) {
		try {
			final Personality principal = getPrincipal();
			final File f = (File) httpSession.getAttribute(PREVIEW_ATTRIBUTE_NAME);
			if (f == null) {
				return DeprecatedResponse.failure("No preview image");
			}

			playerImagesManager.setPlayerImage(principal.getId(), new FileInputStream(f), PlayerImageType.PROFILE);
			httpSession.removeAttribute(PREVIEW_ATTRIBUTE_NAME);
			f.delete();

			return DeprecatedResponse.success();
		} catch (IOException ex) {
			return DeprecatedResponse.failure(messageSource.getMessage("profile.edit.error.system", ex.getMessage(), locale));
		} catch (UnsupportedImageException ex) {
			return DeprecatedResponse.failure(messageSource.getMessage("profile.edit.error.photo.unsupported", locale));
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
}
