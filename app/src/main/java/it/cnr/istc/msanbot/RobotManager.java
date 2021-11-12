package it.cnr.istc.msanbot;

import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.unit.HardWareManager;
import com.sanbot.opensdk.function.unit.SystemManager;

import it.cnr.istc.msanbot.logic.FaceType;

public class RobotManager {

    private static RobotManager _instance = null;

    SystemManager systemManager;

    public static RobotManager getInstance() {
        if(_instance == null){
            _instance = new RobotManager();
        }
        return _instance;
    }

    private RobotManager(){

    }

    public void setSystemManager(SystemManager systemManager) {
        this.systemManager = systemManager;
    }

    /**
     * Traduce l'enum FaceType nostra nell'enum EmotionType del sanbot
     * @param face
     * @return
     */
    public EmotionsType translateFace(FaceType face){
        switch (face){
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
        }
        return EmotionsType.NORMAL;
    }


    public void changeFace(FaceType face){
        EmotionsType sanbotFace = translateFace(face);
        if(systemManager!=null){
            systemManager.showEmotion(sanbotFace);
        }
        switch(face){
            case NORMAL: {
                normalFaceAnimationStart();
                break;
            }
            case SAD: {
                piangiAnimationStart();
                break;
            }
            case LOVE: {
                kissAnimationStart();
                break;
            }
            case LAUGH: {
                smileAnimationStart();
                break;
            }
            case OUTRAGE: {
                angryAnimationStart();
                break;
            }
        }

    }

    private void normalFaceAnimationStart(){
        //TODO normal face animation
    }

    private void piangiAnimationStart(){
        //TODO piangi face animation
    }

    private void kissAnimationStart(){
        //TODO kiss face animation
    }

    private void smileAnimationStart(){
        //TODO smile face animation
    }

    private void angryAnimationStart(){
        //TODO angry face animation
    }

}
