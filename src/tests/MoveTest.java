package tests;

import classes.Board;
import classes.Coords;
import classes.Tile;
import classes.Move;
import interfaces.Hive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoveTest {
    Move move;
    Board board;

    //TODO:
    // Write tests for methods in Move.Java Try and get Moves.

    @BeforeEach
    public void CreateNewBoardBeforeEachTest(){
        board = new Board();
        board.createBoard();
        move = new Move(board);
    }

    // Requirement 5c
    @Test
    public void GetPossiblePathFromCoordsToDestination() throws Hive.IllegalMove {
        int toQ = 2;
        int toR = 0;

        Coords coords = new Coords(0,0);

        board.setTile(new Tile(0,0, Hive.Player.WHITE, Hive.Tile.SOLDIER_ANT));
        board.setTile(new Tile(1, -1, Hive.Player.WHITE, Hive.Tile.SOLDIER_ANT));
        board.setTile(new Tile(1,0, Hive.Player.WHITE, Hive.Tile.QUEEN_BEE));
        board.setTile(new Tile(0,1, Hive.Player.WHITE, Hive.Tile.SOLDIER_ANT));
        board.setTile(new Tile(0,-1, Hive.Player.WHITE, Hive.Tile.SPIDER));
        board.setTile(new Tile(-1, 2, Hive.Player.WHITE, Hive.Tile.SPIDER));

        ArrayList<Coords> path = new ArrayList<>();
        assertTrue(move.isPathAvailable(coords, toQ, toR, path));
    }

    // Requirement 5d
    @Test
    public void CheckIfAllTilesAreStillConnected() throws Hive.IllegalMove {
        classes.Tile tile4 = new classes.Tile();

        ArrayList<classes.Tile> swarm = new ArrayList<>();

        tile4.setCoords(0, -1);

        board.setTile(new Tile(1, -1, Hive.Player.WHITE, Hive.Tile.BEETLE));
        board.setTile(new Tile(1, 0, Hive.Player.WHITE, Hive.Tile.QUEEN_BEE));
        board.setTile(new Tile(0, 1, Hive.Player.WHITE, Hive.Tile.SOLDIER_ANT));
        board.setTile(new Tile(0, -1, Hive.Player.WHITE, Hive.Tile.SOLDIER_ANT));
        board.setTile(new Tile(-1, 2, Hive.Player.WHITE, Hive.Tile.GRASSHOPPER));
        board.setTile(new Tile(-1, 0, Hive.Player.WHITE, Hive.Tile.GRASSHOPPER));
        board.setTile(new Tile(-1, 3, Hive.Player.WHITE, Hive.Tile.GRASSHOPPER));

        swarm.add(tile4);
        assertTrue(move.checkTileConnections(tile4, swarm));
    }

    // Requirement 8
    @Test
    public void findPossibleMovesForQueenBee() throws Hive.IllegalMove {
        board.setTile(new Tile(0, 0, Hive.Player.WHITE, Hive.Tile.QUEEN_BEE));
        board.setTile(new Tile(1, 0 , Hive.Player.WHITE, Hive.Tile.SPIDER));
        assertFalse(move.getPossibleMoves(0,0).isEmpty());
    }

    // Requirement 7
    @Test
    public void findPossibleMovesForBeetle() throws Hive.IllegalMove {
        board.setTile(new Tile(0,0, Hive.Player.WHITE, Hive.Tile.BEETLE));
        board.setTile(new Tile(1, 0, Hive.Player.WHITE, Hive.Tile.SPIDER));
        assertFalse(move.getPossibleMoves(0, 0).isEmpty());
    }

    // Requirement 11
    @Test
    public void findPossibleMovesForGrasshopper() throws Hive.IllegalMove {
        board.setTile(new Tile(0,0, Hive.Player.WHITE, Hive.Tile.GRASSHOPPER));
        board.setTile(new Tile(1,0, Hive.Player.WHITE, Hive.Tile.SPIDER));
        board.setTile(new Tile(-1,0, Hive.Player.WHITE, Hive.Tile.SPIDER));
        assertFalse(move.getPossibleMoves(0,0).isEmpty());
    }

    @Test
    public void GetImpossiblePathFromCoordsToDestination() {
        int toQ = -1;
        int toR = 1;
        Tile movedTile = new Tile();
        Tile tile1 = new Tile();
        Tile tile2 = new Tile();
        Tile tile3 = new Tile();
        Tile tile4 = new Tile();
        Tile tile5 = new Tile();
        Tile tile6 = new Tile();

        movedTile.setCoords(0,0);
        tile1.setCoords(1, -1);
        tile2.setCoords(1, 0);
        tile3.setCoords(0, 1);
        tile4.setCoords(0, -1);
        tile5.setCoords(-1, 2);
        tile6.setCoords(-1, 0);

        board.setTile(movedTile);
        board.setTile(tile1);
        board.setTile(tile2);
        board.setTile(tile3);
        board.setTile(tile4);
        board.setTile(tile5);
        board.setTile(tile6);

        ArrayList<Coords> path = new ArrayList<>();
        assertFalse(move.isPathAvailable(movedTile.getCoords(), toQ, toR, path));
    }

    // Requirement 5d
    @Test
    public void CheckIfAllTilesAreStillConnectedWithSwarmDisconnected() {
        Tile tile1 = new Tile();
        Tile tile2 = new Tile();
        Tile tile3 = new Tile();
        Tile tile4 = new Tile();
        Tile tile5 = new Tile();
        Tile tile6 = new Tile();
        Tile tile7 = new Tile();

        tile1.setCoords(0,0);
        tile2.setCoords(1, -1);
        tile3.setCoords(1, 0);
        tile4.setCoords(0, 1);
        tile5.setCoords(0, -1);
        tile6.setCoords(-4, 3);
        tile7.setCoords(-4, 4);

        board.setTile(tile1);
        board.setTile(tile2);
        board.setTile(tile3);
        board.setTile(tile4);
        board.setTile(tile5);
        board.setTile(tile6);
        board.setTile(tile7);

        ArrayList<Tile> swarm = new ArrayList<>();
        swarm.add(tile6);
        assertFalse(move.checkTileConnections(tile6, swarm));
    }


}
