package view.listeners;

import view.data.PlayerPosition;
import view.data.PlayerInfos;

public interface GameViewListener {

    void onQuitAsked();
    void onHumanPlayerCreated(PlayerInfos player);
    void onAiPlayerCreated(String token);
    void onHumanChoice(PlayerPosition choice);
    void onRestartAsked();

}
