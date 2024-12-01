package controller.games;

import controller.Controller;
import model.GameModel;
import model.Playable;
import model.board.Board;
import model.board.CellState;

import java.util.*;

public enum GameFactory {

    TICTACTOE(3, 3, 2, 3, false, Arrays.asList(CellState.X.getRepresentation(), CellState.O.getRepresentation()), "TicTacToe"),
    GOMOKU(19, 19, 2, 5, false, Arrays.asList(CellState.B.getRepresentation(), CellState.N.getRepresentation()), "Gomoku" ),
    CONNECT_FOUR(7, 6, 2, 4, true, Arrays.asList(CellState.J.getRepresentation(), CellState.R.getRepresentation()), "ConnectFour"),
    ;

    private final int nbCol;
    private final int nbRow;
    private final int nbPlayers;
    private final int nbWin;
    private final boolean fall;
    private final List<String> authorizedTokens = new ArrayList<>();
    private final String name;

    public Playable createModel(){
        return new GameModel(new Board(nbRow, nbCol, fall), nbPlayers, nbWin);
    }

    public List<String> getAuthorizedTokens() {
        return List.copyOf(authorizedTokens);
    }

    boolean geFall(){
        return fall;
    }

    GameFactory(int nbCol, int nbRow, int nbPlayers, int nbWin, boolean fall, List<String> authorizedTokens, String name) {
        this.nbCol = nbCol;
        this.nbRow = nbRow;
        this.nbPlayers = nbPlayers;
        this.nbWin = nbWin;
        this.fall = fall;
        this.authorizedTokens.addAll(authorizedTokens);
        this.name = name;
    }

    public Controller createController(){
        return new GameController(this);
    }

    public String getName() {
        return name;
    }

    public static List<String> getNames(){
        return Arrays.stream(GameFactory.values()).map(GameFactory::getName).toList();
    }

    public static GameFactory getFactory(String name){
        return Arrays.stream(GameFactory.values()).filter(gameFactory -> gameFactory.getName().equals(name)).findFirst().orElse(null)   ;
    }
}
