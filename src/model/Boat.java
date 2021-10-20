package model;

import java.util.ArrayList;
import java.util.List;

public class Boat {
    private int id = 0;
    private String boatName = "";
    private int boatSize = 0;
    public int health = 0;
    public boolean placed = false;
    public List<String> coordinatesOnBoard = new ArrayList<>();

    public Boat(int id, String boatName, int boatSize, int health) {
        this.id = id;
        this.boatName = boatName;
        this.boatSize = boatSize;
        this.health = health;
    }

    public Boat() {
    }

    public List<String> getCoordinatesOnBoard() {
        return coordinatesOnBoard;
    }

    public void setCoordinatesOnBoard(List<String> coordinatesOnBoard) {
        this.coordinatesOnBoard = coordinatesOnBoard;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    public int getBoatSize() {
        return boatSize;
    }

    public void setBoatSize(int boatSize) {
        this.boatSize = boatSize;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
