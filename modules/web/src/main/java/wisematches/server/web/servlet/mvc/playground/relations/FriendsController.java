package wisematches.server.web.servlet.mvc.playground.relations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wisematches.core.Player;
import wisematches.server.services.relations.friends.FriendsManager;
import wisematches.server.web.servlet.mvc.DeprecatedResponse;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.mvc.playground.relations.form.FriendRelationForm;

import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/friends")
@Deprecated
public class FriendsController extends WisematchesController {
	private FriendsManager friendsManager;

	public FriendsController() {
	}

	@RequestMapping("view")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String viewBlacklist(Model model) {
		model.addAttribute("friends", friendsManager.getFriendsList(getPlayer()));
		return "/content/playground/players/friends/view";
	}


	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public DeprecatedResponse addFriend(@RequestBody FriendRelationForm form, Locale locale) {
		final Player player = personalityManager.getMember(form.getPerson());
		if (player == null) {
			return DeprecatedResponse.failure(messageSource.getMessage("friends.err.unknown", locale));
		}
		friendsManager.addFriend(getPlayer(), player, form.getComment());
		return DeprecatedResponse.SUCCESS;
	}

	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public DeprecatedResponse removeFriend(@RequestParam(value = "persons[]") List<Long> removeList) {
		final Player personality = getPlayer();
		for (Long id : removeList) {
			final Player player1 = personalityManager.getMember(id);
			friendsManager.removeFriend(personality, player1);
		}
		return DeprecatedResponse.SUCCESS;
	}

	@Autowired
	public void setFriendsManager(FriendsManager friendsManager) {
		this.friendsManager = friendsManager;
	}
}
