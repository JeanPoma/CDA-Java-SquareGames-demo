package view;

import view.data.PlayerInfos;
import java.util.*;

public interface GameViewable extends Viewable {

    void showStartMenu();
    void showPlayerCreationMenu(int num);
    void showChoiceMenu(PlayerInfos infos);
    void showBoard(String board);
    void showVictory(String name);
    void restart();
    void showRestartMenu();

}
