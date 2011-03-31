package wisematches.server.web.player.impl;

import wisematches.server.player.Language;
import wisematches.server.player.Membership;
import wisematches.server.player.Player;
import wisematches.server.player.PlayerManager;
import wisematches.server.player.computer.guest.GuestPlayer;
import wisematches.server.player.computer.robot.RobotPlayer;
import wisematches.server.standing.rating.PlayerRatingManager;
import wisematches.server.web.player.WebPlayer;
import wisematches.server.web.player.WebPlayerManager;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WebPlayerManagerImpl implements WebPlayerManager {
	private PlayerManager playerManager;
	private PlayerRatingManager ratingManager;

	private Map<Player, WebPlayer> playerMap = new WeakHashMap<Player, WebPlayer>();

	public WebPlayerManagerImpl() {
	}

	@Override
	public WebPlayer getPlayer(long playerId) {
		return getPlayer(playerManager.getPlayer(playerId));
	}

	@Override
	public WebPlayer getPlayer(Player player) {
		if (player == null) {
			return null;
		}

		WebPlayer webPlayer = playerMap.get(player);
		if (webPlayer == null) {
			webPlayer = new WebPlayerImpl(player);
			playerMap.put(player, webPlayer);
		}
		return webPlayer;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setRatingManager(PlayerRatingManager ratingManager) {
		this.ratingManager = ratingManager;
	}

	private class WebPlayerImpl implements WebPlayer {
		private final Player player;

		private WebPlayerImpl(Player player) {
			this.player = player;
		}

		@Override
		public short getRating() {
			return ratingManager.getRating(player);
		}

		@Override
		public boolean isGuest() {
			return player instanceof GuestPlayer;
		}

		@Override
		public boolean isRobot() {
			return player instanceof RobotPlayer;
		}

		@Override
		public long getId() {
			return player.getId();
		}

		@Override
		public String getEmail() {
			return player.getEmail();
		}

		@Override
		public String getNickname() {
			return player.getNickname();
		}

		@Override
		public String getPassword() {
			return player.getPassword();
		}

		@Override
		public Language getLanguage() {
			return player.getLanguage();
		}

		@Override
		public Membership getMembership() {
			return player.getMembership();
		}
	}
}