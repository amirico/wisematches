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
import wisematches.personality.player.Player;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.services.images.PlayerImageType;
import wisematches.server.web.services.images.PlayerImagesManager;
import wisematches.server.web.services.images.UnsupportedImageException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: there is no Unit test for this class
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Controller
@RequestMapping("/playground/profile/image")
public class PlayerImagesController extends AbstractPlayerController {
	private ResourceLoader resourceLoader;
	private PlayerImagesManager playerImagesManager;

	private final Map<PlayerImageType, Resource> noPlayersResources = new HashMap<PlayerImageType, Resource>();

	private static final int BUFFER_SIZE = 1000;
	private static final int SIZE_THRESHOLD = 1204;

	private static final Log log = LogFactory.getLog(PlayerImagesController.class);

	public PlayerImagesController() {
	}

	@RequestMapping("view")
	public void getPlayerImage(@RequestParam("pid") String playerId, Model model, HttpServletResponse response) throws IOException {
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

		final PlayerImageType type = PlayerImageType.PROFILE;
		InputStream stream = playerImagesManager.getPlayerImage(id, type);
		if (stream == null) {
			Resource resource = noPlayersResources.get(type);
			if (resource == null) {
				resource = resourceLoader.getResource("/resources/images/player/noPlayer" + type.name() + ".png");
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
	@RequestMapping("edit")
	private ServiceResponse updatePlayerImage(HttpServletRequest request) {
		try {
			final ServletInputStream inputStream = request.getInputStream();
			Player principal = getPrincipal();
			playerImagesManager.setPlayerImage(principal.getId(), inputStream, PlayerImageType.PROFILE);
			return ServiceResponse.success();
		} catch (IOException ex) {
			return ServiceResponse.failure(ex.getMessage());
		} catch (UnsupportedImageException ex) {
			return ServiceResponse.failure(ex.getMessage());
		}
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
