package controller.application;

import controller.Controller;
import controller.games.GameControllerFactory;
import controller.games.GameFactory;
import view.MainMenu;
import view.MainMenuViewable;
import view.listeners.MainMenuListenable;
import view.listeners.MainMenuListener;

public class Application implements Controller, MainMenuListener {

    private Controller gameController;
    private AppStates currentState;
    private MainMenuViewable view;
    private MainMenuListenable listenable;
    private GameFactory gameFactory;

    public Application() {
        view = new MainMenu(GameFactory.getNames());
        listenable = (MainMenuListenable) view;
        listenable.addMainMenuListener(this);
    }

    @Override
    public void start() {
        currentState = AppStates.START;
        treatState();
    }

    private void treatState(){
        switch(currentState){
            case START:
                view.showMainMenu();
                break;
            case GAME:
                if(gameFactory != null){
                    gameController = GameControllerFactory.createGameController(gameFactory);
                    if(gameController != null){
                        gameController.start();
                        currentState= AppStates.START;
                    } else {
                        view.showError("Sorry something gone wrong...");
                        currentState = AppStates.QUIT;
                    }
                } else {
                    view.showError("Please choose a game");
                    currentState = AppStates.START;
                }
                break;
            case UNKNOWN_GAME:
                view.showError("Unknown game");
                currentState = AppStates.START;
                break;
            case QUIT:
                view.show("See you soon!");
        }
        if(currentState != AppStates.QUIT){
            treatState();
        }
    }

    @Override
    public void onQuitAsked() {
        currentState = AppStates.QUIT;
    }

    @Override
    public void onGameChosen(String name) {
        GameFactory factory = GameFactory.getFactory(name);
        if(factory == null){
            currentState = AppStates.UNKNOWN_GAME;
        } else {
            gameFactory = factory;
            currentState = AppStates.GAME;
        }
    }
}
