package it.cnr.istc.msanbot.logic;

import com.sanbot.opensdk.function.beans.wheelmotion.NoAngleWheelMotion;

public enum RobotMovement
{
    UP("up"),
    DOWN("down"),
    LEFT("left"),
    RIGHT("right"),
    STOP("stop"),
    TURN_LEFT("turn_left"),
    TURN_RIGHT("turn_right"),
    UNKNOWN("unknown");

    public static RobotMovement of(String movement){
        switch(movement){
            case "up": return RobotMovement.UP;
            case "down": return RobotMovement.DOWN;
            case "left": return RobotMovement.LEFT;
            case "right": return RobotMovement.RIGHT;
            case "stop": return RobotMovement.STOP;
            case "turn:left": return RobotMovement.TURN_LEFT;
            case "turn_right":return RobotMovement.TURN_RIGHT;
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
            case LEFT:return NoAngleWheelMotion.ACTION_LEFT;
            case RIGHT:return NoAngleWheelMotion.ACTION_RIGHT;
            case UP:return NoAngleWheelMotion.ACTION_FORWARD;
            case DOWN:return NoAngleWheelMotion.ACTION_BACK;
            case STOP:return NoAngleWheelMotion.ACTION_STOP;
            case TURN_LEFT:return NoAngleWheelMotion.ACTION_TURN_LEFT;
            case TURN_RIGHT:return NoAngleWheelMotion.ACTION_TURN_RIGHT;
            default:return NoAngleWheelMotion.ACTION_RESET;
        }
    }
}
