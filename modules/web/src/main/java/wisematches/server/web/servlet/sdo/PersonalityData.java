package wisematches.server.web.servlet.sdo;

import wisematches.core.*;
import wisematches.server.services.state.PlayerStateManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class PersonalityData {
	private boolean online;
	private Personality personality;

	private PersonalityData(Personality personality, boolean online) {
		this.personality = personality;
		this.online = online;
	}

	public static PersonalityData get(Personality member, boolean online) {
		return new PersonalityData(member, online);
	}

	public static PersonalityData get(Personality member, PlayerStateManager stateManager) {
		return new PersonalityData(member, stateManager.isPlayerOnline(member));
	}

	public long getId() {
		return personality.getId();
	}

	public String getNickname() {
		if (personality instanceof Member) {
			Member member = (Member) personality;
			return member.getNickname();
		}
		return null;
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
