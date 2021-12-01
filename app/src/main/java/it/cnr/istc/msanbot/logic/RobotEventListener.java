package it.cnr.istc.msanbot.logic;

import com.sanbot.opensdk.function.beans.wheelmotion.NoAngleWheelMotion;

public interface RobotEventListener {

    public void faceChanged(FaceType face);

    public void move(Byte angleWheelMotion, int speed, int duration);

    public void turn(Byte angleWheelMotion, int speed, int grade);
}
