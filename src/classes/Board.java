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

    public Tile getQueen(Hive.Player player) {
        for (Tile entry : board) {
            if (entry.getTileType() == Hive.Tile.QUEEN_BEE && entry.getPlayer() == player) {
                return entry;
            }
        }
        return null;
    }



}
