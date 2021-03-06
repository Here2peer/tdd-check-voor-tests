package tests;

import classes.Board;
import interfaces.Hive;
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

    // Requirement 2c
    @Test
    public void BoardContainsEmptyBoard(){

        List<classes.Tile> testBoard = new ArrayList<>();
        Assertions.assertEquals(testBoard, board.getBoard());
    }

    @Test
    public void BoardGetTile() {
        classes.Tile qt = new classes.Tile(0, 0, Hive.Player.BLACK, Hive.Tile.BEETLE);
        board.setTile(qt);
        Assertions.assertEquals(qt, board.getTile(0, 0));
    }

    @Test
    public void BoardEmptyGetTile() {
        int q = 0;
        int r = 0;
        assertNull(board.getTile(q, r));
    }


    // Requirement 2d
    @Test
    public void SetTile() {
        int q = 0;
        int r = 0;
        tile = new classes.Tile(q, r, Hive.Player.BLACK, Hive.Tile.BEETLE);
        board.setTile(tile);
        Assertions.assertEquals(tile, board.getTile(q, r));
    }


    // Requirement 2b
    @Test
    public void GetNeighbor() {
        List<classes.Tile> listOfNeighbors = new ArrayList<>();
        tile.setCoords(1, 1);
        Assertions.assertEquals(listOfNeighbors.getClass(), board.getNeighbors(tile).getClass());
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


}
