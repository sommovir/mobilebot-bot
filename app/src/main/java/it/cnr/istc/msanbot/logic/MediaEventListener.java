package it.cnr.istc.msanbot.logic;

public interface MediaEventListener {

    public void showYoutubeVideoOnRobot(String videoLink);

    public void showImageOnRobot(String imageLink);

    public void showLinkOnRobot(String link);

    public void showTableOnRobot(String table);

    public void showCurrentTable();
}
