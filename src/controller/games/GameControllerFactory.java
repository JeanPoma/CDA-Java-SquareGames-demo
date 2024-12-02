package controller.games;

import controller.Controller;

public class GameControllerFactory {

    public static Controller createGameController(GameFactory factory){
        return new GameController(factory);
    }
}
