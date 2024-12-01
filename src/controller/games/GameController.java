package controller.games;

import controller.Controller;
import model.GameException;
import model.Playable;
import model.board.Position;
import view.GameView;
import view.GameViewable;
import view.data.PlayerPosition;
import view.data.PlayerInfos;
import view.listeners.GameViewListenable;
import view.listeners.GameViewListener;
import java.util.*;

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
        List<Position> winningPositions = model.getWinningLine();
        Position start = winningPositions.getFirst();
        Position end = winningPositions.getLast();
        StringBuilder sb = new StringBuilder();
        sb.append("Your winning line start: (col: ");
        sb.append(start.getCol());
        sb.append(" & row: ");
        sb.append(start.getRow());
        sb.append(") and your line end: (col ");
        sb.append(end.getCol());
        sb.append(" & row ");
        sb.append(end.getRow());
        sb.append(")\n");
        view.show(sb.toString());
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
        model.reset();
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
    public void onHumanChoice(PlayerPosition choice) {
        try {
            if(model.occupy(new Position(choice.col(), choice.row()))){
                currentState=GameStates.CHOICE_DONE;
            } else {
                view.showError("The cell is occupied");
                currentState=GameStates.NEED_CHOICE;
            }
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
