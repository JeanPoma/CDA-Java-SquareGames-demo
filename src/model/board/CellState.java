package model.board;

import java.util.*;

public enum CellState {
    EMPTY(" "),
    X("X"),
    O("O"),
    R("R"),
    J("J"),
    B("B"),
    N("N")
    ;

    private final String representation;

    CellState(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }

    public static CellState getCellState(String representation) {
        return Arrays.stream(CellState.values()).filter(cellState -> cellState.getRepresentation().equals(representation)).findFirst().orElse(null);
    }

    public static List<String> getAllRepresentations() {
        List<String> representations= new ArrayList<>();
        for (CellState cellState : CellState.values()) {
            representations.add(cellState.getRepresentation());
        }
        return representations;
    }
}
