package wisematches.server.web.servlet.mvc.playground.relations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wisematches.core.Player;
import wisematches.server.services.relations.friends.FriendsManager;
import wisematches.server.web.servlet.mvc.ServiceResponse;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.mvc.playground.relations.form.FriendRelationForm;

import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/friends")
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
	public ServiceResponse addFriend(@RequestBody FriendRelationForm form, Locale locale) {
		final Player player = personalityManager.getPlayer(form.getPerson());
		if (player == null) {
			return ServiceResponse.failure(messageSource.getMessage("friends.err.unknown", locale));
		}
		friendsManager.addFriend(getPlayer(), player, form.getComment());
		return ServiceResponse.SUCCESS;
	}

	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public ServiceResponse removeFriend(@RequestParam(value = "persons[]") List<Long> removeList) {
		final Player personality = getPlayer();
		for (Long id : removeList) {
			final Player player1 = personalityManager.getPlayer(id);
			friendsManager.removeFriend(personality, player1);
		}
		return ServiceResponse.SUCCESS;
	}

	@Autowired
	public void setFriendsManager(FriendsManager friendsManager) {
		this.friendsManager = friendsManager;
	}
}
