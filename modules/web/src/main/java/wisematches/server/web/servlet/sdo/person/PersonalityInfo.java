package wisematches.server.web.servlet.sdo.person;

import wisematches.core.*;
import wisematches.server.services.state.PlayerStateManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PersonalityInfo {
	private boolean online;
	private String nickname;
	private Personality personality;

	public PersonalityInfo(String nickname, Personality personality, boolean online) {
		this.nickname = nickname;
		this.personality = personality;
		this.online = online;
	}

	public PersonalityInfo(String nickname, Personality member, PlayerStateManager stateManager) {
		this.nickname = nickname;
		this.personality = member;
		this.online = stateManager.isPlayerOnline(member);
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
