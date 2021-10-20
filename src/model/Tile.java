package model;

public class Tile {
    private boolean hasShip = false;
    private boolean boatHit = false;
    public char state = ' ';

    public Tile() {
    }

    public char getState() {
        return state;
    }

    public void setState(char state) {
        this.state = state;
    }


    public boolean isHasShip() {
        return hasShip;
    }

    public void setHasShip(boolean hasShip) {
        this.hasShip = hasShip;
    }

    public boolean isBoatHit() {
        return boatHit;
    }

    public void setBoatHit(boolean boatHit) {
        this.boatHit = boatHit;
    }
}
