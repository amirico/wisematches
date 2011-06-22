package wisematches.personality.player.computer;

import org.springframework.beans.factory.InitializingBean;
import wisematches.personality.player.computer.guest.GuestPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;

import java.lang.reflect.Field;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ComputerPlayerInitializer implements InitializingBean {
	public ComputerPlayerInitializer() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (Field field : RobotPlayer.class.getFields()) {
			field.get(null);
		}
		for (Field field : GuestPlayer.class.getFields()) {
			field.get(null);
		}
	}
}
