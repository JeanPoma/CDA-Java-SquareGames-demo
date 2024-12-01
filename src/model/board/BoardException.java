package model.board;

public class BoardException extends Exception {

    public BoardException(Position pos) {
        super("Board position " +
                pos.getCol() + " " + pos.getRow() +
                " is off limit");
    }
}
