package classes;

import nl.hive.hanze.Hive;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game implements Hive {
    Player playerEnum = Player.WHITE;
    Tile tileEnum;

    private Board board;

    public Game() {
        this.board = new Board();
        this.board.createBoard();
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
            int count = 0;
            boolean queen = false;
            for (classes.Tile neighbor : neighbors){
                if (neighbor != null) {
                    count++;
                    if (Objects.equals(neighbor.getTileType(), Tile.QUEEN_BEE) && Objects.equals(neighbor.getPlayer(), playerEnum)) {
                        queen = true;
                    }
                    if ((!Objects.equals(neighbor.getPlayer(), playerEnum)) || (count >= 3) && !queen) {
                        illegal = true;
                        break;
                    }
                }
            }
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
        if (tryMove(tile, fromQ, fromR, toQ, toR)) {
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
        if (!board.allTilesPlayed(playerEnum)) {
            throw e;
        }
        for (classes.Tile tile : board.getBoard()) {
            ArrayList<Coords> possibleMoves = board.getPossibleMoves(tile);
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
        classes.Tile queenTile = board.getQueen(player);

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

    public Board getBoard() {
        return board;
    }

    public boolean tryMove(classes.Tile tile, int fromQ, int fromR, int toQ, int toR) throws IllegalMove {
        ArrayList<classes.Tile> swarm = new ArrayList<>();
        boolean moveToSameLocation = (fromQ == toQ) && (fromR == toR);
        boolean moveByOneAllowed = Math.abs(fromQ - toQ) <= 1 && Math.abs(fromR - toR) <= 1;
        boolean destinationIsNotOccupied = (board.getTile(toQ, toR) == null);
        boolean moveDone = false;

        // Needs refactoring
        tile.setCoords(toQ, toR);
        swarm.add(tile);
        boolean swarmIsIntact = board.checkTileConnections(tile, swarm);
        tile.setCoords(fromQ, fromR);

        if(!swarmIsIntact) {
            return false;
        }

        switch (tile.getTileType()) {
            case QUEEN_BEE:
                if(destinationIsNotOccupied) {
                    if (moveByOneAllowed) {
                        // tile.setCoords(toQ, toR);
                        return true;
                    }
                }
                break;
            case BEETLE:
                if (moveByOneAllowed) {
                    ArrayList<Coords> path = new ArrayList<>();
                    if (!destinationIsNotOccupied) {
                        return true;
                    }
                    if (board.isPathAvailable(tile, toQ, toR, path)) {
                        // tile.setCoords(toQ, toR);
                        return true;
                    }
                }
                break;
            case SOLDIER_ANT:
                if (destinationIsNotOccupied) {
                    if (!moveToSameLocation) {
                        ArrayList<Coords> path = new ArrayList<>();
                        if(board.isPathAvailable(tile, toQ, toR, path)) {
                            // tile.setCoords(toQ, toR);
                            return true;
                        }
                    }
                }
                break;
            case SPIDER:
                if (destinationIsNotOccupied) {
                    if (!moveToSameLocation) {
                        if (Math.abs(Math.abs(fromQ) - Math.abs(toQ)) + Math.abs(Math.abs(fromR) - Math.abs(toR)) == 3) {
                            ArrayList<Coords> path = new ArrayList<>();
                            if (board.isPathAvailable(tile, toQ, toR, path)) {
                                // tile.setCoords(toQ, toR);
                                return true;
                            }
                        }
                    }
                }
                break;
            case GRASSHOPPER:
                int moveDistanceQ = Math.abs(fromQ - toQ);
                int moveDistanceR = Math.abs(fromR - toR);
                boolean minusQ = toQ < fromQ;
                boolean minusR = toR < fromR;
                boolean qChanged = moveDistanceQ != 0;
                boolean illegal = false;
                int q = fromQ;
                int r = fromR;
                int step;

                if (destinationIsNotOccupied) {
                    if(moveDistanceQ == 0 || moveDistanceR == 0){
                        for (int i = 1; i < Math.abs(moveDistanceQ+moveDistanceR); i++) {
                            if (minusQ || minusR) {
                                step = i * -1;
                            } else {
                                step = i;
                            }

                            if (qChanged) {
                                q = fromQ + step;
                            } else {
                                r = fromR + step;
                            }
                            if (board.getTile(q, r) == null) {
                                illegal = true;
                                break;
                            }
                        }
                        if (!illegal) {
                            // tile.setCoords(toQ, toR);
                            moveDone = true;
                        }
                    }
                }
        }
        return moveDone;
    }

}
