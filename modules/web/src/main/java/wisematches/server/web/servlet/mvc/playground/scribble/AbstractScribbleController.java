package wisematches.server.web.servlet.mvc.playground.scribble;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.core.Member;
import wisematches.core.Personality;
import wisematches.playground.*;
import wisematches.playground.propose.GameProposalManager;
import wisematches.playground.propose.ProposalRelation;
import wisematches.playground.scribble.*;
import wisematches.playground.scribble.settings.BoardSettings;
import wisematches.playground.scribble.settings.BoardSettingsManager;
import wisematches.playground.tracking.StatisticManager;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.sdo.ServiceResponse;

import java.util.Locale;
import java.util.concurrent.Callable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class AbstractScribbleController extends WisematchesController {
	protected StatisticManager statisticManager;
	protected ScribblePlayManager playManager;
	protected ScribbleSearchManager searchManager;
	protected BoardSettingsManager boardSettingsManager;
	protected GameProposalManager<ScribbleSettings> proposalManager;

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.AbstractScribbleController");

	protected static final ScribbleContext ACTIVE_GAMES_CTX = new ScribbleContext(true);
	protected static final ScribbleContext FINISHED_GAMES_CTX = new ScribbleContext(false);

	protected static final BoardSettings DEFAULT_BOARD_SETTINGS = new BoardSettings(false, false, true, true, true, "tiles-set-classic");

	public AbstractScribbleController() {
	}

	protected int getActiveGamesCount(Personality principal) {
		int activeGames;
		if (principal instanceof Member) {
			activeGames = statisticManager.getStatistic((Member) principal).getActiveGames();
		} else {
			activeGames = searchManager.getTotalCount(principal, ACTIVE_GAMES_CTX);
		}
		return activeGames + proposalManager.getTotalCount(principal, ProposalRelation.INVOLVED);
	}

	protected int getFinishedGamesCount(Personality principal) {
		if (principal instanceof Member) {
			return statisticManager.getStatistic((Member) principal).getFinishedGames();
		} else {
			return searchManager.getTotalCount(principal, FINISHED_GAMES_CTX);
		}
	}

	protected ServiceResponse processSafeAction(Callable<?> callable, Locale locale) {
		try {
			return responseFactory.success(callable.call());
		} catch (BoardLoadingException e) {
			log.info("Board can't be loaded", e);
			return translateSystemException(e, locale);
		} catch (GameMoveException e) {
			return translateBoardException(e, locale);
		} catch (Exception e) {
			log.error("System move exception", e);
			return translateSystemException(e, locale);
		}
	}

	protected ServiceResponse translateSystemException(Exception e, Locale locale) {
		log.error("System exception found that can't be translated", e);
		return responseFactory.failure("game.error.expired", locale);
	}

	protected ServiceResponse translateBoardException(GameMoveException e, Locale locale) {
		try {
			throw e;
		} catch (GameExpiredException ex) {
			return responseFactory.failure("game.error.expired", locale);
		} catch (GameFinishedException ex) {
			return responseFactory.failure("game.error.finished", locale);
		} catch (UnsuitablePlayerException ex) {
			return responseFactory.failure("game.error.unsuitable", locale);
		} catch (UnknownWordException ex) {
			return responseFactory.failure("game.error.word", new Object[]{ex.getWord()}, locale);
		} catch (IncorrectTilesException ex) {
			switch (ex.getType()) {
				case CELL_ALREADY_BUSY:
					return responseFactory.failure("game.error.tiles.busy", locale);
				case NO_BOARD_TILES:
					return responseFactory.failure("game.error.tiles.board", locale);
				case NO_HAND_TILES:
					return responseFactory.failure("game.error.tiles.hand", locale);
				case TILE_ALREADY_PLACED:
					return responseFactory.failure("game.error.tiles.placed", locale);
				case UNKNOWN_TILE:
					return responseFactory.failure("game.error.tiles.unknown", locale);
			}
		} catch (IncorrectPositionException ex) {
			if (ex.isMustBeInCenter()) {
				return responseFactory.failure("game.error.pos.center", locale);
			} else {
				return responseFactory.failure("game.error.pos.general", locale);
			}
		} catch (IncorrectExchangeException ex) {
			switch (ex.getType()) {
				case EMPTY_TILES:
					return responseFactory.failure("game.error.exchange.empty", locale);
				case UNKNOWN_TILES:
					return responseFactory.failure("game.error.exchange.unknown", locale);
				case EMPTY_BANK:
					return responseFactory.failure("game.error.exchange.bank", locale);
			}
		} catch (GameMoveException ex) {
			log.error("Unexpected game move exception found: {}", ex.getClass(), ex);
			return translateSystemException(e, locale);
		}
		return translateSystemException(e, locale);
	}

	@Autowired
	public void setPlayManager(ScribblePlayManager playManager) {
		this.playManager = playManager;
	}

	@Autowired
	public void setSearchManager(ScribbleSearchManager searchManager) {
		this.searchManager = searchManager;
	}

	@Autowired
	public void setStatisticManager(StatisticManager statisticManager) {
		this.statisticManager = statisticManager;
	}

	@Autowired
	public void setBoardSettingsManager(BoardSettingsManager boardSettingsManager) {
		this.boardSettingsManager = boardSettingsManager;
	}

	@Autowired
	public void setProposalManager(GameProposalManager<ScribbleSettings> proposalManager) {
		this.proposalManager = proposalManager;
	}
}
