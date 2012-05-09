package wisematches.server.web.controllers.playground.scribble.controller.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.personality.Language;
import wisematches.personality.Membership;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.personality.player.computer.robot.RobotType;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.BoardManagementException;
import wisematches.playground.criteria.ComparableOperator;
import wisematches.playground.criteria.PlayerCriterion;
import wisematches.playground.criteria.PlayerRestrictions;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.player.PlayerSearchArea;
import wisematches.playground.scribble.player.ScribblePlayerSearchManager;
import wisematches.server.web.controllers.playground.scribble.form.CreateScribbleForm;
import wisematches.server.web.controllers.playground.scribble.form.CreateScribbleTab;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class CreateGameController extends AbstractGameController {
    private RestrictionManager restrictionManager;
    private ScribblePlayerSearchManager searchManager;

    private static final String[] SEARCH_COLUMNS = new String[]{"nickname", "ratingG", "ratingA", "language", "averageMoveTime", "lastMoveTime"};
    private static final List<PlayerSearchArea> SEARCH_AREAS = Arrays.asList(PlayerSearchArea.values());

    private static final Log log = LogFactory.getLog("wisematches.server.web.game.create");

    public CreateGameController() {
    }

    @RequestMapping("create")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String createGamePage(@RequestParam(value = "t", required = false) String type,
                                 @RequestParam(value = "p", required = false) String parameter,
                                 @Valid @ModelAttribute("create") CreateScribbleForm form,
                                 Model model, Locale locale) {
        if (form.getBoardLanguage() == null) {
            form.setBoardLanguage(locale.getLanguage());
        }

        if (!form.isRotten()) {
            if ("robot".equalsIgnoreCase(type)) {
                initRobotForm(form, parameter);
            } else if ("wait".equalsIgnoreCase(type)) {
                initWaitOpponentForm(form, parameter);
            } else if ("challenge".equalsIgnoreCase(type)) {
                initChallengeForm(form, parameter, locale);
            } else if ("board".equalsIgnoreCase(type)) {
                initBoardCloneForm(form, parameter, locale);
            } else {
                initDefaultForm(form);
            }
        }

//		int finishedGames = boardManager.getTotalCount(getPersonality(), GameState.FINISHED);
        // TODO: check games count here. Statistic should be used.

        final Player principal = getPrincipal();
        model.addAttribute("robotPlayers", RobotPlayer.getRobotPlayers());
        model.addAttribute("gamesCount", restrictionManager.getRestriction(principal, "games.active"));
        model.addAttribute("restricted", restrictionManager.isRestricted(principal, "games.active", getActiveGamesCount(principal)));
        model.addAttribute("maxOpponents", restrictionManager.getRestriction(principal, "scribble.opponents"));

        model.addAttribute("searchArea", PlayerSearchArea.FRIENDS);
        model.addAttribute("searchAreas", SEARCH_AREAS);
        model.addAttribute("searchColumns", SEARCH_COLUMNS);
        model.addAttribute("searchEntityDescriptor", searchManager.getEntityDescriptor());

        if (principal.getMembership() == Membership.GUEST) {
            form.setCreateTab(CreateScribbleTab.ROBOT);
            model.addAttribute("playRobotsOnly", true);
        } else {
            model.addAttribute("playRobotsOnly", false);
        }
        return "/content/playground/scribble/create";
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createGameAction(@Valid @ModelAttribute("create") CreateScribbleForm form,
                                   BindingResult result, Model model, Locale locale) throws BoardManagementException {
        if (log.isInfoEnabled()) {
            log.info("Create new game: " + form);
        }

        final Player principal = getPrincipal();
        if (restrictionManager.isRestricted(principal, "games.active", getActiveGamesCount(principal))) {
            return createGamePage(null, null, form, model, locale);
        }

        final Language language = Language.byCode(form.getBoardLanguage());
        if (language == null) {
            result.rejectValue("boardLanguage", "game.create.language.err.blank");
            return createGamePage(null, null, form, model, locale);
        }

        long[] opponents;
        final ScribbleSettings s = new ScribbleSettings(form.getTitle(), language, form.getDaysPerMove());
        if (form.getCreateTab() == CreateScribbleTab.ROBOT) {
            opponents = new long[]{RobotPlayer.valueOf(form.getRobotType()).getId()};
        } else if (form.getCreateTab() == CreateScribbleTab.WAIT) {
            opponents = new long[form.getOpponentsCount()];
        } else if (form.getCreateTab() == CreateScribbleTab.CHALLENGE) {
            opponents = form.getOpponents();
        } else {
            result.rejectValue("commonError", "game.create.opponent.err.incorrect");
            return createGamePage(null, null, form, model, locale);
        }

        boolean robot = false;
        final List<Player> players = new ArrayList<Player>();
        final Comparable restriction = restrictionManager.getRestriction(principal, "scribble.opponents");
        if (opponents.length > (Integer) restriction) {
            result.rejectValue("commonError", "game.create.opponents.err.count", new Object[]{restriction, "/account/membership"}, null);
        } else {
            if (opponents.length == 0) {
                result.rejectValue("commonError", "game.create.opponents.err.count", new Object[]{restriction, "/account/membership"}, null);
            } else if (opponents.length > 3) {
                result.rejectValue("commonError", "game.create.opponents.err.count", new Object[]{restriction, "/account/membership"}, null);
            } else {
                for (long opponent : opponents) {
                    final ComputerPlayer computerPlayer = RobotPlayer.getComputerPlayer(opponent);
                    if (computerPlayer instanceof RobotPlayer) {
                        robot = true;
                        if (opponents.length > 1) {
                            result.rejectValue("commonError", "game.create.opponents.err.robot");
                        } else {
                            players.add(computerPlayer);
                        }
                    } else if (ComputerPlayer.isComputerPlayer(opponent)) {
                        result.rejectValue("commonError", "game.create.opponents.err.unknown", new Object[]{opponent}, null);
                    } else if (opponent != 0) {
                        Player p = playerManager.getPlayer(opponent);
                        if (p == null) {
                            result.rejectValue("commonError", "game.create.opponents.err.unknown", new Object[]{opponent}, null);
                        } else {
                            players.add(p);
                        }
                    }
                }
            }
        }

        if (result.hasErrors()) {
            return createGamePage(null, null, form, model, locale);
        }

        ScribbleBoard board = null;
        if (players.size() == opponents.length) { // challenge or robot
            if (robot) { // robot
                players.add(getPrincipal());
                board = boardManager.createBoard(s, players);
            } else { // challenge
                proposalManager.initiate(s, getPrincipal(), players, form.getChallengeMessage());
            }
        } else if (players.size() == 0) { // waiting
            final List<PlayerCriterion> res = new ArrayList<PlayerCriterion>();
            if (form.getMinRating() > 0) {
                res.add(PlayerRestrictions.rating("player.rating.min", form.getMinRating(), ComparableOperator.GE));
            }
            if (form.getMaxRating() > 0) {
                res.add(PlayerRestrictions.rating("player.rating.max", form.getMaxRating(), ComparableOperator.LE));
            }
            if (form.getTimeouts() > 0) {
                res.add(PlayerRestrictions.timeouts("game.timeouts", form.getTimeouts(), ComparableOperator.LE));
            }
            if (form.getCompleted() > 0) {
                res.add(PlayerRestrictions.completed("game.completed", form.getCompleted(), ComparableOperator.GE));
            }
            proposalManager.initiate(s, getPrincipal(), opponents.length, res.toArray(new PlayerCriterion[res.size()]));
        } else {
            result.rejectValue("commonError", "game.create.opponents.err.mixed");
            return createGamePage(null, null, form, model, locale);
        }

        if (board != null) {
            return "redirect:/playground/scribble/board?b=" + board.getBoardId();
        }
        return "redirect:/playground/scribble/active";
    }

    private void initRobotForm(CreateScribbleForm form, String parameter) {
        form.setCreateTab(CreateScribbleTab.ROBOT);

        RobotType type = RobotType.TRAINEE;
        if (parameter != null) {
            try {
                type = RobotType.valueOf(parameter.toUpperCase());
            } catch (IllegalArgumentException ignore) {
            }
        }
        form.setRobotType(type);
    }

    private void initWaitOpponentForm(CreateScribbleForm form, String parameter) {
        form.setCreateTab(CreateScribbleTab.WAIT);
        form.setOpponentsCount(1);
        if (parameter != null) {
            try {
                form.setOpponentsCount(Integer.valueOf(parameter));
            } catch (NumberFormatException ignore) {
            }
        }
    }

    private void initChallengeForm(CreateScribbleForm form, String parameter, Locale locale) {
        form.setTitle(messageSource.getMessage("game.challenge.player.label", locale, getPrincipal().getNickname()));
        form.setChallengeMessage("");
        form.setCreateTab(CreateScribbleTab.CHALLENGE);
        final List<Long> ids = new ArrayList<Long>();
        if (parameter != null) {
            final String[] split = parameter.split("\\|");
            for (String id : split) {
                try {
                    Player player = playerManager.getPlayer(Long.valueOf(id));
                    if (player != null) {
                        ids.add(player.getId());
                    }
                } catch (NumberFormatException ignore) {
                }
            }

            if (ids.size() != 0) {
                final long[] res = new long[ids.size()];
                for (int i = 0, idsSize = ids.size(); i < idsSize; i++) {
                    res[i] = ids.get(i);
                }
                form.setOpponents(res);
            }
        }
    }

    private void initBoardCloneForm(CreateScribbleForm form, String parameter, Locale locale) {
        try {
            final ScribbleBoard board = boardManager.openBoard(Long.valueOf(parameter));
            if (board != null) {
                form.setTitle(messageSource.getMessage("game.challenge.replay.label", locale, board.getBoardId()));
                form.setChallengeMessage(messageSource.getMessage("game.challenge.replay.description", locale, messageSource.getPlayerNick(getPrincipal(), locale)));
                form.setBoardLanguage(board.getGameSettings().getLanguage());
                form.setDaysPerMove(board.getGameSettings().getDaysPerMove());

                int index = 0;
                final List<ScribblePlayerHand> playersHands = board.getPlayersHands();
                final long[] players = new long[playersHands.size() - 1];
                for (ScribblePlayerHand playersHand : playersHands) {
                    if (playersHand.getPlayerId() == getPersonality().getId()) {
                        continue;
                    }
                    players[index++] = playersHand.getPlayerId();
                }
                form.setOpponents(players);
                form.setCreateTab(CreateScribbleTab.CHALLENGE);
            }
        } catch (BoardLoadingException ignore) { // do nothing
        }
    }

    private void initDefaultForm(CreateScribbleForm form) {
        form.setCreateTab(CreateScribbleTab.ROBOT);
        form.setOpponentsCount(1);
    }

    @Autowired
    public void setRestrictionManager(RestrictionManager restrictionManager) {
        this.restrictionManager = restrictionManager;
    }

    @Autowired
    public void setSearchManager(ScribblePlayerSearchManager searchManager) {
        this.searchManager = searchManager;
    }
}
