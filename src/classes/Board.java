package classes;

import nl.hive.hanze.Hive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Board implements interfaces.Board {
    private List<Tile> board;

    public void createBoard() {
        board = new ArrayList<>();
    }

    public List<Tile> getBoard() {
        return board;
    }

    public void setBoard(List<Tile> gameBoard) {
        board = gameBoard;
    }

    public Tile getTile(int q, int r) {
        Tile tempTile = null;
        for (Tile tile : board) {
            if (q == tile.getCoords().getQ() && r == tile.getCoords().getR()) {
                if (tempTile == null) {

                    tempTile = tile;
                } else if (tile.getTileType() == Hive.Tile.BEETLE) {
                    tempTile = tile;
                }
            }
        }
        return tempTile;
    }

    public void setTile(Tile tile) {
        board.add(tile);
    }

    public Boolean checkPath(int q, int r, int toQ, int toR) {
        return null;
    }

    public Boolean checkTileConnections(Tile entry, ArrayList<Tile> swarm) {
        boolean connected = false;

        int boardSize = board.size();

        List<Tile> neighbors = getNeighbors(entry);

        //Create function for checking if tile is coverd
        for (Tile tile : board) {
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


    public List<Tile> getNeighbors(Tile tile) {
        int q = tile.getCoords().getQ();
        int r = tile.getCoords().getR();
        List<Tile> neighbors = new ArrayList<>();

        Tile check1 = getTile(q - 1, r);
        Tile check2 = getTile(q + 1, r);
        Tile check3 = getTile(q, r + 1);
        Tile check4 = getTile(q, r - 1);
        Tile check5 = getTile(q - 1, r + 1);
        Tile check6 = getTile(q + 1, r - 1);

        neighbors.add(check1);
        neighbors.add(check2);
        neighbors.add(check3);
        neighbors.add(check4);
        neighbors.add(check5);
        neighbors.add(check6);
        return neighbors;
    }

    public ArrayList<Coords> getPossibleMoves(Tile tile) {
        int q = tile.getCoords().getQ();
        int r = tile.getCoords().getR();
        int toR = tile.getCoords().getQ();
        int toQ = tile.getCoords().getR();

        ArrayList<Coords> path = new ArrayList<>();
        path.add(tile.getCoords());

        Coords case1 = new Coords();
        Coords case2 = new Coords();
        Coords case3 = new Coords();
        Coords case4 = new Coords();
        Coords case5 = new Coords();
        Coords case6 = new Coords();

        ArrayList<Coords> possibleMoves = new ArrayList<>();

        List<Coords> neighborsCoords = Arrays.asList(case1, case2, case3, case4, case5, case6);

        switch (tile.getTileType()){
            case QUEEN_BEE:
                case1.setCoords(q - 1, r);
                case2.setCoords(q + 1, r);
                case3.setCoords(q, r + 1);
                case4.setCoords(q, r - 1);
                case5.setCoords(q + 1, r - 1);
                case6.setCoords(q - 1, r + 1);
                for (Coords move : neighborsCoords) {
                    if (getTile(move.getQ(), move.getR()) == null) {
                        possibleMoves.add(move);
                    }
                }
                break;
            case BEETLE:
                case1.setCoords(q - 1, r);
                case2.setCoords(q + 1, r);
                case3.setCoords(q, r + 1);
                case4.setCoords(q, r - 1);
                case5.setCoords(q + 1, r - 1);
                case6.setCoords(q - 1, r + 1);
                for (Coords move : neighborsCoords) {
                    if (getTile(move.getQ(), move.getR()) != null ) {
                        possibleMoves.add(move);
                    } else if(isPathAvailable(tile, move.getQ(), move.getR(), path)){
                        possibleMoves.add(move);
                    }
                }
                break;
            case GRASSHOPPER:
                for (Tile neighbor : getNeighbors(tile)) {
                    if (neighbor != null && !neighbor.equals(tile)) {
                        toQ = neighbor.getCoords().getQ();
                        toR = neighbor.getCoords().getR();

                        if(getTile(q+1, r) != null && !getTile(q+1, r).equals(tile)) {
                            while (getTile(toQ, toR) != null) {
                                toQ++;
                                if (getTile(toQ, toR) == null) {
                                    possibleMoves.add(new Coords(toQ, toR));
                                }
                            }
                        }
                        if(getTile(q-1, r) != null && !getTile(q-1, r).equals(tile)) {
                            toQ = neighbor.getCoords().getQ();
                            toR = neighbor.getCoords().getR();
                            while (getTile(toQ, toR) != null) {
                                toQ--;
                                possibleMoves.add(new Coords(toQ, toR));
                            }
                        }

                        if(getTile(q, r+1) != null && !getTile(q, r+1).equals(tile)) {
                            toQ = neighbor.getCoords().getQ();
                            toR = neighbor.getCoords().getR();
                            while (getTile(toQ, toR) != null) {
                                toR++;
                                possibleMoves.add(new Coords(toQ, toR));
                            }
                        }
                        if(getTile(q, r-1) != null && !getTile(q, r-1).equals(tile)) {
                            toQ = neighbor.getCoords().getQ();
                            toR = neighbor.getCoords().getR();
                            while (getTile(toQ, toR) != null) {
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

    public boolean isPathAvailable(Tile entry, int toQ, int toR, ArrayList<Coords> path) {
        Coords toCoords = new Coords(toQ, toR);
        int boardSize = board.size();
        if (path.size() == 0) {
            int q = entry.getCoords().getQ();
            int r = entry.getCoords().getR();
            Coords firstEntry = new Coords(q, r);
            path.add(firstEntry);
        }
        //Refactor, create swarm object always requiring a Tile as first entry
        ArrayList<Tile> swarm = new ArrayList<>();

        int q = entry.getCoords().getQ();
        int r = entry.getCoords().getR();

        if (board.size() <= 2) {
            return true;
        }

        if (entry.getCoords().equals(toCoords)) {
            if (checkTileConnections(entry, swarm)) {
                return true;
            }
        }

        Coords case1 = new Coords(q - 1, r);
        Coords case2 = new Coords(q + 1, r);
        Coords case3 = new Coords(q, r + 1);
        Coords case4 = new Coords(q, r - 1);
        Coords case5 = new Coords(q + 1, r - 1);
        Coords case6 = new Coords(q - 1, r + 1);

        List<Tile> neighbors = getNeighbors(entry);
        List<Coords> neighborsCoords = Arrays.asList(case1, case2, case3, case4, case5, case6);

        for (int index=0; index < neighbors.size(); index++) {
            if(neighbors.get(index) == null) {
                boolean caseAllowed = true;

                for (Tile neighbor : neighbors) {
                    if (neighborsCoords.get(index).getQ() == 3) {

                    }
                    if(neighbor != null && !neighborsCoords.get(index).equals(entry.getCoords())) {
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
                    if (checkTileConnections(emptyTile, swarm) && !path.contains(emptyTile.getCoords()) ) {
                        entry.setCoords(neighborsCoords.get(index));
                        Coords pathCoord = new Coords(emptyTile.getCoords());
                        path.add(pathCoord);
                        if (!isPathAvailable(entry, toQ, toR, path)) {
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

    public Tile getQueen(Hive.Player player) {
        for (Tile entry : board) {
            if (entry.getTileType() == Hive.Tile.QUEEN_BEE && entry.getPlayer() == player) {
                return entry;
            }
        }
        return null;
    }

    public boolean allTilesPlayed(Hive.Player player) {
        int grasshopper = 3;
        int soldierAnt = 3;
        int beetle = 2;
        int spider = 2;
        int queenBee = 1;

        for (Tile entry : board) {
            if (entry.getPlayer() == player) {
                switch (entry.getTileType()) {
                    case GRASSHOPPER:
                        grasshopper--;
                        break;

                    case SOLDIER_ANT:
                        soldierAnt--;
                        break;

                    case BEETLE:
                        beetle--;
                        break;

                    case SPIDER:
                        spider--;
                        break;

                    case QUEEN_BEE:
                        queenBee--;
                        break;

                }
            }
        }

        return (grasshopper == 0 && soldierAnt == 0 && beetle == 0 && spider == 0 && queenBee == 0);

    }

}
