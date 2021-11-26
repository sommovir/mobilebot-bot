package it.cnr.istc.msanbot.logic;

import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.unit.SpeechManager;

import java.util.LinkedList;
import java.util.List;

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
            System.out.println("CALLING LISTENER");
            listener.showYoutubeVideo(id);
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
}
