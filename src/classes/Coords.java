package classes;

import java.util.Arrays;
import java.util.List;

public class Coords implements interfaces.Coords {
    private int q;
    private int r;

    public Coords(){}

    public Coords(int q, int r){
        this.q = q;
        this.r = r;
    }
    public Coords(Coords coords){
        this.q = coords.getQ();
        this.r = coords.getR();
    }


    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public void setCoords(int q, int r) {
        this.q = q;
        this.r = r;
    }

    public void setCoords(Coords coords) {
        q = coords.getQ();
        r = coords.getR();
    }

    public boolean coordsWithinThreshold(Coords c, int threshold) {
        return Math.abs(c.getQ() - q) <= threshold && Math.abs(c.getR() - r) <= threshold;
    }

    public List<Coords> getAllNeighborsCoords() {
        Coords case1 = new Coords(q - 1, r);
        Coords case2 = new Coords(q + 1, r);
        Coords case3 = new Coords(q, r + 1);
        Coords case4 = new Coords(q, r - 1);
        Coords case5 = new Coords(q + 1, r - 1);
        Coords case6 = new Coords(q - 1, r + 1);
        return Arrays.asList(case1, case2, case3, case4, case5, case6);
    }

    @Override
    public boolean equals(Object coord) {
        if (coord instanceof Coords) {
            Coords c = (Coords) coord;
            return (c.getQ() == q && c.getR() == r);
        } else {
            return false;
        }
    }
}
