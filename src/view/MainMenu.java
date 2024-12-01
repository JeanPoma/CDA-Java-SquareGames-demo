package view;

import view.listeners.MainMenuListenable;
import view.listeners.MainMenuListener;
import view.utils.InputValidator;

import java.util.*;

public class MainMenu extends AbstractView implements MainMenuListenable, MainMenuViewable {

    private final static String WELCOME_MESSAGE = "Welcome to SquareGames :)";

    private final List<MainMenuListener> listeners = new ArrayList<>();
    private final List<String> gameOptions;

    public MainMenu(List<String> gameOptions) {
        this.gameOptions = gameOptions;
    }

    @Override
    public void showMainMenu() {
        show(WELCOME_MESSAGE);
        show("Choose a game:");
        for (int i = 0; i < gameOptions.size(); i++) {
            show(i+ ": " +gameOptions.get(i));
        }
        show("q - quit Game");
        String answer = waitForAnswer();
        if(InputValidator.isInt(answer)){
            int choice = InputValidator.toInt(answer);
            if( choice >= 0 && choice< gameOptions.size()){
                fireOnGameChosen(gameOptions.get(choice));
            } else {
                showError("Choose a correct game");
                showMainMenu();
            }
        } else if (answer.equals("q")) {
            fireOnQuitAsked();
        } else {
            showError("Sorry I don't understand your request, please try again");
            showMainMenu();
        }
    }

    protected void fireOnQuitAsked(){
        for (MainMenuListener listener : listeners) {
            listener.onQuitAsked();
        }
    }

    protected void fireOnGameChosen(String name){
        for (MainMenuListener listener : listeners) {
            listener.onGameChosen(name);
        }
    }


    @Override
    public void addMainMenuListener(MainMenuListener listener) {
        if(listener != null) listeners.add(listener);
    }

    @Override
    public void removeMainMenuListener(MainMenuListener listener) {
        if(listener != null) listeners.remove(listener);
    }
}
