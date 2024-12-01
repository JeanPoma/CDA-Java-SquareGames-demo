package model;

import java.util.*;

import model.board.*;
import model.player.AIPlayer;
import model.player.HumanPlayer;
import model.player.Player;

public class GameModel implements Playable {

    private final InteractableBoard board;
    private final int nbNeeded;
    private final int lengthWin;
    private final List<Player> players = new ArrayList<>();
    private Player currentPlayer;
    private List<Position> winingLine = new ArrayList<>();
    private Position winningStartPosition;
    private int winningStart = -1;
    private int neededPlayerNum = 0;

    public GameModel(InteractableBoard board, int nbNeeded, int lengthWin) {
        this.board = board;
        this.nbNeeded = nbNeeded;
        this.lengthWin = lengthWin;
    }

    @Override
    public String getBoardRepresentation() {
        return board.getRepresentation();
    }

    @Override
    public int getNbRows() {
        return this.board.getNbRows();
    }

    @Override
    public int getNbColumns() {
        return this.board.getNbCols();
    }

    @Override
    public int getNeededPlayerNum() {
        return neededPlayerNum;
    }

    @Override
    public boolean hasAllPlayers() {
        return neededPlayerNum>=getNbPlayersNeeded();
    }

    @Override
    public List<String> getAvailableTokens() {
        return CellState.getAllRepresentations();
    }

    @Override
    public int getNbPlayersNeeded() {
        return nbNeeded;
    }

    @Override
    public int getNbPlayers() {
        return players.size();
    }

    @Override
    public void createHuman(String name, String token) throws GameException{
        createNewPlayer(name, false, token);
    }

    @Override
    public void createAI(String token) throws GameException{
        createNewPlayer(getRandomAIName(), true, token);
    }

    private void createNewPlayer(String name, boolean autonomous, String token) throws GameException{
        if(players.size()>nbNeeded) throw new GameException("There are too many players");
        CellState state = CellState.getCellState(token);
        checkState(state);
        if(autonomous){
            players.add(new AIPlayer(getRandomAIName(), state));
        } else {
            players.add(new HumanPlayer(name, state));
        }
        neededPlayerNum++;
        if(players.size()==1) currentPlayer = players.getFirst();
    }

    private  String getRandomAIName(){
        List<String> names = Arrays.asList("i-robot", "T1000", "Siri", "ChatGpt", "C3-PO", "Kitt", "Marvin", "Data", "Bender", "Jarvis", "Ash", "Ava", "Skynet");
        int r = (int) (Math.random()* names.size());
        return names.get(r);
    }

    private void checkState(CellState state) throws GameException{
        if(state == null) throw new GameException("Unknown state");
        if(isCellStateUsed(state)) throw new GameException("Token is already used");

    }

    private boolean isCellStateUsed(CellState state){
        return players.stream().anyMatch(p->p.getToken() == state);
    }

    @Override
    public void switchPlayer() throws GameException {
        int index = players.indexOf(currentPlayer);
        index++;
        if(index>=players.size()) index=0;
        currentPlayer = players.get(index);
    }

    protected Player getCurrentPlayer(){
        return currentPlayer;
    }

    @Override
    public String getCurrentName() {
        return currentPlayer.getName();
    }

    @Override
    public String getCurrentToken() {
        return currentPlayer.getToken().getRepresentation();
    }

    @Override
    public boolean isCurrentAutonomous() {
        return currentPlayer.isAutonomous();
    }

    @Override
    public boolean occupy(Position position) throws BoardException, GameException {
        CellState state = currentPlayer.getToken();
        return board.occupy(position, state);
    }

    @Override
    public boolean occupy() throws GameException, BoardException {
        if(!currentPlayer.isAutonomous()) throw new GameException("Current player is not autonomous");
        return occupy(generatePosition());
    }

    private Position generatePosition(){
        List<Position> positions = board.getAvailablePositions();
        return positions.get((int) (Math.random()* positions.size()));
    }

    @Override
    public boolean hasWinner() {
        return checkLines() || checkCols() || checkFallingDiag() || checkRaisingDiag() ;
    }

    private boolean hasWinning(Map<Position, List<CellState>> map) {
         List<CellState> line =  map.values().stream()
                .filter(l -> l.size() >= lengthWin)
                .filter(l -> hasConsecutiveState(l, currentPlayer.getToken()))
                 .findFirst().orElse(null);
         if(line == null) return false;
         winningStartPosition = map.keySet().stream().filter(k->line.equals(map.get(k))).findFirst().orElse(null);
         winningStart = map.keySet().stream().toList().indexOf(winningStartPosition);
         return true;
    }

    private boolean checkLines(){
        boolean result =  hasWinning(board.getLinesStates());
        if(result){
            winningStartPosition = new Position(winningStartPosition.getCol()+winningStart, winningStartPosition.getRow());
            for (int i = 0; i < lengthWin; i++) {
                winingLine.add(new Position(winningStartPosition.getCol()+i, winningStartPosition.getRow()));
            }
        }
        return result;
    }

    private boolean checkCols(){
        boolean result =  hasWinning(board.getColsStates());
        if(result){
            winningStartPosition = new Position(winningStartPosition.getCol(), winningStartPosition.getRow()+winningStart);
            for (int i = 0; i < lengthWin; i++) {
                winingLine.add(new Position(winningStartPosition.getCol(), winningStartPosition.getRow()+i));
            }
        }
        return result;
    }

    private boolean checkFallingDiag(){
        boolean result = hasWinning(board.getFallingDiagStates());
        if(result){
            winningStartPosition = new Position(winningStartPosition.getCol()+winningStart, winningStartPosition.getRow()+winningStart);
            for (int i = 0; i < lengthWin; i++) {
                winingLine.add(new Position(winningStartPosition.getCol()+i, winningStartPosition.getRow()+i));
            }
        }
        return result;
    }

    private boolean checkRaisingDiag(){
        boolean result =  hasWinning(board.getRaisingDiagStates());
        if(result){
            winningStartPosition = new Position(winningStartPosition.getCol()-winningStart, winningStartPosition.getRow()+winningStart);
            for (int i = 0; i < lengthWin; i++) {
                winingLine.add(new Position(winningStartPosition.getCol()+i, winningStartPosition.getRow()-i));
            }
        }
        return result;
    }

    private boolean hasConsecutiveState(List<CellState> list, CellState state){
        if(list.size()<lengthWin) return false;
        int count = 0;
        for (CellState cellState : list) {
            if (cellState.equals(state)) {
                count++;
                if (count == lengthWin) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        return false;
    }

    @Override
    public boolean isFull() {
        return board.isFull();
    }

    @Override
    public boolean isEnded() {
        return hasWinner() || isFull();
    }

    @Override
    public List<Position> getWinningLine() {
        return Collections.unmodifiableList(winingLine);
    }

    @Override
    public BoardInfos getBoardInfos() {
        return board.getBoardInfos();
    }

    @Override
    public void reset() {
        board.clear();
        players.clear();
        neededPlayerNum=0;
    }


    public List<Position> getWiningLine() {
        return winingLine;
    }

    public void setWiningLine(List<Position> winingLine) {
        this.winingLine = winingLine;
    }
}
