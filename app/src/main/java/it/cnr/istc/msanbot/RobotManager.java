package it.cnr.istc.msanbot;

import com.sanbot.opensdk.function.beans.EmotionsType;

import it.cnr.istc.msanbot.logic.FaceType;

public class RobotManager {

    private static RobotManager _instance = null;

    public static RobotManager getInstance() {
        if(_instance == null){
            _instance = new RobotManager();
        }
        return _instance;
    }

    private RobotManager(){
    }

    public EmotionsType changeFace(FaceType face){
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

}
