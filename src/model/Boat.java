package model;

import java.util.ArrayList;
import java.util.List;

public class Boat { // sets some variables used to update different boat statuses
    private int id = 0;
    private String boatName = "";
    private int boatSize = 0;
    public int health = 0;
    public boolean placed = false;
    public List<String> coordinatesOnBoard = new ArrayList<>();

    public Boat(int id, String boatName, int boatSize, int health) { // constructor for new boat
        this.id = id;
        this.boatName = boatName;
        this.boatSize = boatSize;
        this.health = health;
    }

    public Boat() { // no args constructor
    }

// getters and setters

    public int getId() {
        return id;
    }

    public String getBoatName() {
        return boatName;
    }

    public boolean getPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    public int getBoatSize() {
        return boatSize;
    }

    public int getHealth() {
        return health;
    }

}
