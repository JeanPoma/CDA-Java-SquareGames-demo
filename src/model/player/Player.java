package model.player;

import model.board.CellState;

public abstract class Player {

    private boolean autonomous;
    private String name;
    private CellState token;

    protected Player(boolean autonomous, String name, CellState token) {
        this.autonomous = autonomous;
        this.name = name;
        this.token = token;
    }

    public boolean isAutonomous() {
        return autonomous;
    }

    public String getName() {
        return name;
    }

    public CellState getToken() {
        return token;
    }

    public boolean changeToken(CellState newToken) {
        if(newToken != null && newToken.equals(this.token) && newToken != CellState.EMPTY) {
            this.token = newToken;
            return true;
        }
        return false;
    }
}
