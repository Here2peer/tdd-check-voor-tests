package tests;

import classes.Board;
import classes.Game;
import nl.hive.hanze.Hive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;
    Board board;
    classes.Tile tile;

    @BeforeEach
    public void CreateNewGameBeforeEachTest(){
        game = new Game();
        board = game.getBoard();
        tile = new classes.Tile();
    }

    @Test
    public void playOneTileOnAnEmptyBoard() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, 0, 0);
        assertEquals(1, board.getBoard().size());
    }

    @Test
    public void playMoveOnEmptyTile() throws Hive.IllegalMove {
        int q = 0;
        int r = 0;


        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        tile = board.getTile(0, 0);

        assertEquals(tile.getPlayer(), Hive.Player.WHITE);
        assertEquals(tile.getTileType(), Hive.Tile.QUEEN_BEE);
    }

    @Test
    public void playTileOnTile() throws Hive.IllegalMove {
        game.play(Hive.Tile.SPIDER, 0,0);
        try {

            game.play(Hive.Tile.BEETLE, 0,0);

        }catch (Hive.IllegalMove e) {
            String expectedMessage = "Illegal move, move not allowed.";
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    public void playFourthTile() throws Hive.IllegalMove {
        game.play(Hive.Tile.SPIDER, 0,0);
        game.play(Hive.Tile.BEETLE, -1,0);
        game.play(Hive.Tile.SOLDIER_ANT, 1,1);
        game.play(Hive.Tile.QUEEN_BEE, 1,0);

        tile = board.getTile(1, 0);
        assertEquals(tile.getPlayer(), Hive.Player.WHITE);
        assertEquals(tile.getTileType(), Hive.Tile.QUEEN_BEE);
    }
    @Test
    public void PlayFiveTilesWhileQueenIsNotPlayedIlligalMove() throws Hive.IllegalMove {
        game.play(Hive.Tile.SPIDER, 0,0);
        game.play(Hive.Tile.BEETLE, -1,0);
        game.play(Hive.Tile.SOLDIER_ANT, 1,1);
        game.play(Hive.Tile.SPIDER, 1,0);

        assertThrows(Hive.IllegalMove.class, () -> {
            game.play(Hive.Tile.GRASSHOPPER, 0,1);
        });

    }

    @Test
    public void playTileNextToNeighbor() throws Hive.IllegalMove{
        int q = 1;
        int r = 0;

        game.play(Hive.Tile.SPIDER, q-1,r);
        game.play(Hive.Tile.QUEEN_BEE, q,r);

        tile = board.getTile(q,r);
        assertEquals(tile.getPlayer(), Hive.Player.WHITE);
        assertEquals(tile.getTileType(), Hive.Tile.QUEEN_BEE);

    }

    @Test
    public void playTileNextToNeighborIllegal() {

        int q = 0;
        int r = 0;

        classes.Tile placedTileA = new classes.Tile(q, r, Hive.Player.BLACK, Hive.Tile.BEETLE);
        classes.Tile placedTileB = new classes.Tile(q, r, Hive.Player.WHITE, Hive.Tile.BEETLE);

        board.getBoard().add(placedTileB);
        board.getBoard().add(placedTileA);
        try {
            game.play(Hive.Tile.SPIDER, q, r);
        } catch (Exception e) {
            assertEquals("Illegal move, move not allowed.", e.getMessage());
        }
    }

    @Test
    public void moveSpiderWhenCoveredByBeetleSpiderCantMove() throws Hive.IllegalMove{
        game.play(Hive.Tile.SPIDER, 0, 0);
        game.play(Hive.Tile.BEETLE, 1, 0);
        game.move(1, 0, 0, 0);
        game.move(0, 0, 0, 1);
        tile = board.getTile(0,0);
        assertSame(tile.getTileType(), Hive.Tile.SPIDER);
    }

    @Test
    public void moveQueenTileByOnePosition() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.move(0, 0, 0, 1);
        tile = board.getTile(0, 1);
        assertSame(tile.getTileType(), Hive.Tile.QUEEN_BEE);
    }
    @Test
    public void moveQueenTileIllegalPosition() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        try {
            game.move(0, 0, 1, 1);
            tile = board.getTile(1, 1);
        } catch (Exception e) {
            assertEquals("Illegal move, move not allowed.", e.getMessage());
        }
    }

    @Test
    public void moveBeetleTile() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, 0, 1);
        game.play(Hive.Tile.BEETLE, 0, 0);
        game.move(0, 0, 0, 1);
        assertSame(board.getTile(0,1).getTileType(), Hive.Tile.BEETLE);
    }

    @Test
    public void moveGrasshopperTileByFourHorizontallyLeftToRight() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.SPIDER, 1, 0);
        game.play(Hive.Tile.GRASSHOPPER, -2, 0);
        game.move(-2, 0, 2, 0);
        tile = board.getTile(2, 0);
        assertSame(tile.getTileType(), Hive.Tile.GRASSHOPPER);
    }
    @Test
    public void moveGrasshopperTileByFourHorizontallyRightToLeft() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.SPIDER, 1, 0);
        game.play(Hive.Tile.GRASSHOPPER, 2, 0);
        game.move(2, 0, -2, 0);
        tile = board.getTile(-2, 0);
        assertSame(tile.getTileType(), Hive.Tile.GRASSHOPPER);
    }
    @Test
    public void moveGrasshopperTileByFourDiagonallyRightToLeft() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.BEETLE, 0, -1);
        game.play(Hive.Tile.SPIDER, 0, 1);
        game.play(Hive.Tile.GRASSHOPPER, 0, 2);
        game.move(0, 2, 0, -2);
        tile = board.getTile(0, -2);
        assertSame(tile.getTileType(), Hive.Tile.GRASSHOPPER);
    }

    @Test
    public void moveGrasshopperTileIllegallyOverEmptyTiles() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.GRASSHOPPER, -2, 0);
        try {
            game.move(-2, 0, 1, 0);
            tile = board.getTile(-2, 0);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Illegal move, move not allowed."));
        }
    }

    @Test
    public void MoveTileAndCreateTwoSwarms() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.GRASSHOPPER, -2, 0);
        try {
            game.move(-2,0,-3,0);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Illegal move, move not allowed."));
        }
    }
    @Test
    public void moveTileInBetweenTilesIllegalMove() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.play(Hive.Tile.BEETLE, 1, -1);
        game.play(Hive.Tile.BEETLE, 1, 0);
        game.play(Hive.Tile.SPIDER, 0, 1);
        game.play(Hive.Tile.SPIDER, 0, 0);
        game.play(Hive.Tile.SPIDER, -1, 0);

        //EDIT FOR ALL TEST CASES.
        assertThrows(Hive.IllegalMove.class, () -> {
            game.move(0, 0, 0, -1);
        });
    }


    @Test
    public void moveSpiderTile() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.BEETLE, 0, -1);
        game.play(Hive.Tile.SPIDER, -2, 0);
        game.move(-2, 0, 1, -2);
        assertSame(game.getBoard().getTile(1, -2).getTileType(), Hive.Tile.SPIDER);
    }

    @Test
    public void moveSoldierAntTile() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.BEETLE, 0, -1);
        game.play(Hive.Tile.SOLDIER_ANT, -2, 0);
        game.move(-2, 0, +2, -1);
        assertSame(game.getBoard().getTile(2, -1).getTileType(), Hive.Tile.SOLDIER_ANT);
    }

    @Test
    public void movePlayedTileIllegal() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.BEETLE, 0, 0);
        game.play(Hive.Tile.GRASSHOPPER, -1, +1);
        assertThrows(Hive.IllegalMove.class, () -> {
            game.move(-1, +1, -1, 2);
        });
    }

    @Test
    public void NoPossibleMoveOrPlacementOfTilePassTurn() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.BEETLE, 0, 0);
        game.play(Hive.Tile.GRASSHOPPER, -1, +1);
        game.pass();
    }

    @Test
    public void isWhiteAWinner() {
        Hive.Tile otherTile = Hive.Tile.SPIDER;
        Hive.Player white = Hive.Player.WHITE;
        Hive.Player black = Hive.Player.BLACK;
        List<classes.Tile> board = this.board.getBoard();
        board.add(new classes.Tile(0,0, black, Hive.Tile.QUEEN_BEE));
        board.add(new classes.Tile(-1, 0, white, otherTile));
        board.add(new classes.Tile(-1, 1, white, otherTile));
        board.add(new classes.Tile(0, 1, black, otherTile));
        board.add(new classes.Tile(1, 0, white, otherTile));
        board.add(new classes.Tile(1, -1, black, otherTile));
        board.add(new classes.Tile(0, -1, white, otherTile));

        assertTrue(game.isWinner(Hive.Player.BLACK));

    }

    @Test
    public void GameIsADraw() {
        Hive.Tile otherTile = Hive.Tile.SPIDER;
        Hive.Player white = Hive.Player.WHITE;
        Hive.Player black = Hive.Player.BLACK;

        List<classes.Tile> board = this.board.getBoard();
        board.add(new classes.Tile(0,0, black, Hive.Tile.QUEEN_BEE));
        board.add(new classes.Tile(-1, 0, white, Hive.Tile.QUEEN_BEE));
        board.add(new classes.Tile(-1, 1, white, otherTile));
        board.add(new classes.Tile(-1, -1, white, otherTile));
        board.add(new classes.Tile(0, 1, black, otherTile));
        board.add(new classes.Tile(-2, 0, black, otherTile));
        board.add(new classes.Tile(1, 0, white, otherTile));
        board.add(new classes.Tile(-2, 1, white, otherTile));
        board.add(new classes.Tile(1, -1, black, otherTile));
        board.add(new classes.Tile(0, -1, white, otherTile));
        assertTrue(game.isDraw());
    }


    @Test
    public void NoWinnerBecauseQueenIsNotPlaced() {
        Hive.Tile otherTile = Hive.Tile.SPIDER;
        Hive.Player white = Hive.Player.WHITE;
        Hive.Player black = Hive.Player.BLACK;
        List<classes.Tile> board = this.board.getBoard();
        board.add(new classes.Tile(0,0, black, otherTile));
        board.add(new classes.Tile(-1, 0, white, otherTile));
        board.add(new classes.Tile(-1, 1, white, otherTile));
        board.add(new classes.Tile(0, 1, black, otherTile));
        board.add(new classes.Tile(1, 0, white, otherTile));
        board.add(new classes.Tile(1, -1, black, otherTile));
        board.add(new classes.Tile(0, -1, white, otherTile));

        assertFalse(game.isWinner(Hive.Player.BLACK));
    }

}