package it.cnr.istc.msanbot.logic;

public interface ConnectionEventListener {

    public void serverOnline();

    public void serverOffline();

    public void speak(String text);

    public void forceAutoListenDelay(Long autoListenDelay);
}
