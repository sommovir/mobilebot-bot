package it.cnr.istc.msanbot.logic;

public enum FaceType {

    NORMAL,
    SAD,
    LOVE,
    CRY,
    LAUGH,
    QUESTION,
    OUTRAGE;

    public static FaceType of(String face){
        if(face.equals("love")) {
            return LOVE;
        }else if(face.equals("fun")){
            return LAUGH;
        }else if(face.equals("sad")){
            return SAD;
        }else if(face.equals("cry")){
            return CRY;
        }else if(face.equals("question")){
            return QUESTION;
        }else if(face.equals("rage")){
            return OUTRAGE;
        }else{
            return NORMAL;
        }
    }
}
