package interfaces;

public interface Coords {
    int getQ();

    int getR();

    void setCoords(int q, int r);

    boolean coordsWithinThreshold(classes.Coords c, int threshold);
}
