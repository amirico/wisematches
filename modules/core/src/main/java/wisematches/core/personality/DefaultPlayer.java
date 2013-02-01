package wisematches.core.personality;

import wisematches.core.Language;
import wisematches.core.Player;
import wisematches.core.PlayerType;
import wisematches.core.personality.player.account.Account;

import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class DefaultPlayer extends Player {
	private final String email;
	private final String nickname;
	private final TimeZone timeZone;
	private final Language language;
	private final PlayerType playerType;

	public DefaultPlayer(Account account, PlayerType playerType) {
		super(account.getId());
		this.nickname = account.getNickname();
		this.email = account.getEmail();
		this.timeZone = account.getTimeZone();
		this.language = account.getLanguage();
		this.playerType = playerType;
	}

	public DefaultPlayer(long id, String nickname, String email, TimeZone timeZone, PlayerType playerType, Language language) {
		super(id);
		this.email = email;
		this.nickname = nickname;
		this.timeZone = timeZone;
		this.language = language;
		this.playerType = playerType;
	}

	@Override
	public String getNickname() {
		return nickname;
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public TimeZone getTimeZone() {
		return timeZone;
	}

	@Override
	public PlayerType getPlayerType() {
		return playerType;
	}
}
