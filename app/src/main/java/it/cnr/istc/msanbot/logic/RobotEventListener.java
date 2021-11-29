package it.cnr.istc.msanbot.logic;

import com.sanbot.opensdk.function.beans.wheelmotion.NoAngleWheelMotion;

public interface RobotEventListener {

    public void FaceChanged(FaceType face);

    public void Move(NoAngleWheelMotion angleWheelMotion,int speed,int duration);
}
