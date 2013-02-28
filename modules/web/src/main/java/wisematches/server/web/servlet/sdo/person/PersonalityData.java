package wisematches.server.web.servlet.sdo.person;

import wisematches.core.*;
import wisematches.server.services.state.PlayerStateManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class PersonalityData {
	private boolean online;
	private String nickname;
	private Personality personality;

	private PersonalityData(String nickname, Personality personality, boolean online) {
		this.nickname = nickname;
		this.personality = personality;
		this.online = online;
	}

	public static PersonalityData get(String nickname, Personality member, boolean online) {
		return new PersonalityData(nickname, member, online);
	}

	public static PersonalityData get(String nickname, Personality member, PlayerStateManager stateManager) {
		return new PersonalityData(nickname, member, stateManager.isPlayerOnline(member));
	}

	public long getId() {
		return personality.getId();
	}

	public String getNickname() {
		return nickname;
	}

	public PersonalityType getType() {
		return personality.getType();
	}

	public RobotType getRobotType() {
		if (personality instanceof Robot) {
			Robot robot = (Robot) personality;
			return robot.getRobotType();
		}
		return null;
	}

	public Membership getMembership() {
		if (personality instanceof Member) {
			Member member = (Member) personality;
			return member.getMembership();
		}
		return null;
	}

	public boolean isOnline() {
		return online;
	}
}
