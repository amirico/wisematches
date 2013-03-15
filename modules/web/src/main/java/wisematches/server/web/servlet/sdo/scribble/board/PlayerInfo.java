package wisematches.server.web.servlet.sdo.scribble.board;

import wisematches.server.web.servlet.sdo.person.PersonalityInfo;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerInfo {
	private ScoreInfo score;
	private PersonalityInfo info;

	public PlayerInfo(ScoreInfo score, PersonalityInfo info) {
		this.score = score;
		this.info = info;
	}

	public long getId() {
		return info.getId();
	}

	public ScoreInfo getScore() {
		return score;
	}

	public PersonalityInfo getInfo() {
		return info;
	}
}
