package wisematches.server.web.controllers.playground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.profile.PlayerProfile;
import wisematches.personality.profile.PlayerProfileManager;
import wisematches.playground.tracking.PlayerTrackingCenter;
import wisematches.playground.tracking.Statistics;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.form.PlayerInfoForm;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.friends.FriendsManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/search")
public class SearchController extends WisematchesController {
    private PlayerManager playerManager;
    private FriendsManager friendsManager;
    private GameMessageSource messageSource;
    private PlayerProfileManager profileManager;
    private PlayerTrackingCenter trackingCenter;

    public SearchController() {
    }

    @ResponseBody
    @RequestMapping("friends")
    public ServiceResponse loadFriendsList(Locale locale) {
        final Collection<Long> friendsIds = friendsManager.getFriendsIds(getPersonality());
        final Collection<PlayerInfoForm> forms = new ArrayList<PlayerInfoForm>();
        for (Long friendsId : friendsIds) {
            Player player = playerManager.getPlayer(friendsId);
            if (player != null) {
                Language primaryLanguage = null;
                PlayerProfile profile = profileManager.getPlayerProfile(player);
                if (profile != null) {
                    primaryLanguage = profile.getPrimaryLanguage();
                }
                if (primaryLanguage == null) {
                    primaryLanguage = player.getLanguage();
                }
                final Statistics ps = trackingCenter.getPlayerStatistic(player);
                forms.add(new PlayerInfoForm(
                        player.getId(),
                        player.getNickname(),
                        player.getMembership(),
                        primaryLanguage,
                        ps.getRating(),
                        ps.getActiveGames(),
                        ps.getFinishedGames(),
                        messageSource.formatMinutes(ps.getAverageMoveTime() / 1000 / 60, locale)
                ));
            }
        }
        return ServiceResponse.success(null, "players", forms);
    }

    @ResponseBody
    @RequestMapping("a")
    public ServiceResponse qwe(Model model) {
        return ServiceResponse.success(null, "aaData", new Object[][]{{1, "asdasd", 1244}, {2, "qwe423", 434}});
    }

    @Autowired
    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Autowired
    public void setFriendsManager(FriendsManager friendsManager) {
        this.friendsManager = friendsManager;
    }

    @Autowired
    public void setTrackingCenter(PlayerTrackingCenter trackingCenter) {
        this.trackingCenter = trackingCenter;
    }

    @Autowired
    public void setProfileManager(PlayerProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Autowired
    public void setMessageSource(GameMessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
