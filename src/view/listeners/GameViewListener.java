package view.listeners;

import view.data.PlayerChoice;
import view.data.PlayerInfos;

public interface GameViewListener {

    void onQuitAsked();
    void onHumanPlayerCreated(PlayerInfos player);
    void onAiPlayerCreated(String token);
    void onHumanChoice(PlayerChoice choice);
    void onRestartAsked();

}
