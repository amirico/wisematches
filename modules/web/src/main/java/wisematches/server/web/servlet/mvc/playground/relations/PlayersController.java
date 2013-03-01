package wisematches.server.web.servlet.mvc.playground.relations;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.server.services.relations.PlayerEntityBean;
import wisematches.server.services.relations.PlayerSearchArea;
import wisematches.server.services.relations.ScribblePlayerSearchManager;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.sdo.DataTablesRequest;
import wisematches.server.web.servlet.sdo.ServiceResponse;
import wisematches.server.web.servlet.sdo.person.PersonalityInfo;
import wisematches.server.web.servlet.sdo.person.PersonalityLongInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/players")
public class PlayersController extends WisematchesController {
	private ScribblePlayerSearchManager playerSearchManager;

	private static final List<PlayerSearchArea> AREAS = Arrays.asList(PlayerSearchArea.FRIENDS, PlayerSearchArea.FORMERLY, PlayerSearchArea.PLAYERS);

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.PlayersController");

	public PlayersController() {
	}

	@RequestMapping("")
	public String showPlayersPage(@RequestParam(value = "area", required = false, defaultValue = "FRIENDS") PlayerSearchArea area, Model model) {
		model.addAttribute("searchArea", area);
		model.addAttribute("searchAreas", AREAS);
		return "/content/playground/players/search/view";
	}

	@RequestMapping("load.ajax")
	public ServiceResponse loadPlayersService(@RequestParam("area") String areaName, @RequestBody DataTablesRequest request, Locale locale) {
		final Player player = getPrincipal();
		final PlayerSearchArea area = PlayerSearchArea.valueOf(areaName.toUpperCase());
		log.debug("Loading players for area: {} for player ", area, player);

		final int totalCount = playerSearchManager.getTotalCount(player, area);
		final List<PlayerEntityBean> beans = playerSearchManager.searchEntities(player, area, request.getOrders(), request.getLimit());
		final List<PersonalityLongInfo> rows = new ArrayList<>();
		for (PlayerEntityBean bean : beans) {
			final Personality person = personalityManager.getPerson(bean.getPlayer());
			final PersonalityInfo pi = PersonalityInfo.get(messageSource.getPersonalityNick(person, locale), person, playerStateManager);
			rows.add(new PersonalityLongInfo(pi, bean, messageSource, locale));
		}
		return responseFactory.success(request.replay(totalCount, rows));
	}

	@Autowired
	public void setPlayerSearchManager(ScribblePlayerSearchManager playerSearchManager) {
		this.playerSearchManager = playerSearchManager;
	}
}
