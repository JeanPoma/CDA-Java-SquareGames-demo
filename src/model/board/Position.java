package model.board;

public class Position {

    public final static int DEFAULT=-1;

    private int col;
    private int row;

    public Position(int col) {
        this(col, DEFAULT);
    }

    public Position(int col, int row) {
        this.col=col;
        this.row=row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        if(col<0) this.col=DEFAULT;
        else this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        if (row<0) this.row = DEFAULT;
        else this.row = row;
    }
}
