package model.player;

import model.board.CellState;

public class AIPlayer extends Player {

    public AIPlayer(String name, CellState token) {
        super(true, name, token);
    }
}
