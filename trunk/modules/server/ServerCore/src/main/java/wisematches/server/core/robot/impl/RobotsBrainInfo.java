package wisematches.server.core.robot.impl;

import wisematches.server.core.robot.RobotBrain;
import wisematches.server.core.robot.RobotType;
import wisematches.server.core.room.Room;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class RobotsBrainInfo {
    private final Room room;
    private final RobotType robotType;
    private final Class<RobotBrain> brainClass;

    public RobotsBrainInfo(Room room, RobotType robotType, Class<RobotBrain> brainClass) {
        this.room = room;
        this.robotType = robotType;
        this.brainClass = brainClass;
    }

    public Room getRoom() {
        return room;
    }

    public RobotType getRobotType() {
        return robotType;
    }

    public Class<RobotBrain> getBrainClass() {
        return brainClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RobotsBrainInfo that = (RobotsBrainInfo) o;
        return brainClass.equals(that.brainClass) && robotType == that.robotType && room.equals(that.room);
    }

    @Override
    public int hashCode() {
        int result = room.hashCode();
        result = 31 * result + robotType.hashCode();
        result = 31 * result + brainClass.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RobotsBrainInfo{" +
                "room=" + room +
                ", robotType=" + robotType +
                ", brainClass=" + brainClass +
                '}';
    }
}
