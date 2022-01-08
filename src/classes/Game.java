package classes;

import interfaces.Hive;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game implements Hive {
    private final Board board;
    public Move move;
    Player playerEnum = Player.WHITE;

    public Game() {
        this.board = new Board();
        this.board.createBoard();
        this.move = new Move(board);
    }

    public void nextPlayer(){
        playerEnum = (playerEnum == Player.WHITE) ? Player.BLACK : Player.WHITE;
    }

    /**
     * Play a new tileType.
     *
     * @param tile Tile to play
     * @param q    Q coordinate of hexagon to play to
     * @param r    R coordinate of hexagon to play to
     * @throws IllegalMove If the tileType could not be played
     */
    public void play(Tile tile, int q, int r) throws IllegalMove {
        List<classes.Tile> gameBoard = board.getBoard();
        classes.Tile playableTile = new classes.Tile(q, r, playerEnum, tile);
        boolean illegal = false;
        if (gameBoard.isEmpty()) {
            board.setTile(playableTile);
            return;
        } else if (board.getTile(q, r) != null) {
            illegal = true;
        } else {
            List<classes.Tile> neighbors = board.getNeighbors(playableTile);
            int count = 1;
            boolean queen = board.getQueen(playerEnum) != null;
            for (classes.Tile neighbor : neighbors){
                if (neighbor != null) {
                    count++;
                    if (Objects.equals(neighbor.getTileType(), Tile.QUEEN_BEE) && Objects.equals(neighbor.getPlayer(), playerEnum)) {
                        queen = true;
                    }

                    if ((!Objects.equals(neighbor.getPlayer(), playerEnum) || count >= 3) && !queen) {
                        illegal = true;
                        break;

                    }
                }
            }
            if (!illegal && gameBoard.size() > 1) {
                illegal = count == 0;
            }
        }
        if (allTilesPlayed(playerEnum)) {
            illegal = true;
        }
        if (illegal) {
            throw new IllegalMove("Illegal move, move not allowed.");
        } else {
            board.setTile(playableTile);
        }
    }
    /**
     * Move an existing tileType.
     *
     * @param fromQ Q coordinate of the tileType to move
     * @param fromR R coordinate of the tileType to move
     * @param toQ   Q coordinate of the hexagon to move to
     * @param toR   R coordinare of the hexagon to move to
     * @throws IllegalMove If the tileType could not be moved
     */
    public void move(int fromQ, int fromR, int toQ, int toR) throws IllegalMove {
        classes.Tile tile = board.getTile(fromQ, fromR);
        IllegalMove e = new IllegalMove("Illegal move, move not allowed.");
        if (move.tryMove(tile, fromQ, fromR, toQ, toR)) {
            tile.setCoords(toQ, toR);
        } else {
            throw e;
        }
    }

    /**
     * Pass the turn.
     *
     * @throws IllegalMove If the turn could not be passed
     */
    public void pass() throws IllegalMove {
        IllegalMove e = new IllegalMove("Illegal move, move not allowed.");
        if (!allTilesPlayed(playerEnum)) {
            throw e;
        }
        for (classes.Tile tile : board.getBoard()) {
            ArrayList<Coords> possibleMoves = move.getPossibleMoves(tile.getCoords().getQ(), tile.getCoords().getR());
        }
        throw new IllegalMove("Passing not allowed!");
    }

    /**
     * Check whether the given player is the winner.
     *
     * @param player Player to check
     * @return Boolean
     */
    public boolean isWinner(Player player) {
        classes.Tile queenTile = board.getQueen((playerEnum == player) ? Player.BLACK : Player.WHITE);

        if(queenTile == null) {
            return false;
        }
        else{
            return !board.getNeighbors(queenTile).contains(null);
        }
    }

    /**
     * Check whether the game is a draw.
     *
     * @return Boolean
     */
    public boolean isDraw() {
        return isWinner(Player.WHITE) && isWinner(Player.BLACK);
    }

    public boolean allTilesPlayed(Hive.Player player) {
        int grasshopper = 3;
        int soldierAnt = 3;
        int beetle = 2;
        int spider = 2;
        int queenBee = 1;

        for (classes.Tile entry : board.getBoard()) {
            if (entry.getPlayer() == player) {
                switch (entry.getTileType()) {
                    case GRASSHOPPER -> grasshopper--;
                    case SOLDIER_ANT -> soldierAnt--;
                    case BEETLE -> beetle--;
                    case SPIDER -> spider--;
                    case QUEEN_BEE -> queenBee--;
                }
            }
        }

        return (grasshopper == 0 && soldierAnt == 0 && beetle == 0 && spider == 0 && queenBee == 0);
    }


    //Todo: Create function for Pass movement:
    // Pass for soldier ant and spider needs to be implemented
    // refactor code, below method is non functional.
    public ArrayList<Coords> getPossibleMoves(int q, int r) {
        return move.getPossibleMoves(q, r);
    }

}
