package view;

import model.board.Position;
import view.data.PlayerChoice;
import view.data.PlayerInfos;
import view.listeners.GameViewListenable;
import view.listeners.GameViewListener;
import view.utils.InputValidator;

import java.util.*;

public class GameView extends AbstractView implements GameViewListenable, GameViewable {

    private final static List<GameViewListener> listeners = new ArrayList<>();
    private List<String> tokens;
    private List<String> usedTokens = new ArrayList<>();
    private final String gameName;
    private final boolean hasRow;

    public GameView(List<String> tokens, String gameName, boolean hasRow) {
        this.tokens = List.copyOf(tokens);
        this.gameName = gameName;
        this.hasRow = hasRow;
    }

    @Override
    public void restart() {
        this.usedTokens.clear();
    }

    @Override
    public void showRestartMenu() {
        show("Game ended, what would you like to do?");
        show("r - restart");
        show("q - quit");
        String answer = waitForAnswer();
        if(answer.equals("r")) {
            fireOnRestartAsked();
        } else if (answer.equals("q")) {
            fireOnQuitAsked();
        } else {
            show("Sorry I don't understand");
            showStartMenu();
        }
    }

    @Override
    public void showStartMenu() {
        show("Welcome to "+gameName);
    }

    @Override
    public void showPlayerCreationMenu(int num) {
        show("Create player "+ (num+1) + ":");
        show("h - Human");
        show("a - AI");
        show("q - quit");
        String answer = waitForAnswer();
        switch (answer) {
            case "q" -> fireOnQuitAsked();
            case "h" -> showHumanPlayerCreationMenu();
            case "a" -> fireOnAiPlayerCreated(popAvailableToken());
            default -> {
                showError("Sorry, I don't understand that.");
                showPlayerCreationMenu(num);
            }
        }
    }

    private void showHumanPlayerCreationMenu(){
        show("Please enter your name:");
        String name = waitForAnswer();
        String pawn = showChoosePawn();
        fireOnHumanPlayerCreated(new PlayerInfos(pawn, name));
    }

    private String showChoosePawn(){
        show("Please choose a pawn");
        for (String t: getAvailableTokens()) {
            show(t);
        }
        String pawn = waitForAnswer();
        if(usedTokens.contains(pawn)) {
            showError("Sorry, the token is not available");
            showChoosePawn();
        }
        usedTokens.add(pawn);
        return pawn;
    }

    private List<String> getAvailableTokens(){
        List<String> tokens = new ArrayList<>();
        for (String s: this.tokens) {
            if(!usedTokens.contains(s)) {
                tokens.add(s);
            }
        }
        return tokens;
    }

    private String popAvailableToken(){
        for (String t: tokens) {
            if(!usedTokens.contains(t)) {
                usedTokens.add(t);
                return t;
            }
        }
        return null;
    }

    @Override
    public void showChoiceMenu(PlayerInfos infos) {
        show("Hello "+infos.name()+", where do you want to play? ("+infos.token()+")");
        show("r - restart");
        show("p - play");
        show("q - quit");
        String answer = waitForAnswer();
        switch (answer) {
            case "r" -> fireOnRestartAsked();
            case "q" -> fireOnQuitAsked();
            case "p" -> askPosition();
            default -> {
                showError("Sorry, I don't understand that.");
                showChoiceMenu(infos);
            }
        }
    }

    private void askPosition() {
        int col = waitForInt("column");
        int row = Position.DEFAULT;
        if(hasRow) row = waitForInt( "row");
        fireOnHumanCHoice(new PlayerChoice(new Position(col, row)));
    }

    private int waitForInt(String name){
        show("Choose "+name);
        String answer = waitForAnswer();
        if(InputValidator.isInt(answer)){
            return InputValidator.toInt(answer);
        } else {
            showError("Not a valid number");
            return waitForInt(name);
        }
    }

    @Override
    public void showBoard(String board) {
        show(board);
    }

    @Override
    public void showVictory(String name) {
        show("Congrats "+ name + "!");
    }

    @Override
    public void addGameViewListener(GameViewListener l) {
        if(l!=null && !listeners.contains(l)) listeners.add(l);
    }

    @Override
    public void removeGameViewListener(GameViewListener l) {
        if(l!=null) listeners.remove(l);
    }

    protected void fireOnQuitAsked(){
        for (GameViewListener l : listeners){l.onQuitAsked();}
    }

    protected void fireOnHumanPlayerCreated(PlayerInfos player){
        for (GameViewListener l : listeners){l.onHumanPlayerCreated(player);}
    }

    protected void fireOnAiPlayerCreated(String token){
        for (GameViewListener l : listeners){l.onAiPlayerCreated(token);}
    }

    protected void fireOnHumanCHoice(PlayerChoice choice){
        for (GameViewListener l : listeners) {l.onHumanChoice(choice);}
    }

    protected void fireOnRestartAsked(){
        for (GameViewListener l : listeners) {l.onRestartAsked();}
    }
}
