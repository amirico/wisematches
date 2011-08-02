package wisematches.server.web.controllers.playground.form;

import wisematches.personality.Language;
import wisematches.personality.Membership;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerInfoForm {
	private final long playerId;
	private final String nickname;
	private final String language;
	private final String membership;
	private final int rating;

	public PlayerInfoForm(final long playerId, final String nickname, final Membership membership, final Language language, int rating) {
		this.playerId = playerId;
		this.nickname = nickname;
		this.language = language.code();
		this.membership = membership.name();
		this.rating = rating;
	}

	public long getPlayerId() {
		return playerId;
	}

	public String getNickname() {
		return nickname;
	}

	public String getLanguage() {
		return language;
	}

	public String getMembership() {
		return membership;
	}

	public int getRating() {
		return rating;
	}
}
