package it.cnr.istc.msanbot;

import android.media.FaceDetector;

import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.beans.OperationResult;
import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.unit.SystemManager;

public class RobotMangaer {
    private static RobotMangaer instance = null;


    private RobotMangaer(){
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
