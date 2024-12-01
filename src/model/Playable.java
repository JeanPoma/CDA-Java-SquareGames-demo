package model;

import model.board.BoardException;
import model.board.BoardInfos;
import model.board.Position;
import java.util.*;

public interface Playable {

    String getBoardRepresentation();
    int getNbRows();
    int getNbColumns();
    int getNeededPlayerNum();
    boolean hasAllPlayers();

    List<String> getAvailableTokens();

    int getNbPlayersNeeded();
    int getNbPlayers();
    void createHuman(String name, String token) throws GameException;
    void createAI(String token) throws GameException;
    void switchPlayer() throws GameException;
    String getCurrentName();
    String getCurrentToken();
    boolean isCurrentAutonomous();

    boolean occupy(Position position) throws BoardException, GameException;
    boolean occupy() throws GameException, BoardException;

    boolean hasWinner();
    boolean isFull();
    boolean isEnded();
    List<Position> getWinningLine();
    BoardInfos getBoardInfos();

    void reset();

}
