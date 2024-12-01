package model.board;

import java.util.List;
import java.util.Map;

public interface InteractableBoard {

    boolean isFull();
    void clear();
    int getNbRows();
    int getNbCols();
    boolean occupy(Position p, CellState state) throws BoardException;
    boolean isCellFree(Position p) throws BoardException;
    CellState getCellState(Position p) throws BoardException;
    String getRepresentation();
    Map<Position, List<CellState>> getLinesStates();
    Map<Position, List<CellState>> getColsStates();
    Map<Position, List<CellState>> getRaisingDiagStates();
    Map<Position, List<CellState>> getFallingDiagStates();
    boolean canFall();
    List<Position> getAvailablePositions();
    BoardInfos getBoardInfos();
}
