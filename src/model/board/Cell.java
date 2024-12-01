package model.board;

public class Cell {

    private CellState state;
    public final static String HORIZONTAL = "----";

    public Cell() {
        state=CellState.EMPTY;
    }

    public String getRepresentation(){
        return "| " + state.getRepresentation()+ " ";
    }

    public CellState getState() {
        return state;
    }

    public boolean isEmpty() {
        return state==CellState.EMPTY;
    }

    public boolean occupy(CellState state) {
        if(state != null && isEmpty() && state!=CellState.EMPTY) {
            this.state=state;
            return true;
        }
        return false;
    }

    public void clear(){
        state=CellState.EMPTY;
    }
}
