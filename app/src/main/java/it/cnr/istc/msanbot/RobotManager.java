package it.cnr.istc.msanbot;

import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.EmotionsType;
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


    public void changeFace(FaceType face) {
        EmotionsType sanbotFace = translateFace(face);
        if (systemManager != null) {
            systemManager.showEmotion(sanbotFace);
        }
        switch (face) {
            case NORMAL:
                normalFaceAnimationStart();
                break;
            case SAD:
                piangiAnimationStart();
                break;

            case LOVE:
                kissAnimationStart();
                break;

            case LAUGH:
                smileAnimationStart();
                break;

            case OUTRAGE:
                angryAnimationStart();
                break;

            case CRY:
                cryAnimationStart();
                break;

            case QUESTION:
                questionAnimationStart();
                break;


        }
    }

    private void normalFaceAnimationStart() {
        for (RobotEventListener listener : robotEventListeners) {
            listener.FaceChanged(FaceType.NORMAL);
        }
    }

    private void piangiAnimationStart() {
        for (RobotEventListener listener : robotEventListeners) {
            listener.FaceChanged(FaceType.SAD);
        }
    }

    private void kissAnimationStart() {
        for (RobotEventListener listener : robotEventListeners) {
            listener.FaceChanged(FaceType.LOVE);
        }
    }

    private void smileAnimationStart() {
        for (RobotEventListener listener : robotEventListeners) {
            listener.FaceChanged(FaceType.LAUGH);
        }
    }

    private void angryAnimationStart() {
        for (RobotEventListener listener : robotEventListeners) {
            listener.FaceChanged(FaceType.OUTRAGE);
        }
    }

    private void cryAnimationStart() {
        for (RobotEventListener listener : robotEventListeners) {
            listener.FaceChanged(FaceType.CRY);
        }
    }

    private void questionAnimationStart() {
        for (RobotEventListener listener : robotEventListeners) {
            listener.FaceChanged(FaceType.QUESTION);
        }
    }

}
