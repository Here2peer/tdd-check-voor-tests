package tests;

import classes.Board;
import classes.Coords;
import classes.Game;
import nl.hive.hanze.Hive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;
    Board board;
    classes.Tile tile;

    @BeforeEach
    public void CreateNewGameBeforeEachTest(){
        game = new Game();
        tile = new classes.Tile();
    }

    // Requirement 4b
    @Test
    public void playOneTileOnAnEmptyBoard() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, 0, 0);
    }

    // Requirement 4b
    @Test
    public void playMoveOnEmptyTile() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
    }

    // Requirement 4c
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

    // Requirement 4e
    @Test
    public void playFourthTile() throws Hive.IllegalMove {
        game.play(Hive.Tile.SPIDER, 0,0);
        game.play(Hive.Tile.BEETLE, -1,0);
        game.play(Hive.Tile.QUEEN_BEE, 1,0);
        game.play(Hive.Tile.SOLDIER_ANT, 1,1);
    }


    @Test
    public void PlayFiveTilesWhileQueenIsNotPlayedIlligalMove() throws Hive.IllegalMove {
        game.play(Hive.Tile.SPIDER, 0,0);
        game.play(Hive.Tile.BEETLE, -1,0);
        game.play(Hive.Tile.SPIDER, 1,0);
        game.play(Hive.Tile.SOLDIER_ANT, 1,1);

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

    }

    @Test
    public void playTileNextToNeighborIllegal() throws Hive.IllegalMove {
        int q = 0;
        int r = 0;

        game.play(Hive.Tile.BEETLE, q, r);
        try {
            game.play(Hive.Tile.SPIDER, q-1, r);
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
    }

    @Test
    public void moveQueenTileByOnePosition() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.move(0, 0, 0, 1);
    }
    @Test
    public void moveQueenTileIllegalPosition() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        try {
            game.move(0, 0, 1, 1);
        } catch (Exception e) {
            assertEquals("Illegal move, move not allowed.", e.getMessage());
        }
    }

    @Test
    public void moveBeetleTile() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 1);
        game.play(Hive.Tile.BEETLE, 0, 0);
        game.move(0, 0, 0, 1);
    }

    @Test
    public void moveGrasshopperTileByFourHorizontallyLeftToRight() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.SPIDER, 1, 0);
        game.play(Hive.Tile.GRASSHOPPER, -2, 0);
        game.move(-2, 0, 2, 0);
    }
    @Test
    public void moveGrasshopperTileByFourHorizontallyRightToLeft() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.SPIDER, 1, 0);
        game.play(Hive.Tile.GRASSHOPPER, 2, 0);
        game.move(2, 0, -2, 0);
    }
    @Test
    public void moveGrasshopperTileByFourDiagonallyRightToLeft() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.BEETLE, 0, -1);
        game.play(Hive.Tile.SPIDER, 0, 1);
        game.play(Hive.Tile.GRASSHOPPER, 0, 2);
        game.move(0, 2, 0, -2);
    }

    @Test
    public void moveGrasshopperTileIllegallyOverEmptyTiles() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.GRASSHOPPER, -2, 0);
        try {
            game.move(-2, 0, 1, 0);
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
    }

    @Test
    public void moveSoldierAntTile() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);
        game.play(Hive.Tile.BEETLE, 0, -1);
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.SOLDIER_ANT, -2, 0);
        game.move(-2, 0, 2, -1);
    }

    @Test
    public void movePlayedTileIllegal() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);
        game.play(Hive.Tile.BEETLE, 0, 0);
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.GRASSHOPPER, -1, +1);
        assertThrows(Hive.IllegalMove.class, () -> {
            game.move(-1, +1, -1, 2);
        });
    }

    @Test
    public void NoPossibleMoveOrPlacementOfTilePassTurn() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);
        game.play(Hive.Tile.BEETLE, 0, 0);
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.GRASSHOPPER, -1, +1);
        game.pass();
    }

    @Test
    public void isWhiteAWinner() throws Hive.IllegalMove {
        Hive.Tile otherTile = Hive.Tile.SPIDER;
        Hive.Player black = Hive.Player.BLACK;

        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(otherTile, -1,0);
        game.play(otherTile, -1, 1);
        game.play(otherTile, 0, 1);
        game.play(otherTile, 1, 0);
        game.play(otherTile, 1, -1);
        game.play(otherTile, 0, -1);

        assertTrue(game.isWinner(Hive.Player.BLACK));

    }

    @Test
    public void GameIsADraw() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, -1, 0);
        game.nextPlayer();
        game.play(Hive.Tile.QUEEN_BEE, 1, 0);
        game.nextPlayer();

        game.play(Hive.Tile.BEETLE, -2, 1);
        game.play(Hive.Tile.BEETLE, -2, 0);
        game.play(Hive.Tile.SPIDER, -1, -1);


        game.nextPlayer();
        game.play(Hive.Tile.SPIDER, 0, 1);
        game.play(Hive.Tile.SOLDIER_ANT, 1, 1);
        game.play(Hive.Tile.SPIDER, 2, 0);
        game.play(Hive.Tile.SOLDIER_ANT, 2, -1);
        game.play(Hive.Tile.SOLDIER_ANT, 1, -1);
        game.play(Hive.Tile.BEETLE, -1, 2);
        game.play(Hive.Tile.BEETLE, 1, -2);
        game.play(Hive.Tile.GRASSHOPPER, 3, 0);

        game.move(3,0, 0, 0);
        game.move(-1, 2, -1, 1);
        game.move(1, -2, 0, -1);


        assertTrue(game.isDraw());
    }

    @Test
    public void findPossibleMovesForQueenBee() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.SPIDER, 1, 0);
        assertFalse(game.getPossibleMoves(0,0).isEmpty());
    }

    @Test
    public void findPossibleMovesForBeetle() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, 0,0);
        game.play(Hive.Tile.SPIDER, 1, 0);
        assertFalse(game.getPossibleMoves(0, 0).isEmpty());
    }

    @Test
    public void findPossibleMovesForGrasshopper() throws Hive.IllegalMove {
        game.play(Hive.Tile.GRASSHOPPER, 0,0);
        game.play(Hive.Tile.SPIDER, 1,0);
        game.play(Hive.Tile.SPIDER, -1,0);
        assertFalse(game.getPossibleMoves(0,0).isEmpty());
    }

    @Test
    public void CheckIfAllTilesArePlayedForPlayerWhite() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.SPIDER, 1, 0);
        game.play(Hive.Tile.SPIDER, 2, 0);
        game.play(Hive.Tile.BEETLE, 3, 0);
        game.play(Hive.Tile.BEETLE, 4, 0);
        game.play(Hive.Tile.SOLDIER_ANT, 5, 0);
        game.play(Hive.Tile.SOLDIER_ANT, 6, 0);
        game.play(Hive.Tile.SOLDIER_ANT, 7, 0);
        game.play(Hive.Tile.GRASSHOPPER, 8, 0);
        game.play(Hive.Tile.GRASSHOPPER, 9, 0);
        game.play(Hive.Tile.GRASSHOPPER, 10, 0);

        assertTrue(game.allTilesPlayed(Hive.Player.WHITE));
    }

    @Test
    public void CheckIfAllTilesAreStillConnected() throws Hive.IllegalMove {
        classes.Tile tile4 = new classes.Tile();

        ArrayList<classes.Tile> swarm = new ArrayList<>();


        tile4.setCoords(0, -1);


        game.play(Hive.Tile.BEETLE, 1, -1);
        game.play(Hive.Tile.QUEEN_BEE, 1, 0);
        game.play(Hive.Tile.SOLDIER_ANT, 0, 1);
        game.play(Hive.Tile.SOLDIER_ANT, 0, -1);
        game.play(Hive.Tile.GRASSHOPPER, -1, 2);
        game.play(Hive.Tile.GRASSHOPPER, -1, 0);
        game.play(Hive.Tile.GRASSHOPPER, -1, 3);

        swarm.add(tile4);
        assertTrue(game.checkTileConnections(tile4, swarm));
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
        assertFalse(game.checkTileConnections(tile6, swarm));
    }

    @Test
    public void GetPossiblePathFromCoordsToDestination() throws Hive.IllegalMove {
        int toQ = 2;
        int toR = 0;

        Coords coords = new Coords(0,0);

        game.play(Hive.Tile.SOLDIER_ANT, 0,0);
        game.play(Hive.Tile.SOLDIER_ANT, 1,-1);
        game.play(Hive.Tile.QUEEN_BEE, 1,0);
        game.play(Hive.Tile.SOLDIER_ANT, 0,1);
        game.play(Hive.Tile.SPIDER, 0,-1);
        game.play(Hive.Tile.SPIDER, -1,2);

        ArrayList<Coords> path = new ArrayList<>();
        assertTrue(game.isPathAvailable(coords, toQ, toR, path));
    }


}