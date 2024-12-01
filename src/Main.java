import controller.application.Application;
import controller.games.GameFactory;
import model.GameException;
import model.Playable;
import model.board.Board;
import model.board.BoardException;
import model.board.Position;

public class Main {
    public static void main(String[] args) {
        //testGame();
        new Application().start();
    }

    private static void testGame() {
        Playable game = GameFactory.CONNECT_FOUR.createModel();
        try {
            game.createAI("O");
            game.createAI("X");

            while(!game.isEnded()){
                System.out.println(game.occupy());
                System.out.println(game.getBoardRepresentation());
                if(!game.isEnded()) game.switchPlayer();
            }

            for(Position p : game.getWinningLine()){
                System.out.println("Col: " + p.getCol()+ " Row: "+p.getRow());
            }

        } catch (GameException | BoardException e) {
            System.err.println(e.getMessage());
        }
    }
}