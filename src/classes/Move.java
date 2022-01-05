package classes;

import interfaces.Hive;

import java.util.ArrayList;
import java.util.List;

public class Move {
    private final Board board;

    public Move(Board board) {
        this.board = board;
    }
    // TODO:
    //  Use Open Closed principle as detailed here: https://java2blog.com/open-closed-principle-java/
    //  and here: https://blog.scottlogic.com/2016/07/28/java-enums-how-to-use-them-smarter.html
    //  Create a Move class.
    public boolean tryMove(Tile tile, int fromQ, int fromR, int toQ, int toR) {
        ArrayList<Tile> swarm = new ArrayList<Tile>();
        boolean moveToSameLocation = (fromQ == toQ) && (fromR == toR);
        boolean moveByOneAllowed = !((fromQ - toQ == -1 && fromR - toR == -1) || (fromQ - toQ == 1 && fromR - toR == 1));

        boolean destinationIsNotOccupied = (board.getTile(toQ, toR) == null);


        // Needs refactoring
        tile.setCoords(toQ, toR);
        swarm.add(tile);
        boolean swarmIsIntact = checkTileConnections(tile, swarm);
        tile.setCoords(fromQ, fromR);

        if (!swarmIsIntact) {
            return false;
        }

        if (tile.getTileType().equals(Hive.Tile.QUEEN_BEE)) {
            return tryMoveQueenBee(destinationIsNotOccupied, moveByOneAllowed);
        } else if (tile.getTileType().equals(Hive.Tile.BEETLE)) {
            return tryMoveBeetle(moveByOneAllowed, destinationIsNotOccupied, tile, toQ, toR);
        } else if (tile.getTileType().equals(Hive.Tile.SOLDIER_ANT)) {
            return tryMoveSoldierAnt(destinationIsNotOccupied, moveToSameLocation, tile, toQ, toR);
        } else if (tile.getTileType().equals(Hive.Tile.SPIDER)) {
            return tryMoveSpider(destinationIsNotOccupied, moveToSameLocation, tile, toQ, toR, fromQ, fromR);
        } else if (tile.getTileType().equals(Hive.Tile.GRASSHOPPER)) {
            return tryMoveGrasshopper(destinationIsNotOccupied, moveToSameLocation, toQ, toR, fromQ, fromR);
        }
        return false;
    }

    public Boolean checkTileConnections(Tile entry, ArrayList<Tile> swarm) {
        boolean connected = false;
        if (swarm.size() == 1) {
            swarm.set(0, board.getTile(swarm.get(0).getCoords().getQ(), swarm.get(0).getCoords().getR()));
        }
        int boardSize = board.getBoard().size();

        List<Tile> neighbors = board.getNeighbors(entry);

        //Create function for checking if tile is covered
        for (Tile tile : board.getBoard()) {
            if (tile.getCoords().equals(entry.getCoords()) && tile != entry) {
                boardSize--;
            }
        }
        for (Tile neighbor : neighbors) {
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

    public void noPathAvailable(Tile entry, ArrayList<Coords> path) {
        int q = entry.getCoords().getQ();
        int r = entry.getCoords().getR();
        Coords firstEntry = new Coords(q, r);
        path.add(firstEntry);
    }

    public boolean isPathAvailable(Coords coords, int toQ, int toR, ArrayList<Coords> path) {
        Coords toCoords = new Coords(toQ, toR);
        Tile entry = board.getTile(coords.getQ(), coords.getR());
        //Refactor, create swarm object always requiring a Tile as first entry
        ArrayList<Tile> swarm = new ArrayList<Tile>();

        boolean caseAllowed = true;

        List<Coords> neighborsCoords = entry.getCoords().getAllNeighborsCoords();
        List<Tile> neighbors = board.getNeighbors(entry);

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

        for (int index = 0; index < neighbors.size(); index++) {
            if (neighbors.get(index) == null) {

                for (Tile neighbor : neighbors) {
                    if (neighbor != null && !neighborsCoords.get(index).equals(entry.getCoords())) {
                        if (neighbor.getCoords().equals(neighborsCoords.get(index))) {
                            caseAllowed = false;
                        }
                    }
                }
                if (caseAllowed) {
                    Tile emptyTile = new Tile();
                    emptyTile.setCoords(neighborsCoords.get(index));
                    if (!swarm.contains(entry)) {
                        swarm.add(entry);
                    }
                    if (checkTileConnections(emptyTile, swarm) && !path.contains(emptyTile.getCoords())) {
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
    }//Todo: Create function for Pass movement:

    // Pass for soldier ant and spider needs to be implemented
    // refactor code, below method is non functional.
    public ArrayList<Coords> getPossibleMoves(int q, int r) {
        Tile tile = board.getTile(q, r);

        ArrayList<Coords> path = new ArrayList<Coords>();
        path.add(tile.getCoords());


        ArrayList<Coords> possibleMoves = new ArrayList<Coords>();

        List<Coords> neighborsCoords = tile.getCoords().getAllNeighborsCoords();

        if (tile.getTileType().equals(Hive.Tile.QUEEN_BEE)) {
            getMovesForQueenBee(neighborsCoords, possibleMoves);
        } else if (tile.getTileType().equals(Hive.Tile.BEETLE)) {
            getMovesForBeetle(neighborsCoords, tile, possibleMoves, path);
        } else if (tile.getTileType().equals(Hive.Tile.GRASSHOPPER)) {
            getMovesForGrasshopper(tile, q, r, possibleMoves);
        } else if (tile.getTileType().equals(Hive.Tile.SPIDER)) {
            getMovesForSpider();
        } else if (tile.getTileType().equals(Hive.Tile.SOLDIER_ANT)) {
            getMovesForSoldierAnt();
        }
        return possibleMoves;
    }

    private boolean tryMoveQueenBee(boolean destinationIsNotOccupied, boolean moveByOneAllowed) {
        if (destinationIsNotOccupied) {
            if (moveByOneAllowed) {
                return true;
            }
        }
        return false;
    }

    private boolean tryMoveBeetle(boolean moveByOneAllowed, boolean destinationIsNotOccupied, Tile tile, int toQ, int toR) {
        if (moveByOneAllowed) {
            ArrayList<Coords> path = new ArrayList<Coords>();
            if (!destinationIsNotOccupied) {
                return true;
            }
            if (isPathAvailable(tile.getCoords(), toQ, toR, path)) {
                return true;
            }
        }
        return false;
    }

    private boolean tryMoveSoldierAnt(boolean destinationIsNotOccupied, boolean moveToSameLocation, Tile tile, int toQ, int toR) {
        if (destinationIsNotOccupied) {
            if (!moveToSameLocation) {
                ArrayList<Coords> path = new ArrayList<Coords>();
                if (isPathAvailable(tile.getCoords(), toQ, toR, path)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean tryMoveSpider(boolean destinationIsNotOccupied, boolean moveToSameLocation, Tile tile, int toQ, int toR, int fromQ, int fromR) {
        if (destinationIsNotOccupied) {
            if (!moveToSameLocation) {
                if (Math.abs(Math.abs(fromQ) - Math.abs(toQ)) + Math.abs(Math.abs(fromR) - Math.abs(toR)) == 3) {
                    ArrayList<Coords> path = new ArrayList<Coords>();
                    if (isPathAvailable(tile.getCoords(), toQ, toR, path)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean tryMoveGrasshopper(boolean destinationIsNotOccupied, boolean moveToSameLocation, int toQ, int toR, int fromQ, int fromR) {
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
            if (moveDistanceQ == 0 || moveDistanceR == 0) {
                for (int i = 1; i < Math.abs(moveDistanceQ + moveDistanceR); i++) {
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
                return !illegal;
            }
        }
        return false;
    }

    private void getMovesForQueenBee(List<Coords> neighborsCoords, ArrayList<Coords> possibleMoves) {
        for (Coords move : neighborsCoords) {
            if (board.getTile(move.getQ(), move.getR()) == null) {
                possibleMoves.add(move);
            }
        }
    }

    private void getMovesForBeetle(List<Coords> neighborsCoords, Tile tile, ArrayList<Coords> possibleMoves, ArrayList<Coords> path) {
        for (Coords move : neighborsCoords) {
            if (board.getTile(move.getQ(), move.getR()) != null) {
                possibleMoves.add(move);
            } else if (isPathAvailable(tile.getCoords(), move.getQ(), move.getR(), path)) {
                possibleMoves.add(move);
            }
        }
    }

    private void getMovesForGrasshopper(Tile tile, int q, int r, ArrayList<Coords> possibleMoves) {
        for (Tile neighbor : board.getNeighbors(tile)) {
            if (neighbor != null && !neighbor.equals(tile)) {
                int toQ = neighbor.getCoords().getQ();
                int toR = neighbor.getCoords().getR();

                if (board.getTile(q + 1, r) != null && !board.getTile(q + 1, r).equals(tile)) {
                    while (board.getTile(toQ, toR) != null) {
                        toQ++;
                        if (board.getTile(toQ, toR) == null) {
                            possibleMoves.add(new Coords(toQ, toR));
                        }
                    }
                }
                if (board.getTile(q - 1, r) != null && !board.getTile(q - 1, r).equals(tile)) {
                    toQ = neighbor.getCoords().getQ();
                    toR = neighbor.getCoords().getR();
                    while (board.getTile(toQ, toR) != null) {
                        toQ--;
                        possibleMoves.add(new Coords(toQ, toR));
                    }
                }

                if (board.getTile(q, r + 1) != null && !board.getTile(q, r + 1).equals(tile)) {
                    toQ = neighbor.getCoords().getQ();
                    toR = neighbor.getCoords().getR();
                    while (board.getTile(toQ, toR) != null) {
                        toR++;
                        possibleMoves.add(new Coords(toQ, toR));
                    }
                }
                if (board.getTile(q, r - 1) != null && !board.getTile(q, r - 1).equals(tile)) {
                    toQ = neighbor.getCoords().getQ();
                    toR = neighbor.getCoords().getR();
                    while (board.getTile(toQ, toR) != null) {
                        toR--;
                        possibleMoves.add(new Coords(toQ, toR));
                    }
                }
            }
        }
    }

    private void getMovesForSoldierAnt() {
    }
    private void getMovesForSpider() {
    }
}