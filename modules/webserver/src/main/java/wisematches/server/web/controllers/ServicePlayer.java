package wisematches.server.web.controllers;

import wisematches.personality.Membership;
import wisematches.personality.account.Account;
import wisematches.personality.player.Player;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ServicePlayer {
	private long id;
	private boolean online;
	private String nickname;
	private boolean robot;

	private ServicePlayer() {
	}

	public static ServicePlayer get(Account player, PlayerStateManager stateManager) {
		ServicePlayer p = new ServicePlayer();
		p.id = player.getId();
		p.online = stateManager.isPlayerOnline(player);
		p.nickname = player.getNickname();
		p.robot = false;
		return p;
	}

	public static ServicePlayer get(Player player, GameMessageSource source, PlayerStateManager stateManager, Locale locale) {
		ServicePlayer p = new ServicePlayer();
		p.id = player.getId();
		p.online = stateManager.isPlayerOnline(player);
		p.nickname = source.getPlayerNick(player, locale);
		p.robot = player.getMembership() == Membership.ROBOT || player.getMembership() == Membership.GUEST;
		return p;
	}

	public long getId() {
		return id;
	}

	public boolean isOnline() {
		return online;
	}

	public String getNickname() {
		return nickname;
	}

	public boolean isRobot() {
		return robot;
	}
}
