package it.cnr.istc.msanbot.logic;

public enum LoggingTag {
    ELAPSED_TIME("ELAPSED TIME"),
    SYSTEM_TURNS("SYSTEM TURNS"),
    TOTAL_SYSTEM_TURNS("TOTAL SYSTEM TURNS"),
    USER_TURNS("USER TURNS"),
    TOTAL_USER_TURNS("TOTAL USER TURNS"),
    TOTAL_TURNS("TOTAL TURNS"),
    TIMEOUT("TIMEOUT"),
    REJECTS("REJECTS"),
    REPROMPT("REPROMPT"),
    NOANSWER("NOANSWER"),
    NO_USER_ANSWER("NO USER ANSWER"),
    CANCEL("CANCEL"),
    NOTE("NOTE"),
    BARGEINS("BARGEINS"),
    USER_CONNECTED("USER CONNECTED"),
    USER_DISCONNECTED("USER DISCONNECTED"),
    FACE("FACE"),
    TABLE("TABLE"),
    VIDEO("VIDEO"),
    IMG("IMG"),
    LINK("LINK"),
    CHANGE_USERNAME("CHANGE USERNAME"), //when the user change his username
    REC_BUTTON_PRESSED("REC BUTTON PRESSED BY USER"),
    WRONG_ANSWER("WRONG ANSWER"),
    NEGATIVE_ANS("NEGATIVE ANSWER"),
    BYPASS("BYPASS"),
    POSITIVE_ANS("POSITIVE ANSWER"),
    SPEAK("SPEAK"), //no need to be "helped"
    REPEAT("REPEAT");


    private LoggingTag(String tag) {
        this.tag = tag;
    }

    private String tag;

    /**
     * Ritorna il valore del tag circondato da minore e maggiore
     * @return il tag con maggiore e minore
     */
    public String getTag(){
        return "<" + tag + ">";
    }
}
