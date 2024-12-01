package model.board;
import java.util.*;
public record BoardInfos(int nbCols, int nbRows, List<CellState> states) {
}
