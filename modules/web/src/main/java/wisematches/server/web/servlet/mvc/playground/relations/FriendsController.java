package wisematches.server.web.servlet.mvc.playground.relations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Player;
import wisematches.server.services.relations.friends.FriendsManager;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.mvc.playground.relations.form.FriendRelationForm;
import wisematches.server.web.servlet.sdo.ServiceResponse;

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
		model.addAttribute("friends", friendsManager.getFriendsList(getPrincipal()));
		return "/content/playground/players/friends/view";
	}


	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ServiceResponse addFriend(@RequestBody FriendRelationForm form, Locale locale) {
		final Player player = personalityManager.getMember(form.getPerson());
		if (player == null) {
			return responseFactory.failure("friends.err.unknown", locale);
		}
		friendsManager.addFriend(getPrincipal(), player, form.getComment());
		return responseFactory.success();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public ServiceResponse removeFriend(@RequestParam(value = "persons[]") List<Long> removeList) {
		final Player personality = getPrincipal();
		for (Long id : removeList) {
			final Player player1 = personalityManager.getMember(id);
			friendsManager.removeFriend(personality, player1);
		}
		return responseFactory.success();
	}

	@Autowired
	public void setFriendsManager(FriendsManager friendsManager) {
		this.friendsManager = friendsManager;
	}
}
