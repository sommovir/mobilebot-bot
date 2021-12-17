package it.cnr.istc.msanbot;

import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.beans.wheelmotion.NoAngleWheelMotion;
import com.sanbot.opensdk.function.unit.HardWareManager;
import com.sanbot.opensdk.function.unit.SystemManager;

import java.util.LinkedList;
import java.util.List;

import it.cnr.istc.msanbot.logic.FaceType;
import it.cnr.istc.msanbot.logic.RobotEventListener;

public class RobotManager {

    private static RobotManager _instance = null;
    private List<RobotEventListener> robotEventListeners = new LinkedList<>();

    private SystemManager systemManager;

    public static RobotManager getInstance() {
        if (_instance == null) {
            _instance = new RobotManager();
        }
        return _instance;
    }

    private RobotManager() {

    }

    public void addRobotEventListener(RobotEventListener listener) {
        this.robotEventListeners.add(listener);
    }

    public void setSystemManager(SystemManager systemManager) {
        this.systemManager = systemManager;
    }

    /**
     * Traduce l'enum FaceType nostra nell'enum EmotionType del sanbot
     *
     * @param face
     * @return
     */
    public EmotionsType translateFace(FaceType face) {
        switch (face) {
            case NORMAL:
                return EmotionsType.NORMAL;
            case SAD:
                return EmotionsType.CRY;
            case LOVE:
                return EmotionsType.KISS;
            case LAUGH:
                return EmotionsType.SMILE;
            case OUTRAGE:
                return EmotionsType.ANGRY;
            case QUESTION:
                return EmotionsType.QUESTION;
            case CRY:
                return EmotionsType.CRY;
        }
        return EmotionsType.NORMAL;
    }


    public void changeFace(FaceType face, long delay) {
        switch (face) {
            case NORMAL:
                normalFaceAnimationStart(delay);
                break;
            case SAD:
                piangiAnimationStart(delay);
                break;

            case LOVE:
                kissAnimationStart(delay);
                break;

            case LAUGH:
                smileAnimationStart(delay);
                break;

            case OUTRAGE:
                angryAnimationStart(delay);
                break;

            case CRY:
                cryAnimationStart(delay);
                break;

            case QUESTION:
                questionAnimationStart(delay);
                break;


        }
    }

    private void normalFaceAnimationStart(long delay) {
        for (RobotEventListener listener : robotEventListeners) {
            listener.faceChanged(FaceType.NORMAL, delay);
        }
    }

    private void piangiAnimationStart(long delay) {
        for (RobotEventListener listener : robotEventListeners) {
            listener.faceChanged(FaceType.SAD, delay);
        }
    }

    private void kissAnimationStart(long delay) {
        for (RobotEventListener listener : robotEventListeners) {
            listener.faceChanged(FaceType.LOVE, delay);
        }
    }

    private void smileAnimationStart(long delay) {
        for (RobotEventListener listener : robotEventListeners) {
            listener.faceChanged(FaceType.LAUGH, delay);
        }
    }

    private void angryAnimationStart(long delay) {
        for (RobotEventListener listener : robotEventListeners) {
            listener.faceChanged(FaceType.OUTRAGE, delay);
        }
    }

    private void cryAnimationStart(long delay) {
        for (RobotEventListener listener : robotEventListeners) {
            listener.faceChanged(FaceType.CRY, delay);
        }
    }

    private void questionAnimationStart(long delay) {
        for (RobotEventListener listener : robotEventListeners) {
            listener.faceChanged(FaceType.QUESTION, delay);
        }
    }

    public void moveRobot(Byte angleWheelMotion, int speed, int duration){
        for(RobotEventListener listener : robotEventListeners){
            listener.move(angleWheelMotion,speed,duration);
        }
    }

    public void turnRobot(Byte angleWheelMotion, int speed, int grade){
        for(RobotEventListener listener : robotEventListeners){
            listener.turn(angleWheelMotion,speed,grade);
        }
    }

    public String getMovementDirection(String x){
        String y[] = x.split(">");

        return (String) y[2].subSequence(0, y[2].length() - 2);
    }

    public String getMovementVelocityTurn(String x){
        String y[] = x.split(">");

        return (String) y[3].subSequence(0, y[3].indexOf("<"));
    }

}
