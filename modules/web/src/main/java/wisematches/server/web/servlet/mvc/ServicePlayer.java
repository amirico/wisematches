package wisematches.server.web.servlet.mvc;

import wisematches.core.Membership;
import wisematches.core.Personality;
import wisematches.playground.GameMessageSource;
import wisematches.server.services.state.PlayerStateManager;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
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
		this.membership = membership != null ? membership : Membership.BASIC;
	}

	public static ServicePlayer get(long pid, String nickname, Membership membership, PlayerStateManager stateManager) {
		throw new UnsupportedOperationException("commented");
//		return new ServicePlayer(pid, nickname, stateManager.isPlayerOnline(null), membership);
	}

	public static ServicePlayer get(Personality player, GameMessageSource source, PlayerStateManager stateManager, Locale locale) {
		throw new UnsupportedOperationException("commented");
/*
		ServicePlayer p = new ServicePlayer();
		p.id = player.getId();
		p.online = stateManager.isPlayerOnline(player);
		p.nickname = source.getPersonalityNick(player, locale);
		p.membership = null;//player.g();
		return p;
*/
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
