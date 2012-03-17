package wisematches.server.web.controllers.playground.scribble;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wisematches.personality.Language;
import wisematches.personality.Membership;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.personality.player.computer.robot.RobotType;
import wisematches.playground.*;
import wisematches.playground.blacklist.BlacklistManager;
import wisematches.playground.blacklist.BlacklistedException;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.GameProposalManager;
import wisematches.playground.propose.a.GameRestriction;
import wisematches.playground.restriction.RestrictionException;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.playground.restrictions.GameRestrictionRating;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.player.PlayerSearchArea;
import wisematches.playground.scribble.player.ScribblePlayerSearchManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.scribble.form.CreateScribbleForm;
import wisematches.server.web.controllers.playground.scribble.form.CreateScribbleTab;
import wisematches.server.web.controllers.playground.scribble.form.PlayerInfoForm;
import wisematches.server.web.controllers.playground.scribble.form.ScribbleInfoForm;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.state.PlayerStateManager;

import javax.validation.Valid;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class ScribbleGameController extends WisematchesController {
    private PlayerManager playerManager;
    private GameMessageSource messageSource;
    private BlacklistManager blacklistManager;
    private ScribbleBoardManager boardManager;
    private RestrictionManager restrictionManager;
    private PlayerStateManager playerStateManager;
    private ScribblePlayerSearchManager searchManager;
    private GameProposalManager<ScribbleSettings> proposalManager;

    private static final String[] SEARCH_COLUMNS = new String[]{"nickname", "ratingG", "ratingA", "language", "averageMoveTime", "lastMoveTime"};
    private static final List<PlayerSearchArea> SEARCH_AREAS = Arrays.asList(PlayerSearchArea.values());

    private static final Log log = LogFactory.getLog("wisematches.server.web.dashboard");

    public ScribbleGameController() {
    }

    @RequestMapping("active")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String showActiveGames(@RequestParam(value = "p", required = false) Long pid, Model model) throws UnknownEntityException {
        final Player principal;
        if (pid == null) {
            principal = getPrincipal();
        } else {
            principal = playerManager.getPlayer(pid);
        }
        if (principal == null) {
            throw new UnknownEntityException(null, "account");
        }
        if (log.isDebugEnabled()) {
            log.debug("Loading active games for personality: " + principal);
        }
        model.addAttribute("player", principal);

        final Collection<ScribbleBoard> activeBoards = boardManager.searchEntities(principal, GameState.ACTIVE, null, null, null);
        model.addAttribute("activeBoards", activeBoards);
        if (log.isDebugEnabled()) {
            log.debug("Found " + activeBoards.size() + " active games for personality: " + principal);
        }

        if (principal == getPrincipal()) {
            final Collection<GameProposal<ScribbleSettings>> proposals = proposalManager.getPlayerProposals(principal);
            model.addAttribute("activeProposals", proposals);
            if (log.isDebugEnabled()) {
                log.debug("Found " + proposals.size() + " proposals for personality: " + principal);
            }
        } else {
            model.addAttribute("activeProposals", Collections.emptyList());
        }
        return "/content/playground/scribble/active";
    }

    @ResponseBody
    @RequestMapping(value = "active.ajax")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ServiceResponse showActiveGamesAjax(@RequestParam(value = "p", required = false) Long pid, Locale locale) throws UnknownEntityException {
        final Player principal;
        if (pid == null) {
            principal = getPrincipal();
        } else {
            principal = playerManager.getPlayer(pid);
        }
        if (principal == null) {
            throw new UnknownEntityException(null, "account");
        }
        if (log.isDebugEnabled()) {
            log.debug("Loading active games for personality: " + principal);
        }

        final Collection<ScribbleBoard> activeBoards = boardManager.searchEntities(principal, GameState.ACTIVE, null, null, null);
        final List<ScribbleInfoForm> forms = new ArrayList<ScribbleInfoForm>(activeBoards.size());
        for (ScribbleBoard board : activeBoards) {
            final ScribbleSettings settings = board.getGameSettings();
            final long playerTurn = board.getPlayerTurn() != null ? board.getPlayerTurn().getPlayerId() : 0;

            final List<ScribblePlayerHand> playersHands = board.getPlayersHands();
            final PlayerInfoForm[] players = new PlayerInfoForm[playersHands.size()];
            for (int i = 0, playersHandsSize = playersHands.size(); i < playersHandsSize; i++) {
                final ScribblePlayerHand hand = playersHands.get(i);
                final Player player = playerManager.getPlayer(hand.getPlayerId());
                players[i] = new PlayerInfoForm(player.getId(),
                        player.getNickname(),
                        player.getMembership().name(),
                        playerStateManager.isPlayerOnline(player),
                        hand.getPoints());
            }
            final String elapsedTime = messageSource.formatRemainedTime(board, locale);
            forms.add(new ScribbleInfoForm(board.getBoardId(), settings, elapsedTime, playerTurn, players));
        }
        return ServiceResponse.success(null, "boards", forms);
    }

    @RequestMapping("join")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String showWaitingGames(Model model) throws RestrictionException {
        final Player principal = getPrincipal();
        if (log.isDebugEnabled()) {
            log.debug("Loading waiting games for personality: " + principal);
        }
        final List<GameProposal<ScribbleSettings>> proposals = new ArrayList<GameProposal<ScribbleSettings>>(proposalManager.getActiveProposals());
        if (log.isDebugEnabled()) {
            log.debug("Found " + proposals.size() + " proposals for personality: " + principal);
        }
        model.addAttribute("gamesCount", restrictionManager.getRestriction(principal, "games.active"));
        model.addAttribute("restricted", restrictionManager.isRestricted(principal, "games.active", getActiveGamesCount(principal)));
        model.addAttribute("activeProposals", proposals);
        model.addAttribute("blacklistManager", blacklistManager);
        return "/content/playground/scribble/join";
    }

    @ResponseBody
    @RequestMapping("join.ajax")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ServiceResponse showWaitingGamesAjax() throws RestrictionException {
        return ServiceResponse.FAILURE;
    }

    @RequestMapping(value = "join", params = "p")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String joinGameAction(@RequestParam("p") long id, Model model) throws BoardManagementException, RestrictionException {
        if (log.isInfoEnabled()) {
            log.info("Join to game: " + id);
        }
        try {
            final Player principal = getPrincipal();
            restrictionManager.checkRestriction(principal, "games.active", getActiveGamesCount(principal));

            GameProposal<ScribbleSettings> proposal = proposalManager.getProposal(id);
            if (proposal != null) {
                for (Personality personality : proposal.getPlayers()) {
                    blacklistManager.checkBlacklist(personality, principal);
                }
            }

            proposal = proposalManager.attachPlayer(id, principal);
            if (proposal == null) {
                model.addAttribute("joinError", "game.error.restriction.ready.description");
            } else if (proposal.isReady()) {
                final ScribbleBoard board = boardManager.createBoard(proposal.getGameSettings(), proposal.getPlayers());
                return "redirect:/playground/scribble/board?b=" + board.getBoardId();
            }
        } catch (BlacklistedException e) {
            model.addAttribute("joinError", "game.error.restriction.blacklist.description");
        } catch (ViolatedCriterionException e) {
            model.addAttribute("joinError", "game.error.restriction." + e.getCode() + ".description");
            model.addAttribute("joinErrorArgs", new Object[]{e.getActualValue(), e.getExpectedValue()});
        }
        return showWaitingGames(model);
    }

    @RequestMapping(value = "accept", params = "p")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String acceptProposalAction(@RequestParam("p") long id, Model model) throws BoardManagementException, RestrictionException {
        if (log.isInfoEnabled()) {
            log.info("Accept a game: " + id);
        }
        return joinGameAction(id, model);
    }

    @RequestMapping(value = "decline", params = "p")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String declineProposalAction(@RequestParam("p") long id, Model model) throws BoardManagementException, RestrictionException {
        if (log.isInfoEnabled()) {
            log.info("Decline a game: " + id);
        }
        try {
            final Player principal = getPrincipal();
            final GameProposal<ScribbleSettings> proposal = proposalManager.cancel(id, principal);
            if (proposal == null) {
                model.addAttribute("joinError", "game.error.restriction.ready.description");
            }
        } catch (ViolatedCriterionException e) {
            model.addAttribute("joinError", "game.error.restriction." + e.getCode() + ".description");
            model.addAttribute("joinErrorArgs", new Object[]{e.getActualValue(), e.getExpectedValue()});
        }
        return showWaitingGames(model);
    }

    @ResponseBody
    @RequestMapping("decline.ajax")
    public ServiceResponse declineProposalAjax(@RequestParam("p") long proposal) {
        final Player player = getPrincipal();
        if (log.isDebugEnabled()) {
            log.debug("Cancel proposal " + proposal + " for player " + player);
        }

        try {
            if (proposalManager.cancel(proposal, player) == null) {
                return ServiceResponse.FAILURE;
            }
            return ServiceResponse.SUCCESS;
        } catch (NumberFormatException ex) {
            return ServiceResponse.failure("format");
        } catch (ViolatedCriterionException e) {
            return ServiceResponse.failure("active");
        }
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
                initRobotForm(form, parameter, locale);
            } else if ("wait".equalsIgnoreCase(type)) {
                initWaitOpponentForm(form, parameter, locale);
            } else if ("challenge".equalsIgnoreCase(type)) {
                initChallengeForm(form, parameter, locale);
            } else if ("board".equalsIgnoreCase(type)) {
                initBoardCloneForm(form, parameter, locale);
            } else {
                initDefaultForm(form, parameter, locale);
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
                proposalManager.initiateProposal(getPrincipal(), s, form.getChallengeMessage(), players);
            }
        } else if (players.size() == 0) { // waiting
            GameRestriction r = null;
            if (form.getMinRating() != 0 || form.getMaxRating() != 0) {
                r = new GameRestrictionRating(form.getMinRating(), form.getMaxRating());
            }
            proposalManager.initiateProposal(getPrincipal(), s, opponents.length + 1, r);
        } else {
            result.rejectValue("commonError", "game.create.opponents.err.mixed");
            return createGamePage(null, null, form, model, locale);
        }

        if (board != null) {
            return "redirect:/playground/scribble/board?b=" + board.getBoardId();
        }
        return "redirect:/playground/scribble/active";
    }

    private void initRobotForm(CreateScribbleForm form, String parameter, Locale locale) {
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

    private void initWaitOpponentForm(CreateScribbleForm form, String parameter, Locale locale) {
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

    private void initDefaultForm(CreateScribbleForm form, String parameter, Locale locale) {
        form.setCreateTab(CreateScribbleTab.ROBOT);
        form.setOpponentsCount(1);
    }

    private int getActiveGamesCount(Player principal) {
        return boardManager.getTotalCount(principal, GameState.ACTIVE) + proposalManager.getPlayerProposals(principal).size();
    }

    @Autowired
    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Autowired
    public void setMessageSource(GameMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setBoardManager(ScribbleBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @Autowired
    public void setBlacklistManager(BlacklistManager blacklistManager) {
        this.blacklistManager = blacklistManager;
    }

    @Autowired
    public void setPlayerStateManager(PlayerStateManager playerStateManager) {
        this.playerStateManager = playerStateManager;
    }

    @Autowired
    public void setRestrictionManager(RestrictionManager restrictionManager) {
        this.restrictionManager = restrictionManager;
    }

    @Autowired
    public void setSearchManager(ScribblePlayerSearchManager searchManager) {
        this.searchManager = searchManager;
    }

    @Autowired
    public void setProposalManager(GameProposalManager<ScribbleSettings> proposalManager) {
        this.proposalManager = proposalManager;
    }
}
