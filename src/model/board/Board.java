package model.board;

import java.util.*;

public class Board implements InteractableBoard{

    private final int nbRows;
    private final int nbCols;
    private final boolean fall;
    private final List<Cell> cells = new ArrayList<Cell>();

    public Board(int nbRows, int nbCols, boolean fall) {
        this.nbRows = Math.max(nbRows, 0);
        this.nbCols = Math.max(nbCols, 0);
        this.fall = fall;
        for (int i = 0; i < nbRows*nbCols; i++) { cells.add(new Cell()); }
    }

    @Override
    public boolean isFull() {
        return cells.stream().noneMatch(Cell::isEmpty);
    }

    @Override
    public void clear() {
        cells.forEach(Cell::clear);
    }

    @Override
    public int getNbRows() {
        return nbRows;
    }

    @Override
    public int getNbCols() {
        return nbCols;
    }

    protected Cell getCell(Position pos) throws BoardException {
        if(isFull()) throw new BoardException(pos);
        if(pos == null) pos = new Position(Position.DEFAULT, Position.DEFAULT);
        if(pos.getRow()>=nbRows || pos.getCol()>=nbCols) throw  new BoardException(pos);
        return cells.get(Math.max(pos.getRow(),0)*nbCols + pos.getCol());
    }

    @Override
    public boolean occupy(Position p, CellState state) throws BoardException {
        if(fall){
            int row = getFirstAvalaibleFallRow(p.getCol());
            if(row<0) throw  new BoardException(p);
            p = new Position(p.getCol(), row );
        }
        return getCell(p).occupy(state);
    }

    private int getFirstAvalaibleFallRow(int col){
        int row = nbRows-1;
        while (row >= 0 && !cells.get(row*nbCols+col).isEmpty()){
            row--;
        }
        return row;
    }

    @Override
    public boolean isCellFree(Position p) throws BoardException {
        return getCell(p).isEmpty();
    }

    @Override
    public CellState getCellState(Position p) throws BoardException {
        return getCell(p).getState();
    }

    @Override
    public String getRepresentation() {
        int prefix_length = 5;
        int col_length = 4;
        StringBuilder sb = new StringBuilder();
        while(sb.toString().length()<prefix_length) sb.append(" ");
        for (int i = 0; i < nbCols; i++) {
            StringBuilder c = new StringBuilder();
            c.append("  ");
            c.append(i);
            while(c.toString().length()<col_length) c.append(" ");
            sb.append(c.toString());
        }
        sb.append("\n");
        for (int i = 0; i < nbRows; i++) {
            if(!fall) sb.append(getLine());
            StringBuilder prefix = new StringBuilder(" " + i);
            while(prefix.toString().length()<prefix_length) prefix.append(" ");
            sb.append(prefix);
            for (int j = 0; j < nbCols; j++) {
                sb.append(cells.get(i*nbCols + j).getRepresentation());
            }
            sb.append("|\n");
        }
        sb.append(getLine());
        return sb.toString();
    }

    protected String getLine(){
        StringBuilder line = new StringBuilder();
        line.append("     ");
        for (int i = 0; i < nbCols; i++) {
            line.append(Cell.HORIZONTAL);
        }
        line.append("-\n");
        return line.toString();
    }

    @Override
    public Map<Position, List<CellState>> getLinesStates() {
        Map<Position, List<CellState>> linesStates = new HashMap<Position, List<CellState>>();
        for (int i = 0; i < nbRows; i++) {
            List<CellState> lines = new ArrayList<>();
            Position origin = new Position(0, i);
            for (int j = 0; j < nbCols; j++) {
                lines.add(cells.get(i*nbCols+ j).getState());
            }
            linesStates.put(origin, lines);
        }
        return linesStates;
    }

    @Override
    public Map<Position, List<CellState>> getColsStates() {
        Map<Position, List<CellState>> colsStates = new HashMap<Position, List<CellState>>();
        for (int i = 0; i < nbCols; i++) {
            List<CellState> lines = new ArrayList<>();
            Position origin = new Position(i, 0);
            for (int j = 0; j < nbRows; j++) {
                lines.add(cells.get(j*nbCols+ i).getState());
            }
            colsStates.put(origin, lines);
        }
        return colsStates;
    }

    @Override
    public Map<Position, List<CellState>> getRaisingDiagStates() {
        Map<Position, List<CellState>> diagsStates = new HashMap<Position, List<CellState>>();
        for (int i = 0; i < nbCols; i++) {
            for (int j = 0; j < nbRows; j++) {
                List<CellState> diag = new ArrayList<>();
                Position origin = new Position(i, j);
                int k = 0;
                while (i+k<nbCols && j-k>=0) {
                    diag.add(cells.get((j-k)*nbCols+ i+k).getState());
                    k++;
                }
                diagsStates.put(origin, diag);
            }
        }
        return diagsStates;
    }

    @Override
    public Map<Position, List<CellState>> getFallingDiagStates() {
        Map<Position, List<CellState>> diagsStates = new HashMap<Position, List<CellState>>();
        for (int i = 0; i < nbCols; i++) {
            for (int j = 0; j < nbRows; j++) {
                List<CellState> diag = new ArrayList<>();
                Position origin = new Position(i, j);
                int k = 0;
                while (i+k<nbCols && j+k<nbRows) {
                    diag.add(cells.get((j+k)*nbCols+ i+k).getState());
                    k++;
                }
                diagsStates.put(origin, diag);
            }
        }
        return diagsStates;
    }

    @Override
    public boolean canFall() {
        return fall;
    }

    @Override
    public List<Position> getAvailablePositions() {
        return cells.stream()
                .filter(Cell::isEmpty)
                .map(cells::indexOf)
                .map(i->new Position(i%nbCols, i/nbCols))
                .toList();
    }

    @Override
    public BoardInfos getBoardInfos() {
        return new BoardInfos(nbCols, nbRows, cells.stream().map(Cell::getState).toList());
    }

}
