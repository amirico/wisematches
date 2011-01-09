package wisematches.server.core.robot;

import wisematches.kernel.player.InternationalPlayer;
import wisematches.kernel.player.Player;
import wisematches.kernel.util.Language;

/**
 * This interface indicates that player is robot.
 * <p/>
 * {@code wisematches.server.core.robot.RobotPlayer} if localized player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface RobotPlayer extends Player, InternationalPlayer {
    /**
     * Returns localized robot.
     *
     * @param language
     * @return the localized robot.
     */
    RobotPlayer getNationalityPlayer(Language language);

    /**
     * Returns type of this robot.
     *
     * @return the type of the player.
     */
    RobotType getRobotType();
}