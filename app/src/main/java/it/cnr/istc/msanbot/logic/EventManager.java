package it.cnr.istc.msanbot.logic;

import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.wheelmotion.NoAngleWheelMotion;
import com.sanbot.opensdk.function.unit.SpeechManager;

import java.util.LinkedList;
import java.util.List;

import it.cnr.istc.msanbot.MainActivity;

public class EventManager {

    private boolean serverOnline = false;

    private static EventManager _instance = null;

    private List<ConnectionEventListener> connectionEventListenerList = new LinkedList<>();
    private List<MediaEventListener> mediaEventListeners = new LinkedList<>();
    private EventManager(){

    }

    public boolean isServerOnline() {
        return serverOnline;
    }

    public static EventManager getInstance(){
        if(_instance == null){
            _instance = new EventManager();
        }
        return _instance;
    }

    public void addMediaEventListener(MediaEventListener listener){
        System.out.println("LISTENER SI AGGIUNGE");
        this.mediaEventListeners.add(listener);
    }

    public void addConnectionEventListener(ConnectionEventListener listener){
        this.connectionEventListenerList.add(listener);
    }

    public void serverOnline(){
        this.serverOnline = true;
        for (ConnectionEventListener listener : connectionEventListenerList) {
            listener.serverOnline();
        }
    }

    public void serverOffline(){
        this.serverOnline = false;
        for (ConnectionEventListener listener : connectionEventListenerList) {
            listener.serverOffline();
        }
    }

    public void playYouTubeVideo(String id){
        for (MediaEventListener listener : mediaEventListeners) {
            System.out.println("CALLING LISTENER VIDEO");
            listener.showYoutubeVideoOnRobot(id);
        }
    }


    public void speak(String text){
        for (ConnectionEventListener listener : connectionEventListenerList) {
            System.out.println("CALLING LISTENER");
            listener.speak(text);
        }
    }

    public void forceAutoListen(Long autoListenDelay) {
        for (ConnectionEventListener listener : connectionEventListenerList) {
            System.out.println("CALLING LISTENER");
            listener.forceAutoListenDelay(autoListenDelay);
        }
    }

    public void showLink(String link) {
        for (MediaEventListener listener : mediaEventListeners) {
            listener.showLinkOnRobot(link);
        }
    }

    public void showImage(String link) {
        System.out.println("SIZE: " + mediaEventListeners.size());
        for (MediaEventListener listener : mediaEventListeners) {
            System.out.println("djsaoijfa");
            listener.showImageOnRobot(link);
        }
    }

    public void showTable(String table) {
        for (MediaEventListener listener : mediaEventListeners) {
            listener.showTableOnRobot(table);
        }
    }

}
