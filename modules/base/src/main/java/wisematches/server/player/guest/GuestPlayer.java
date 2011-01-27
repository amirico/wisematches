package wisematches.server.player.guest;

import wisematches.server.player.Language;
import wisematches.server.player.Membership;
import wisematches.server.player.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GuestPlayer implements Player {
	private static final Player PLAYER = new GuestPlayer();

	private GuestPlayer() {
	}

	@Override
	public long getId() {
		return 0;
	}

	@Override
	public String getEmail() {
		return null;
	}

	@Override
	public String getNickname() {
		return "Guest";
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public Language getLanguage() {
		return Language.DEFAULT;
	}

	@Override
	public Membership getMembership() {
		return Membership.GUEST;
	}

	@Override
	public int getRating() {
		return 1200;
	}

	public static Player getPlayer() {
		return PLAYER;
	}
}
