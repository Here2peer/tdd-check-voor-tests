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
        if (!allTilesPlayed(playerEnum)) {
            throw e;
        }
        for (classes.Tile tile : board.getBoard()) {
            ArrayList<Coords> possibleMoves = getPossibleMoves(tile.getCoords().getQ(), tile.getCoords().getR());
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

    private Board getBoard() {
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
        boolean swarmIsIntact = checkTileConnections(tile, swarm);
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
                    if (isPathAvailable(tile.getCoords(), toQ, toR, path)) {
                        // tile.setCoords(toQ, toR);
                        return true;
                    }
                }
                break;
            case SOLDIER_ANT:
                if (destinationIsNotOccupied) {
                    if (!moveToSameLocation) {
                        ArrayList<Coords> path = new ArrayList<>();
                        if(isPathAvailable(tile.getCoords(), toQ, toR, path)) {
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
                            if (isPathAvailable(tile.getCoords(), toQ, toR, path)) {
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

    public Boolean checkPath(int q, int r, int toQ, int toR) {
        return null;
    }

    public Boolean checkTileConnections(classes.Tile entry, ArrayList<classes.Tile> swarm) {
        boolean connected = false;
        if (swarm.size() == 1) {
            swarm.set(0, board.getTile(swarm.get(0).getCoords().getQ(), swarm.get(0).getCoords().getR()));
        }
        int boardSize = board.getBoard().size();

        List<classes.Tile> neighbors = board.getNeighbors(entry);

        //Create function for checking if tile is covered
        for (classes.Tile tile : board.getBoard()) {
            if (tile.getCoords().equals(entry.getCoords()) && tile != entry) {
                boardSize--;
            }
        }
        for (classes.Tile neighbor : neighbors) {
            if (!swarm.contains(neighbor) && neighbor != null) {
                swarm.add(neighbor);
                connected = checkTileConnections(neighbor, swarm);
            }
        }
        if (swarm.size() == boardSize) {
            connected = true;
        }
        return connected;
    }

    public ArrayList<Coords> noPathAvailable(classes.Tile entry, ArrayList<Coords> path) {
        int q = entry.getCoords().getQ();
        int r = entry.getCoords().getR();
        Coords firstEntry = new Coords(q, r);
        path.add(firstEntry);
        return path;
    }

    public boolean isPathAvailable(classes.Coords coords, int toQ, int toR, ArrayList<Coords> path) {
        Coords toCoords = new Coords(toQ, toR);
        classes.Tile entry = board.getTile(coords.getQ(), coords.getR());
        //Refactor, create swarm object always requiring a Tile as first entry
        ArrayList<classes.Tile> swarm = new ArrayList<>();

        boolean caseAllowed = true;

        List<Coords> neighborsCoords = entry.getCoords().getAllNeighborsCoords();
        List<classes.Tile> neighbors = board.getNeighbors(entry);

        if (path.size() == 0) {
            noPathAvailable(entry, path);
        }

        if (board.getBoard().size() <= 2) {
            return true;
        }

        if (entry.getCoords().equals(toCoords)) {
            if (checkTileConnections(entry, swarm)) {
                return true;
            }
        }

        for (int index=0; index < neighbors.size(); index++) {
            if(neighbors.get(index) == null) {

                for (classes.Tile neighbor : neighbors) {
                    if(neighbor != null && !neighborsCoords.get(index).equals(entry.getCoords())) {
                        if (neighbor.getCoords().equals(neighborsCoords.get(index))) {
                            caseAllowed = false;
                        }
                    }
                }
                if (caseAllowed) {
                    classes.Tile emptyTile = new classes.Tile();
                    emptyTile.setCoords(neighborsCoords.get(index));
                    if (!swarm.contains(entry)) {
                        swarm.add(entry);
                    }
                    if (checkTileConnections(emptyTile, swarm) && !path.contains(emptyTile.getCoords()) ) {
                        entry.setCoords(neighborsCoords.get(index));
                        Coords pathCoord = new Coords(emptyTile.getCoords());
                        path.add(pathCoord);
                        if (!isPathAvailable(entry.getCoords(), toQ, toR, path)) {
                            path.remove(pathCoord);
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
        if (path.size() != 0) {
            entry.setCoords(path.get(0));
            return entry.getCoords().equals(toCoords);
        } else {
            return false;
        }
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
    // refactor code, below code is non functional.
    public ArrayList<Coords> getPossibleMoves(int q, int r) {
        classes.Tile tile = board.getTile(q, r);
        int toR = tile.getCoords().getQ();
        int toQ = tile.getCoords().getR();

        ArrayList<Coords> path = new ArrayList<>();
        path.add(tile.getCoords());


        ArrayList<Coords> possibleMoves = new ArrayList<>();

        List<Coords> neighborsCoords = tile.getCoords().getAllNeighborsCoords();

        switch (tile.getTileType()){
            case QUEEN_BEE:
                for (Coords move : neighborsCoords) {
                    if (board.getTile(move.getQ(), move.getR()) == null) {
                        possibleMoves.add(move);
                    }
                }
                break;
            case BEETLE:
                for (Coords move : neighborsCoords) {
                    if (board.getTile(move.getQ(), move.getR()) != null ) {
                        possibleMoves.add(move);
                    } else if(isPathAvailable(tile.getCoords(), move.getQ(), move.getR(), path)){
                        possibleMoves.add(move);
                    }
                }
                break;
            case GRASSHOPPER:
                for (classes.Tile neighbor : board.getNeighbors(tile)) {
                    if (neighbor != null && !neighbor.equals(tile)) {
                        toQ = neighbor.getCoords().getQ();
                        toR = neighbor.getCoords().getR();

                        if(board.getTile(q+1, r) != null && !board.getTile(q+1, r).equals(tile)) {
                            while (board.getTile(toQ, toR) != null) {
                                toQ++;
                                if (board.getTile(toQ, toR) == null) {
                                    possibleMoves.add(new Coords(toQ, toR));
                                }
                            }
                        }
                        if(board.getTile(q-1, r) != null && !board.getTile(q-1, r).equals(tile)) {
                            toQ = neighbor.getCoords().getQ();
                            toR = neighbor.getCoords().getR();
                            while (board.getTile(toQ, toR) != null) {
                                toQ--;
                                possibleMoves.add(new Coords(toQ, toR));
                            }
                        }

                        if(board.getTile(q, r+1) != null && !board.getTile(q, r+1).equals(tile)) {
                            toQ = neighbor.getCoords().getQ();
                            toR = neighbor.getCoords().getR();
                            while (board.getTile(toQ, toR) != null) {
                                toR++;
                                possibleMoves.add(new Coords(toQ, toR));
                            }
                        }
                        if(board.getTile(q, r-1) != null && !board.getTile(q, r-1).equals(tile)) {
                            toQ = neighbor.getCoords().getQ();
                            toR = neighbor.getCoords().getR();
                            while (board.getTile(toQ, toR) != null) {
                                toR--;
                                possibleMoves.add(new Coords(toQ, toR));
                            }
                        }
                    }
                }
                break;
            case SPIDER:
                break;
            case SOLDIER_ANT:
                break;
        }

        return possibleMoves;
    }
}
