package wisematches.server.web.controllers.playground;

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
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.BoardManagementException;
import wisematches.playground.blacklist.BlacklistManager;
import wisematches.playground.blacklist.BlacklistedException;
import wisematches.playground.message.MessageManager;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.GameProposalManager;
import wisematches.playground.propose.GameRestriction;
import wisematches.playground.propose.ViolatedRestrictionException;
import wisematches.playground.propose.restrictions.GameRestrictionRating;
import wisematches.playground.restriction.RestrictionException;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.search.ScribbleSearchesEngine;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.form.CreateScribbleForm;
import wisematches.server.web.controllers.playground.form.OpponentType;
import wisematches.server.web.services.ads.AdvertisementManager;

import javax.validation.Valid;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class ScribbleGameController extends WisematchesController {
    private PlayerManager playerManager;
    private MessageManager messageManager;
    private BlacklistManager blacklistManager;
    private ScribbleBoardManager boardManager;
    private ScribbleSearchesEngine searchesEngine;
    private RestrictionManager restrictionManager;
    private AdvertisementManager advertisementManager;
    private GameProposalManager<ScribbleSettings> proposalManager;

    private static final Log log = LogFactory.getLog("wisematches.server.web.dashboard");

    public ScribbleGameController() {
    }

    @RequestMapping("create")
    public String createGamePage(@Valid @ModelAttribute("create") CreateScribbleForm form, BindingResult result,
                                 Model model, Locale locale) {
        if (form.getBoardLanguage() == null) {
            form.setBoardLanguage(locale.getLanguage());
        }

        final Player principal = getPrincipal();
        model.addAttribute("robotPlayers", RobotPlayer.getRobotPlayers());
        model.addAttribute("gamesCount", restrictionManager.getRestriction(principal, "games.active"));
        model.addAttribute("restricted", restrictionManager.isRestricted(principal, "games.active", getActiveGamesCount(principal)));
        model.addAttribute("maxOpponents", restrictionManager.getRestriction(principal, "scribble.opponents"));
        model.addAttribute("playRobotsOnly", principal.getMembership() == Membership.GUEST);
        if (principal.getMembership().isAdsVisible()) {
            model.addAttribute("advertisementBlock", advertisementManager.getAdvertisementBlock("dashboard", Language.byLocale(locale)));
        }
        return "/content/playground/scribble/create";
    }

    @RequestMapping("challenge")
    public String challenge(@RequestParam("p") long pid, @Valid @ModelAttribute("create") CreateScribbleForm form,
                            BindingResult result, Model model, Locale locale) {
        form.setTitle("Challenge from " + getPrincipal().getNickname());
        form.setOpponentType(OpponentType.CHALLENGE);
        form.setOpponents(new long[]{pid});
        return createGamePage(form, result, model, locale);
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
            return createGamePage(form, result, model, locale);
        }

        final ScribbleSettings s = new ScribbleSettings(form.getTitle(), form.getBoardLanguage(), form.getDaysPerMove());
        if (form.getOpponentType() == OpponentType.ROBOT) {
            final ScribbleBoard board = boardManager.createBoard(s, Arrays.asList(principal, RobotPlayer.valueOf(form.getRobotType())));
            return "redirect:/playground/scribble/board?b=" + board.getBoardId();
        } else if (form.getOpponentType() == OpponentType.WAIT) {
            createWaitAction(form, result, principal, s);
        } else if (form.getOpponentType() == OpponentType.CHALLENGE) {
            createChallengeAction(form, result, principal, s);
        }

        if (result.hasErrors()) {
            return createGamePage(form, result, model, locale);
        }
        return "redirect:/playground/scribble/active";
    }

    private void createWaitAction(CreateScribbleForm form, BindingResult result, Player principal, ScribbleSettings s) {
        final Comparable restriction = restrictionManager.getRestriction(principal, "scribble.opponents");
        if (form.getOpponentsCount() > (Integer) restriction) {
            result.rejectValue("opponentsCount", "game.create.opponents.err.count", new Object[]{restriction, "/account/membership"}, null);
        } else {
            GameRestriction r = null;
            if (form.getMinRating() != 0 || form.getMaxRating() != 0) {
                r = new GameRestrictionRating(form.getMinRating(), form.getMaxRating());
            }
            try {
                proposalManager.initiateWaitingProposal(s, principal, form.getOpponentsCount() + 1, r);
            } catch (Exception ex) {
                result.rejectValue("title", ex.getMessage());
            }
        }
    }

    private void createChallengeAction(CreateScribbleForm form, BindingResult result, Player principal, ScribbleSettings s) {
        final long[] opponents = form.getOpponents();
        if (opponents == null || opponents.length == 0) {
            result.rejectValue("opponents", "game.create.opponents.err.min");
        } else {
            final Comparable restriction = restrictionManager.getRestriction(principal, "scribble.opponents");
            if (opponents.length > (Integer) restriction) {
                result.rejectValue("opponentsCount", "game.create.opponents.err.count", new Object[]{restriction, "/account/membership"}, null);
            } else {
                final List<Player> players = new ArrayList<Player>(opponents.length);
                for (int i = 0, opponentsLength = opponents.length; i < opponentsLength; i++) {
                    long opponent = opponents[i];
                    if (opponent != 0) {
                        final Player player = playerManager.getPlayer(opponent);
                        if (player == null) {
                            result.rejectValue("opponents", "game.create.opponents.err.unknown", new Object[]{opponent}, null);
                        } else {
                            players.add(player);
                        }
                    }
                }

                if (!result.hasErrors()) {
                    try {
                        proposalManager.initiateChallengeProposal(s, principal, players);
                    } catch (Exception ex) {
                        result.rejectValue("title", ex.getMessage());
                    }
                }
            }
        }
    }

    @RequestMapping("join")
    public String showWaitingGames(Model model, Locale locale) throws RestrictionException {
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
        if (principal.getMembership().isAdsVisible()) {
            model.addAttribute("advertisementBlock", advertisementManager.getAdvertisementBlock("gameboard", Language.byLocale(locale)));
        }
        return "/content/playground/scribble/join";
    }

    @RequestMapping(value = "join", params = "p")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String joinGameAction(@RequestParam("p") String id, Model model, Locale locale) throws BoardManagementException, RestrictionException {
        if (log.isInfoEnabled()) {
            log.info("Join to game: " + id);
        }
        try {
            final Player principal = getPrincipal();
            restrictionManager.checkRestriction(principal, "games.active", getActiveGamesCount(principal));

            GameProposal<ScribbleSettings> proposal = proposalManager.getProposal(Long.valueOf(id));
            if (proposal != null) {
                for (Personality personality : proposal.getPlayers()) {
                    blacklistManager.checkBlacklist(personality, principal);
                }
            }

            proposal = proposalManager.attachPlayer(Long.valueOf(id), principal);
            if (proposal == null) {
                model.addAttribute("joinError", "game.error.restriction.ready.description");
            } else if (proposal.isReady()) {
                final ScribbleBoard board = boardManager.createBoard(proposal.getGameSettings(), proposal.getPlayers());
                return "redirect:/playground/scribble/board?b=" + board.getBoardId();
            }
        } catch (BlacklistedException e) {
            model.addAttribute("joinError", "game.error.restriction.blacklist.description");
        } catch (ViolatedRestrictionException e) {
            model.addAttribute("joinError", "game.error.restriction." + e.getCode() + ".description");
            model.addAttribute("joinErrorArgs", new Object[]{e.getActualValue(), e.getExpectedValue()});
        }
        return showWaitingGames(model, locale);
    }

    @RequestMapping(value = "decline", params = "p")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String declineGameAction(@RequestParam("p") String id, Model model, Locale locale) throws BoardManagementException, RestrictionException {
        if (log.isInfoEnabled()) {
            log.info("Decline game: " + id);
        }
//        try {
//            final Player principal = getPrincipal();
//            restrictionManager.checkRestriction(principal, "games.active", getActiveGamesCount(principal));

/*
            GameProposal<ScribbleSettings> proposal = proposalManager.getProposal(Long.valueOf(id));
            if (proposal != null) {
                for (Personality personality : proposal.getPlayers()) {
                    blacklistManager.checkBlacklist(personality, principal);
                }
            }

            proposal = proposalManager.attachPlayer(Long.valueOf(id), principal);
            if (proposal == null) {
                model.addAttribute("joinError", "game.error.restriction.ready.description");
            } else if (proposal.isReady()) {
                final ScribbleBoard board = boardManager.createBoard(proposal.getGameSettings(), proposal.getPlayers());
                return "redirect:/playground/scribble/board?b=" + board.getBoardId();
            }
        } catch (BlacklistedException e) {
            model.addAttribute("joinError", "game.error.restriction.blacklist.description");
        } catch (ViolatedRestrictionException e) {
            model.addAttribute("joinError", "game.error.restriction." + e.getCode() + ".description");
            model.addAttribute("joinErrorArgs", new Object[]{e.getActualValue(), e.getExpectedValue()});
        }
*/
        return showWaitingGames(model, locale);
    }

    @RequestMapping("active")
    public String showActiveGames(Model model, Locale locale) {
        final Player principal = getPrincipal();
        if (log.isDebugEnabled()) {
            log.debug("Loading active games for personality: " + principal);
        }
        final Collection<ScribbleBoard> activeBoards = boardManager.getActiveBoards(principal);
        if (log.isDebugEnabled()) {
            log.debug("Found " + activeBoards.size() + " active games for personality: " + principal);
        }
        final Collection<GameProposal<ScribbleSettings>> proposals = proposalManager.getPlayerProposals(principal);
        if (log.isDebugEnabled()) {
            log.debug("Found " + proposals.size() + " proposals for personality: " + principal);
        }
        model.addAttribute("activeBoards", activeBoards);
        model.addAttribute("activeProposals", proposals);
        if (principal.getMembership().isAdsVisible()) {
            model.addAttribute("advertisementBlock", advertisementManager.getAdvertisementBlock("dashboard", Language.byLocale(locale)));
        }
        return "/content/playground/scribble/active";
    }

    @ResponseBody
    @RequestMapping("cancel")
    public ServiceResponse cancelProposal(@RequestParam("p") String proposal, Model model, Locale locale) {
        final Player player = getPrincipal();
        if (log.isDebugEnabled()) {
            log.debug("Cancel proposal " + proposal + " for player " + player);
        }

        try {
            proposalManager.detachPlayer(Long.valueOf(proposal), player);
            return ServiceResponse.SUCCESS;
        } catch (NumberFormatException ex) {
            return ServiceResponse.failure("format");
        } catch (ViolatedRestrictionException e) {
            return ServiceResponse.failure("active");
        }
    }

    private int getActiveGamesCount(Player principal) {
        return searchesEngine.getActiveBoardsCount(principal) + proposalManager.getPlayerProposals(principal).size();
    }

    @Autowired
    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Autowired
    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
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
    public void setSearchesEngine(ScribbleSearchesEngine searchesEngine) {
        this.searchesEngine = searchesEngine;
    }

    @Autowired
    public void setRestrictionManager(RestrictionManager restrictionManager) {
        this.restrictionManager = restrictionManager;
    }

    @Autowired
    public void setAdvertisementManager(AdvertisementManager advertisementManager) {
        this.advertisementManager = advertisementManager;
    }

    @Autowired
    public void setProposalManager(GameProposalManager<ScribbleSettings> proposalManager) {
        this.proposalManager = proposalManager;
    }
}
