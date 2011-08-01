package wisematches.server.web.controllers.playground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.services.friends.FriendsManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/search")
public class SearchController extends WisematchesController {
    private FriendsManager friendsManager;

    public SearchController() {
    }

    @RequestMapping("friends")
    public String asd(Model model) {
        model.addAttribute("plain", Boolean.TRUE);
        model.addAttribute("friends", friendsManager.getFriendsList(getPersonality()));
        return "/content/playground/search/widget/friends";
    }

    @Autowired
    public void setFriendsManager(FriendsManager friendsManager) {
        this.friendsManager = friendsManager;
    }
}
