package tests;

import classes.Board;
import classes.Coords;
import nl.hive.hanze.Hive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    Board board;
    classes.Tile tile;
    @BeforeEach
    public void CreateNewBoardBeforeEachTest(){
        board = new Board();
        tile = new classes.Tile();
        board.createBoard();
    }

    @Test
    public void BoardContainsEmptyBoard(){

        List<classes.Tile> testBoard = new ArrayList<>();
        Assertions.assertEquals(testBoard, board.getBoard());
    }

    @Test
    public void BoardGetTile() {
        classes.Tile qt = new classes.Tile(0, 0, Hive.Player.BLACK, Hive.Tile.BEETLE);
        board.setTile(qt);
        try {
            Assertions.assertEquals(qt, board.getTile(0, 0));
        }catch (Exception ignored) {

        }
    }

    @Test
    public void BoardEmptyGetTile() {
        int q = 0;
        int r = 0;
        classes.Tile qt = new classes.Tile(q, r, Hive.Player.BLACK, Hive.Tile.BEETLE);
        try {
            board.getTile(q, r);
        }catch (Exception e) {
            String expectedMessage = "No tile on board with" + q + r + "." ;
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    public void SetTile() throws Board.NoTileFound {
        int q = 0;
        int r = 0;
        tile = new classes.Tile(q, r, Hive.Player.BLACK, Hive.Tile.BEETLE);
        board.setTile(tile);
        Assertions.assertEquals(tile, board.getTile(q, r));
    }

    @Test
    public void GetNeighbor() throws Board.NoTileFound {
        List<classes.Tile> listOfNeighbors = new ArrayList<>();
        tile.setCoords(1, 1);
        Assertions.assertEquals(listOfNeighbors.getClass(), board.getNeighbors(tile).getClass());
    }

    @Test
    public void CheckIfAllTilesAreStillConnected() {
        classes.Tile tile1 = new classes.Tile();
        classes.Tile tile2 = new classes.Tile();
        classes.Tile tile3 = new classes.Tile();
        classes.Tile tile4 = new classes.Tile();
        classes.Tile tile5 = new classes.Tile();
        classes.Tile tile6 = new classes.Tile();
        classes.Tile tile7 = new classes.Tile();

        ArrayList<classes.Tile> swarm = new ArrayList<>();


        tile1.setCoords(1, -1);
        tile2.setCoords(1, 0);
        tile3.setCoords(0, 1);
        tile4.setCoords(0, -1);
        tile5.setCoords(-1, 2);
        tile6.setCoords(-1,0);
        tile7.setCoords(-1,3);

        board.setTile(tile1);
        board.setTile(tile2);
        board.setTile(tile3);
        board.setTile(tile4);
        board.setTile(tile5);
        board.setTile(tile6);
        board.setTile(tile7);

        swarm.add(tile4);
        assertTrue(board.checkTileConnections(tile4, swarm));
    }
    @Test
    public void CheckIfAllTilesAreStillConnectedWithSwarmDisconnected() {
        classes.Tile tile1 = new classes.Tile();
        classes.Tile tile2 = new classes.Tile();
        classes.Tile tile3 = new classes.Tile();
        classes.Tile tile4 = new classes.Tile();
        classes.Tile tile5 = new classes.Tile();
        classes.Tile tile6 = new classes.Tile();
        classes.Tile tile7 = new classes.Tile();

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

        ArrayList<classes.Tile> swarm = new ArrayList<>();
        swarm.add(tile6);
        assertFalse(board.checkTileConnections(tile6, swarm));
    }

    @Test
    public void GetPossiblePathFromCoordsToDestination() {
        int toQ = 2;
        int toR = 0;
        classes.Tile movedTile = new classes.Tile();
        classes.Tile tile1 = new classes.Tile();
        classes.Tile tile2 = new classes.Tile();
        classes.Tile tile3 = new classes.Tile();
        classes.Tile tile4 = new classes.Tile();
        classes.Tile tile5 = new classes.Tile();

        movedTile.setCoords(0,0);
        tile1.setCoords(1, -1);
        tile2.setCoords(1, 0);
        tile3.setCoords(0, 1);
        tile4.setCoords(0, -1);
        tile5.setCoords(-1, 2);

        board.setTile(movedTile);
        board.setTile(tile1);
        board.setTile(tile2);
        board.setTile(tile3);
        board.setTile(tile4);
        board.setTile(tile5);

        ArrayList<Coords> path = new ArrayList<>();
        assertTrue(board.isPathAvailable(movedTile, toQ, toR, path));
        System.out.println();
    }

    @Test
    public void GetImpossiblePathFromCoordsToDestination() {
        int toQ = -1;
        int toR = 1;
        classes.Tile movedTile = new classes.Tile();
        classes.Tile tile1 = new classes.Tile();
        classes.Tile tile2 = new classes.Tile();
        classes.Tile tile3 = new classes.Tile();
        classes.Tile tile4 = new classes.Tile();
        classes.Tile tile5 = new classes.Tile();
        classes.Tile tile6 = new classes.Tile();

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
        assertFalse(board.isPathAvailable(movedTile, toQ, toR, path));
        System.out.println();
    }

    @Test
    public void GetQueenFromBoardTilesForPlayerWhite() {
        classes.Tile queenTile = new classes.Tile();
        classes.Tile tile1 = new classes.Tile();
        classes.Tile tile2 = new classes.Tile();
        classes.Tile tile3 = new classes.Tile();

        queenTile.setTile(Hive.Tile.QUEEN_BEE);
        queenTile.setPlayer(Hive.Player.WHITE);

        board.setTile(queenTile);
        board.setTile(tile1);
        board.setTile(tile2);
        board.setTile(tile3);

        assertSame(board.getQueen(Hive.Player.WHITE).getTileType(), Hive.Tile.QUEEN_BEE);
    }

    @Test
    public void CheckIfAllTilesArePlayedForPlayerWhite() {
        classes.Tile tile1 = new classes.Tile();
        classes.Tile tile2 = new classes.Tile();
        classes.Tile tile3 = new classes.Tile();
        classes.Tile tile4 = new classes.Tile();
        classes.Tile tile5 = new classes.Tile();
        classes.Tile tile6 = new classes.Tile();
        classes.Tile tile7 = new classes.Tile();
        classes.Tile tile8 = new classes.Tile();
        classes.Tile tile9 = new classes.Tile();
        classes.Tile tile10 = new classes.Tile();
        classes.Tile tile11 = new classes.Tile();

        tile1.setTile(Hive.Tile.QUEEN_BEE);
        tile2.setTile(Hive.Tile.SPIDER);
        tile3.setTile(Hive.Tile.SPIDER);
        tile4.setTile(Hive.Tile.BEETLE);
        tile5.setTile(Hive.Tile.BEETLE);
        tile6.setTile(Hive.Tile.SOLDIER_ANT);
        tile7.setTile(Hive.Tile.SOLDIER_ANT);
        tile8.setTile(Hive.Tile.SOLDIER_ANT);
        tile9.setTile(Hive.Tile.GRASSHOPPER);
        tile10.setTile(Hive.Tile.GRASSHOPPER);
        tile11.setTile(Hive.Tile.GRASSHOPPER);

        tile1.setPlayer(Hive.Player.WHITE);
        tile2.setPlayer(Hive.Player.WHITE);
        tile3.setPlayer(Hive.Player.WHITE);
        tile4.setPlayer(Hive.Player.WHITE);
        tile5.setPlayer(Hive.Player.WHITE);
        tile6.setPlayer(Hive.Player.WHITE);
        tile7.setPlayer(Hive.Player.WHITE);
        tile8.setPlayer(Hive.Player.WHITE);
        tile9.setPlayer(Hive.Player.WHITE);
        tile10.setPlayer(Hive.Player.WHITE);
        tile11.setPlayer(Hive.Player.WHITE);

        board.setTile(tile1);
        board.setTile(tile2);
        board.setTile(tile3);
        board.setTile(tile4);
        board.setTile(tile5);
        board.setTile(tile6);
        board.setTile(tile7);
        board.setTile(tile8);
        board.setTile(tile9);
        board.setTile(tile10);
        board.setTile(tile11);

        assertTrue(board.allTilesPlayed(Hive.Player.WHITE));
    }

    @Test
    public void findPossibleMovesForQueenBee() {
        classes.Tile tile1 = new classes.Tile();
        classes.Tile tile2 = new classes.Tile();

        tile1.setTile(Hive.Tile.QUEEN_BEE);
        tile2.setTile(Hive.Tile.SPIDER);
        tile1.setCoords(0,0);
        tile2.setCoords(1,0);

        board.setTile(tile1);
        board.setTile(tile2);
        ArrayList<Coords> test = board.getPossibleMoves(tile1);
        assertFalse(board.getPossibleMoves(tile1).isEmpty());
    }

    @Test
    public void findPossibleMovesForBeetle() {
        classes.Tile tile1 = new classes.Tile();
        classes.Tile tile2 = new classes.Tile();

        tile1.setTile(Hive.Tile.BEETLE);
        tile2.setTile(Hive.Tile.SPIDER);
        tile1.setCoords(0,0);
        tile2.setCoords(1,0);

        board.setTile(tile1);
        board.setTile(tile2);
        ArrayList<Coords> test = board.getPossibleMoves(tile1);
        assertFalse(board.getPossibleMoves(tile1).isEmpty());
    }

    @Test
    public void findPossibleMovesForGrasshopper() {
        classes.Tile tile1 = new classes.Tile();
        classes.Tile tile2 = new classes.Tile();
        classes.Tile tile3 = new classes.Tile();

        tile1.setTile(Hive.Tile.GRASSHOPPER);
        tile2.setTile(Hive.Tile.SPIDER);
        tile3.setTile(Hive.Tile.SPIDER);

        tile1.setCoords(0,0);
        tile2.setCoords(1,0);
        tile3.setCoords(-1,0);

        board.setTile(tile1);
        board.setTile(tile2);
        board.setTile(tile3);
        ArrayList<Coords> test = board.getPossibleMoves(tile1);
        assertFalse(board.getPossibleMoves(tile1).isEmpty());
    }
}
