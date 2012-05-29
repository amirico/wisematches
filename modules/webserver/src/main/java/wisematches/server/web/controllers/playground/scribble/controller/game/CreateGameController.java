package wisematches.server.web.controllers.playground.scribble.controller.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wisematches.personality.Language;
import wisematches.personality.Membership;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.personality.player.computer.robot.RobotType;
import wisematches.playground.BoardCreationException;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.criteria.ComparableOperator;
import wisematches.playground.criteria.PlayerCriterion;
import wisematches.playground.criteria.PlayerRestrictions;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.player.PlayerSearchArea;
import wisematches.playground.scribble.player.ScribblePlayerSearchManager;
import wisematches.server.web.controllers.ServiceResponse;
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

    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @RequestMapping(value = "create.ajax", method = RequestMethod.POST)
    public ServiceResponse createGameAction(@RequestBody CreateScribbleForm form, Locale locale) {
        if (log.isInfoEnabled()) {
            log.info("Create new game: " + form);
        }

        try {
            Thread.sleep(15000);
            return ServiceResponse.failure();
        } catch (InterruptedException ex) {
        }

        if (form.getTitle().length() > 150) {
            return ServiceResponse.failure(messageSource.getMessage("game.create.title.err.max", locale));
        }
        if (form.getDaysPerMove() < 2) {
            return ServiceResponse.failure(messageSource.getMessage("game.create.time.err.min", locale));
        } else if (form.getDaysPerMove() > 14) {
            return ServiceResponse.failure(messageSource.getMessage("game.create.time.err.max", locale));
        }

        if (form.getCreateTab() == null) {
            return ServiceResponse.failure(messageSource.getMessage("game.create.opponent.err.blank", locale));
        }

        if (form.getChallengeMessage().length() > 254) {
            return ServiceResponse.failure(messageSource.getMessage("game.create.opponent.challenge.err", locale));
        }

        final Player principal = getPrincipal();
        final int activeGamesCount = getActiveGamesCount(principal);
        if (restrictionManager.isRestricted(principal, "games.active", activeGamesCount)) {
            return ServiceResponse.failure(messageSource.getMessage("game.create.forbidden", locale, activeGamesCount));
        }

        final Language language = Language.byCode(form.getBoardLanguage());
        if (language == null) {
            return ServiceResponse.failure(messageSource.getMessage("game.create.language.err.blank", locale));
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
            return ServiceResponse.failure(messageSource.getMessage("game.create.opponent.err.incorrect", locale));
        }

        boolean robot = false;
        final List<Player> players = new ArrayList<Player>();
        final Comparable restriction = restrictionManager.getRestriction(principal, "scribble.opponents");
        if (opponents.length > (Integer) restriction) {
            return ServiceResponse.failure(messageSource.getMessage("game.create.opponents.err.count", locale, restriction));
        } else {
            if (opponents.length == 0) {
                return ServiceResponse.failure(messageSource.getMessage("game.create.opponents.err.min", locale));
            } else if (opponents.length > 3) {
                return ServiceResponse.failure(messageSource.getMessage("game.create.opponents.err.max", locale));
            } else {
                for (long opponent : opponents) {
                    final ComputerPlayer computerPlayer = RobotPlayer.getComputerPlayer(opponent);
                    if (computerPlayer instanceof RobotPlayer) {
                        robot = true;
                        if (opponents.length > 1) {
                            return ServiceResponse.failure(messageSource.getMessage("game.create.opponents.err.robot", locale));
                        } else {
                            players.add(computerPlayer);
                        }
                    } else if (ComputerPlayer.isComputerPlayer(opponent)) {
                        return ServiceResponse.failure(messageSource.getMessage("game.create.opponents.err.unknown", locale, opponent));
                    } else if (opponent != 0) {
                        Player p = playerManager.getPlayer(opponent);
                        if (p == null) {
                            return ServiceResponse.failure(messageSource.getMessage("game.create.opponents.err.unknown", locale, opponent));
                        } else {
                            players.add(p);
                        }
                    }
                }
            }
        }

        ScribbleBoard board = null;
        if (players.size() == opponents.length) { // challenge or robot
            if (robot) { // robot
                players.add(getPrincipal());
                try {
                    board = boardManager.createBoard(s, players);
                } catch (BoardCreationException ex) {
                    log.error("New board can't be created: " + s + ", " + players, ex);
                    return ServiceResponse.failure(messageSource.getMessage("game.create.opponents.err.unknown", locale));
                }
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
            return ServiceResponse.failure(messageSource.getMessage("game.create.opponents.err.mixed", locale));
        }

        if (board != null) {
            return ServiceResponse.success(null, "board", board.getBoardId());
        }
        return ServiceResponse.success();
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
