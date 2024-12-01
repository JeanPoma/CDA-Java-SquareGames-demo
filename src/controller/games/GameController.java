package controller.games;

import controller.Controller;
import model.GameException;
import model.Playable;
import model.board.BoardException;
import model.board.Position;
import view.GameView;
import view.GameViewable;
import view.data.PlayerChoice;
import view.data.PlayerInfos;
import view.listeners.GameViewListenable;
import view.listeners.GameViewListener;

public class GameController implements Controller, GameViewListener {

    private final Playable model;
    private final GameViewable view;
    private GameStates currentState;

    public GameController(GameFactory factory) {
        this.model = factory.createModel();
        this.view = new GameView(factory.getAuthorizedTokens(), factory.getName(), !factory.geFall());
        GameViewListenable listenable = (GameViewListenable) view;
        listenable.addGameViewListener(this);
    }

    @Override
    public void start() {
        currentState = GameStates.START;
        treatStates();
    }

    private void treatStates(){
        switch(currentState){
            case QUIT -> sayGoodbye();
            case START -> treatStart();
            case RESTART -> restart();
            case CHANGE_PLAYER -> change_player();
            case ERROR_CREATION -> treatCreationError();
            case ERROR_CHOICE -> treatChoiceError();
            case PLAYER_CREATED -> onPlayerCreated();
            case CHOICE_DONE -> onChoiceDone();
            case NEED_PLAYER-> askNewPlayer();
            case TIE -> treatTie();
            case VICTORY -> showVictory();
            case NEED_CHOICE -> treatNeedChoice();
            case ENDED -> view.showRestartMenu();
        }

        if(currentState != GameStates.QUIT){
            treatStates();
        }
    }

    private void sayGoodbye() {
        view.show("Goodbye!");
    }

    private void askNewPlayer() {
        view.showPlayerCreationMenu(model.getNeededPlayerNum());
    }

    private void onPlayerCreated() {
        currentState = model.hasAllPlayers() ? GameStates.NEED_CHOICE : GameStates.NEED_PLAYER;
    }

    private void treatStart() {
        view.showStartMenu();
        currentState= GameStates.NEED_PLAYER;
    }

    private void treatNeedChoice(){
        view.show(model.getCurrentName()+" it's your turn");
        if(model.isCurrentAutonomous()){
            try {
                model.occupy();
                currentState = GameStates.CHOICE_DONE;
            } catch (Exception e) {
                view.showError(e.getMessage());
                currentState=GameStates.QUIT;
            }
        } else {
            view.showChoiceMenu(new PlayerInfos(model.getCurrentToken(), model.getCurrentName()));
        }
    }

    private void treatCreationError(){
        view.showError("Player creation error");
        currentState=GameStates.NEED_PLAYER;
    }

    private void treatChoiceError(){
        view.showError("Player choice error");
        currentState=GameStates.NEED_CHOICE;
    }

    private void treatTie(){
        view.show("Equality");
        currentState=GameStates.ENDED;
    }

    private void showVictory(){
        view.showVictory(model.getCurrentName());
        view.show("");
        currentState=GameStates.ENDED;
    }

    private void change_player(){
        try {
            model.switchPlayer();
            currentState = GameStates.NEED_CHOICE;
        } catch (GameException e) {
            view.showError(e.getMessage());
            currentState=GameStates.QUIT;
        }
    }
    
    private void onChoiceDone(){
        view.showBoard(model.getBoardRepresentation());
        view.show(model.getCurrentName()+" your choice has been performed");
        view.show("");
        if(model.hasWinner()){
            currentState=GameStates.VICTORY;
        } else if (model.isFull()) {
            currentState=GameStates.TIE;
        } else {
            currentState=GameStates.CHANGE_PLAYER;
        }
    }

    private void restart() {
        view.show("Lets'restart");
        model.reset();
        view.restart();
        currentState = GameStates.START;
    }

    @Override
    public void onQuitAsked() {
        currentState=GameStates.QUIT;
    }

    @Override
    public void onHumanPlayerCreated(PlayerInfos player) {
        try {
            model.createHuman(player.name(), player.token());
            currentState = GameStates.PLAYER_CREATED;
        } catch (GameException e) {
            view.showError(e.getMessage());
            currentState = GameStates.ERROR_CREATION;
        }
    }

    @Override
    public void onAiPlayerCreated(String token) {
        try {
            model.createAI(token);
            currentState = GameStates.PLAYER_CREATED;
        } catch (GameException e) {
            view.showError(e.getMessage());
            currentState = GameStates.ERROR_CREATION;
        }
    }

    @Override
    public void onHumanChoice(PlayerChoice choice) {
        try {
            model.occupy(choice.p());
            currentState=GameStates.CHOICE_DONE;
        } catch (Exception e) {
            view.showError(e.getMessage());
            currentState = GameStates.ERROR_CHOICE;
        }
    }

    @Override
    public void onRestartAsked() {
        currentState=GameStates.RESTART;
    }
}
