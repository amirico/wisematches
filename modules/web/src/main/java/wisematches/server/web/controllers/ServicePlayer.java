package wisematches.server.web.controllers;

import wisematches.core.Personality;
import wisematches.core.personality.Player;
import wisematches.core.personality.member.Membership;
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
	private Membership membership;

	private ServicePlayer() {
	}

	public ServicePlayer(long id, String nickname, boolean online, Membership membership) {
		this.id = id;
		this.online = online;
		this.nickname = nickname;
		this.membership = membership != null ? membership : Membership.DEFAULT;
	}

	public static ServicePlayer get(long pid, String nickname, Membership membership, PlayerStateManager stateManager) {
		return new ServicePlayer(pid, nickname, stateManager.isPlayerOnline(Personality.person(pid)), membership);
	}

	public static ServicePlayer get(Player player, GameMessageSource source, PlayerStateManager stateManager, Locale locale) {
		ServicePlayer p = new ServicePlayer();
		p.id = player.getId();
		p.online = stateManager.isPlayerOnline(player);
		p.nickname = source.getPlayerNick(player, locale);
		p.membership = player.getMembership();
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

	public Membership getMembership() {
		return membership;
	}
}
