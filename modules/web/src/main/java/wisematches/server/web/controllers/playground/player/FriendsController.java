package wisematches.server.web.controllers.playground.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wisematches.core.Personality;
import wisematches.core.personality.player.MemberPlayerManager;
import wisematches.playground.friends.FriendsManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.player.form.FriendRelationForm;

import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/friends")
public class FriendsController extends WisematchesController {
	private MemberPlayerManager playerManager;
	private FriendsManager friendsManager;

	public FriendsController() {
	}

	@RequestMapping("view")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String viewBlacklist(Model model) {
		model.addAttribute("friends", friendsManager.getFriendsList(getPrincipal()));
		return "/content/playground/players/friends/view";
	}


	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ServiceResponse addFriend(@RequestBody FriendRelationForm form, Locale locale) {
		final Personality player = playerManager.getPlayer(form.getPerson());
		if (player == null) {
			return ServiceResponse.failure(gameMessageSource.getMessage("friends.err.unknown", locale));
		}
		friendsManager.addFriend(getPersonality(), player, form.getComment());
		return ServiceResponse.SUCCESS;
	}

	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public ServiceResponse removeFriend(@RequestParam(value = "persons[]") List<Long> removeList) {
		final Personality personality = getPersonality();
		for (Long id : removeList) {
			friendsManager.removeFriend(personality, Personality.person(id));
		}
		return ServiceResponse.SUCCESS;
	}

	@Autowired
	public void setPlayerManager(MemberPlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setFriendsManager(FriendsManager friendsManager) {
		this.friendsManager = friendsManager;
	}
}
