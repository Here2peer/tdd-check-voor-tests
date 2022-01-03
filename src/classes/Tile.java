package classes;

import interfaces.Hive;
import interfaces.Hive.Player;

public class Tile {
    private Coords coords;
    private Player player;
    private Hive.Tile tile;

    public Tile() {
        this.coords = new Coords();
    }

    public Tile(int q, int r, Player player, Hive.Tile tile) {
        this.coords = new Coords();
        this.player = player;
        this.tile = tile;
        this.coords.setCoords(q, r);
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(int q, int r) {
        this.coords.setCoords(q, r);
    }
    public void setCoords(Coords coords) {
        this.coords.setCoords(coords);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Hive.Tile getTileType() {
        return tile;
    }

    public void setTile(Hive.Tile tile) {
        this.tile = tile;
    }


}
