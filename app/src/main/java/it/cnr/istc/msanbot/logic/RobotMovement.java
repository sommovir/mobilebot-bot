package it.cnr.istc.msanbot.logic;

import com.sanbot.opensdk.function.beans.wheelmotion.NoAngleWheelMotion;

public enum RobotMovement
{
    MOVE_FORWARD("MOVE_FORWARD"),
    MOVE_BACKWARD("MOVE_BACKWARD"),
    LEFT("left"),
    RIGHT("right"),
    STOP("STOP"),
    TURN_LEFT("TURN_LEFT"),
    TURN_RIGHT("TURN_RIGHT"),
    UNKNOWN("UNKNOWN");

    public static RobotMovement of(String movement){
        switch(movement){
            case "MOVE_FORWARD": return RobotMovement.MOVE_FORWARD;
            case "MOVE_BACKWARD": return RobotMovement.MOVE_BACKWARD;
            case "stop": return RobotMovement.STOP;
            case "TURN_LEFT": return RobotMovement.TURN_LEFT;
            case "TURN_RIGHT":return RobotMovement.TURN_RIGHT;
            default: return RobotMovement.UNKNOWN;
        }
    }

    private String robotMovement = "";

    private RobotMovement(String robotMovement) {
        this.robotMovement = robotMovement;
    }

    public byte toSanbotMovement(){
        RobotMovement newMovement = of(robotMovement);
        switch (newMovement){
            //case LEFT:return NoAngleWheelMotion.ACTION_LEFT;
            //case RIGHT:return NoAngleWheelMotion.ACTION_RIGHT;
            case MOVE_FORWARD:return NoAngleWheelMotion.ACTION_FORWARD;
            case MOVE_BACKWARD:return NoAngleWheelMotion.ACTION_BACK;
            case STOP:return NoAngleWheelMotion.ACTION_STOP;
            case TURN_LEFT:return NoAngleWheelMotion.ACTION_TURN_LEFT;
            case TURN_RIGHT:return NoAngleWheelMotion.ACTION_TURN_RIGHT;
            default:return NoAngleWheelMotion.ACTION_RESET;
        }
    }

}
