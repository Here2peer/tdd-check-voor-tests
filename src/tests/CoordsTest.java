package tests;

import org.junit.jupiter.api.*;
import classes.Coords;

public class CoordsTest {
    Coords coords;

    @BeforeEach
    public void BeforeEachCoordsTest() {
        this.coords = new Coords();
    }

    @Test
    public void AreCoordsWithinThreshold(){
        Coords coordtest = new Coords();
        coordtest.setCoords(0,0);
        coords.setCoords(1, 0);
        Assertions.assertTrue(coords.coordsWithinThreshold(coordtest, 1));
    }

    @Test
    public void AreCoordsEqualToGivenCoords() {
        Coords case1 = new Coords();
        Coords case2 = new Coords();
        case1.setCoords(0,0);
        case2.setCoords(0,0);
        Assertions.assertTrue(case1.equals(case2));
    }
}
