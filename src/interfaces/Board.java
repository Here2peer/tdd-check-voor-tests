package interfaces;

import classes.Coords;
import classes.Tile;

import java.util.ArrayList;
import java.util.List;

public interface Board {

    void createBoard();

    List<Tile> getBoard();

    void setBoard(List<Tile> board);

    Tile getTile(int q, int r);

    void setTile(Tile tile);

    public Boolean checkPath(int q, int r, int toQ, int toR);

    public Boolean checkTileConnections(Tile tile, ArrayList<Tile> swarm);

    public List<Tile> getNeighbors(Tile tile);
        class NoTileFound extends Exception {
        public NoTileFound() { super(); }
        public NoTileFound(String message) { super(message); }
    }

    public ArrayList<Coords> getPossibleMoves(Tile tile);

}
