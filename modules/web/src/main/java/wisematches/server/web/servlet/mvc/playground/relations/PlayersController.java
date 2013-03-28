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
import wisematches.core.search.Range;
import wisematches.server.services.relations.players.PlayerContext;
import wisematches.server.services.relations.players.PlayerEntityBean;
import wisematches.server.services.relations.players.PlayerRelationship;
import wisematches.server.services.relations.players.impl.HibernatePlayerSearchManager;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.sdo.DataTablesRequest;
import wisematches.server.web.servlet.sdo.ServiceResponse;
import wisematches.server.web.servlet.sdo.person.PersonalityInfo;
import wisematches.server.web.servlet.sdo.person.PersonalityLongInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/players")
public class PlayersController extends WisematchesController {
	private HibernatePlayerSearchManager playerSearchManager;

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.PlayersController");

	public PlayersController() {
	}

	@RequestMapping("")
	public String showPlayersPage(@RequestParam(value = "area", required = false, defaultValue = "FRIENDS") PlayerRelationship area, Model model) {
		model.addAttribute("searchArea", area);
		return "/content/playground/players/search/view";
	}

	@RequestMapping("load.ajax")
	public ServiceResponse loadPlayersService(HttpServletRequest servletRequest, @RequestBody DataTablesRequest tableRequest, Locale locale) {
		final Player player = getPrincipal();
		PlayerRelationship relationship = null;
		try {
			relationship = PlayerRelationship.valueOf(servletRequest.getParameter("searchTypes"));
		} catch (IllegalArgumentException ignore) {
		}

		final String nickname = servletRequest.getParameter("nickname");
		final String ratingMin = servletRequest.getParameter("ratingMin");
		final String ratingMax = servletRequest.getParameter("ratingMax");
		final String activeMin = servletRequest.getParameter("activeMin");
		final String activeMax = servletRequest.getParameter("activeMax");
		final String finishedMin = servletRequest.getParameter("finishedMin");
		final String finishedMax = servletRequest.getParameter("finishedMax");
		final String lastMoveTime = servletRequest.getParameter("lastMoveTime");

		final String amv = servletRequest.getParameter("minAverageMoveTime");

		Range rating = decodeRange(ratingMin, ratingMax);
		Range activeGames = decodeRange(activeMin, activeMax);
		Range finishedGames = decodeRange(finishedMin, finishedMax);

		float minAverageMoveTime = Float.NaN;
		if (!amv.isEmpty()) {
			try {
				minAverageMoveTime = Float.parseFloat(amv);
			} catch (NumberFormatException ignore) {
			}
		}

		Date lastMove = null;
		if (lastMoveTime != null) {
			try {
				lastMove = new Date(System.currentTimeMillis() - Long.valueOf(lastMoveTime));
			} catch (NumberFormatException ignore) {
			}
		}

		final PlayerContext context = new PlayerContext(nickname, relationship, rating, activeGames, finishedGames, lastMove, minAverageMoveTime);
		log.debug("Loading players with context: {} for player {}", context, player);

		final int totalCount = playerSearchManager.getTotalCount(player, context);
		final List<PlayerEntityBean> beans = playerSearchManager.searchEntities(player, context, tableRequest.getOrders(), tableRequest.getLimit());
		final List<PersonalityLongInfo> rows = new ArrayList<>();
		for (PlayerEntityBean bean : beans) {
			final Personality person = personalityManager.getPerson(bean.getPlayer());
			final PersonalityInfo pi = new PersonalityInfo(messageSource.getPersonalityNick(person, locale), person, playerStateManager);
			rows.add(new PersonalityLongInfo(pi, bean, messageSource, locale));
		}
		return responseFactory.success(tableRequest.replay(totalCount, rows));
	}

	private Range decodeRange(String ratingMin, String ratingMax) {
		Range rating = null;
		if (!ratingMin.isEmpty() || !ratingMax.isEmpty()) {
			int min = 0;
			try {
				min = ratingMin.isEmpty() ? 0 : Integer.parseInt(ratingMin);
			} catch (NumberFormatException ignore) {
			}
			int max = 0;
			try {
				max = ratingMax.isEmpty() ? 0 : Integer.parseInt(ratingMax);
			} catch (NumberFormatException ignore) {
			}
			rating = Range.limit(min, max == 0 ? 0 : max - min);
		}
		return rating;
	}

	@Autowired
	public void setPlayerSearchManager(HibernatePlayerSearchManager playerSearchManager) {
		this.playerSearchManager = playerSearchManager;
	}
}
