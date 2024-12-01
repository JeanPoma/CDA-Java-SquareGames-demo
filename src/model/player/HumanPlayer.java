package model.player;

import model.board.CellState;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, CellState token) {
        super(false, name, token);
    }
}
