package model;


import utils.Checker;
import utils.FileHelper;

import java.io.FileNotFoundException;
import java.util.*;

public class Board {

    private int boardSize = 0;
    private int cellSize = 0;
    private final int DEFAULT_BOARD_SIZE = 5;
    private final int DEFAULT_CELL_SIZE = 2;


    List<List<Tile>> boardGrid = new ArrayList<>();
    List<Boat> boats = new FileHelper().readBoatToList();
    List<Boat> displayBoats = boats;

    private List<String> horizontalBoarder = new ArrayList<>(Arrays.asList("A","B","C","D","E","F","G","H","I","J"));
    private List<String> verticalBoarder = new ArrayList<>(Arrays.asList("0","1","2","3","4","5","6","7","8","9"));

    public Board() throws FileNotFoundException {
    }

    public void populateBoard() {
        for (int p = 0; p < boardSize; p++) {
            List<Tile> row = new ArrayList<>();
            for (int i = 0; i < boardSize; i++) {
                row.add(new Tile());
            }
            boardGrid.add(row);
        }
    }



    public void renderBoard(){
        int total = boardSize * cellSize;
        char c = ' ';
        int verticalBoarderCounter = 0;
        int horizontalBoarderCounter = 0;



        while (horizontalBoarderCounter != boardSize){
            if (horizontalBoarderCounter < 10){
                System.out.print(" " + horizontalBoarder.get(horizontalBoarderCounter));
                horizontalBoarderCounter++;

            } else if (horizontalBoarderCounter >= 10){
                int firstIndex = (horizontalBoarderCounter/10) - 1;
                int secondIndex = horizontalBoarderCounter%10;
                System.out.print(horizontalBoarder.get(firstIndex) + horizontalBoarder.get(secondIndex));
                horizontalBoarderCounter++;
            }
        }

        System.out.println();

        for (int i = 0; i <= total; i++){
            for (int k = 0 ; k <= total; k++){
                if (k % cellSize == 0 && i % cellSize != 0 && k == total){
                    if (verticalBoarderCounter < 10){
                        System.out.print("| " + verticalBoarder.get(verticalBoarderCounter));

                    }else if (verticalBoarderCounter >= 10){
                        int firstIndex = (verticalBoarderCounter / 10) - 1;
                        int secondIndex = verticalBoarderCounter % 10;
                        System.out.print("| " + verticalBoarder.get(firstIndex) + verticalBoarder.get(secondIndex));
                    }

                    verticalBoarderCounter++;
                } else if (k % cellSize == 0 && i % cellSize != 0) {
                    c = '|';
                } else if (i % cellSize == 0 || k % cellSize == 0) {
                    c = '-';
                }else {
                    c = boardGrid.get(i / 2).get(k / 2).state; // if c == 5 no state???? try
                }
                System.out.print(c);
                c = ' ';
            }
            System.out.println();
        }
    }

    public void setBoardDimensions() throws FileNotFoundException {
        FileHelper fileHelper = new FileHelper();
        List<String> boardSizesAsString = fileHelper.getBoardSize();
        String state = "";
        for (String s : boardSizesAsString) {
            if (s.equals("size")) {
                state = "size";
            }
            if (state.equals("size") && !s.equals("size")) {
                boardSize = Integer.parseInt(s);
                state = "";
            }
            if (s.equals("cellSize")) {
                state = "cellSize";
            }
            if (state.equals("cellSize") && !s.equals("cellSize")) {
                cellSize = Integer.parseInt(s);
                state = "";
            }
        }
        if (cellSize == 0 || boardSize == 0){
            boardSize = DEFAULT_BOARD_SIZE;
            cellSize = DEFAULT_CELL_SIZE;

        }
        if (cellSize > 5 || boardSize > 20){
            boardSize = DEFAULT_BOARD_SIZE;
            cellSize = DEFAULT_CELL_SIZE;
        }
    }

    private void displayShipToConsole(Boat boatToRemove){
        System.out.println("ID   Boat Name             Length ");


        for (Boat toCheck: displayBoats) {
            if (toCheck.equals(boatToRemove)){
                displayBoats.remove(boatToRemove);
                break;
            }
        }

        for (Boat boat: displayBoats) {
            String str = (boat.getId() + "    " + boat.getBoatName()+ "                 " );
            String output = new StringBuilder(str).insert(27,boat.getBoatSize()).toString();
            System.out.print(output);
            System.out.println();
        }
    }

    public void placeShip(){

        Scanner sc = new Scanner(System.in);
        String id = "";
        String positionH = "";
        String positionH2 = "";
        String positionV = "";
        String positionV2 = "";
        String alignment = "";
        int boatCheck = boats.size();



        do {
            displayShipToConsole(new Boat());
            System.out.println("Choose a ship id and where you for like to place it(e.g 1 A3 V)");
            String userChoice = sc.nextLine().toUpperCase(Locale.ROOT).replaceAll("\\s+","");

            if (userChoice.length() >= 4 && userChoice.length() <= 6){
// for 4 characters
                if (userChoice.length() == 4){
                    boolean isValid = new Checker().coordinatesCheckFor4(userChoice);
                    if (isValid){
                        boolean checkState = false;


                        id = Character.toString(userChoice.charAt(0));
                        Boat boat = boats.get(Integer.parseInt(id));
                        positionH = Character.toString(userChoice.charAt(1));
                        positionV = Character.toString(userChoice.charAt(2));
                        alignment = Character.toString(userChoice.charAt(3));

                        if (!boat.getPlaced()){
                            positionH = horizontalToDigitConversion(positionH);
                            if (alignment.equals("H")){
                                if (Integer.parseInt(positionV) + boat.getBoatSize() > boardSize){
                                    System.out.println("Ship can't be placed there, try another co-ordinate!");
                                } else {
                                    for (int i = 0; i < boat.getBoatSize(); i++) {
                                        if (boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH) + i).isHasShip()) {
                                            checkState = true;
                                            System.out.println("A boat is already in this position");
                                        }
                                    }
                                    if (!checkState) {
                                        displayShipToConsole(boat);
                                        boatCheck =- 1;
                                        for (int j = 0; j < boat.getBoatSize(); j++) {

                                            boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH) + j).state = 'S';
                                            boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH) + j).setHasShip(true);
                                            boat.coordinatesOnBoard.add(positionH + positionV);
                                            boat.setPlaced(true);
                                        }
                                    }
                                }
                            }
                        }
                        if (alignment.equals("V")){
                            if (Integer.parseInt(positionH) + boat.getBoatSize() > boardSize){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");

                            }else {
                                for (int i = 0; i < boat.getBoatSize(); i++) {
                                    if (boardGrid.get(Integer.parseInt(positionH) + i).get(Integer.parseInt(positionV)).isHasShip()) {
                                        checkState = true;
                                        System.out.println("A boat is already in this position");
                                    }
                                }
                                if (!checkState){
                                    boatCheck =- 1;
                                    displayShipToConsole(boat);
                                    for (int j = 0; j < boat.getBoatSize(); j++) {

                                        boardGrid.get(Integer.parseInt(positionV) + j).get(Integer.parseInt(positionH)).state = 'S';
                                        boardGrid.get(Integer.parseInt(positionV) + j).get(Integer.parseInt(positionH)).setHasShip(true);
                                        boat.coordinatesOnBoard.add(positionH + positionV);
                                        boat.setPlaced(true);

                                    }
                                }
                            }
                        }

                    }
                }
            } else{
                System.out.println("ERROR! Incorrect input for co-ordinates");
            }

            // for 5 characters
            if (userChoice.length() == 5){
                int isValid = new Checker().coordinatesCheckFor5(userChoice);
                if (isValid == 0){
                    System.out.println("ERROR! invalid input");
                }
                if (isValid == 1){
                    //AA1
                    boolean checkState = false;
                    id = Character.toString(userChoice.charAt(0));
                    Boat boat = boats.get(Integer.parseInt(id));
                    positionH = Character.toString(userChoice.charAt(1));
                    positionH2 = Character.toString(userChoice.charAt(2));
                    positionV = Character.toString(userChoice.charAt(3));
                    alignment = Character.toString(userChoice.charAt(4));

                    if (!boat.getPlaced()){
                        positionH = horizontalToDigitConversion(positionH);
                        positionH2 = horizontalToDigitConversion(positionH2);
                        if (alignment.equals("H")){
                            if (Integer.parseInt((positionV)) + boat.getBoatSize() > boardSize){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }else {
                                for (int i = 0; i < boat.getBoatSize(); i++) {
                                    if (boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH + positionH2) + i).isHasShip()) {
                                        checkState = true;
                                        System.out.println("A boat is already in this position");
                                    }
                                }
                                if (!checkState){
                                    boatCheck =- 1;
                                    displayShipToConsole(boat);
                                    for (int j = 0; j < boat.getBoatSize(); j++){
                                        boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH + positionH2) + j).state = 'S';
                                        boardGrid.get(Integer.parseInt(positionV)).get(Integer.parseInt(positionH + positionH2) + j).setHasShip(true);
                                        boat.coordinatesOnBoard.add(positionH + positionH2 + positionV);
                                        boat.setPlaced(true);
                                    }
                                }
                            }
                        }
                        if (alignment.equals("V")){
                            if (Integer.parseInt((positionH)) + boat.getBoatSize() > boardSize){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }else {
                                for (int i = 0; i < boat.getBoatSize(); i++){
                                    if (boardGrid.get(Integer.parseInt(positionH + positionH2) + i).get(Integer.parseInt(positionV)).isHasShip()){
                                        checkState = true;
                                        System.out.println("A boat is already in this position");
                                    }
                                    if (!checkState){
                                        boatCheck =- 1;
                                        displayShipToConsole(boat);
                                        for (int j = 0; j < boat.getBoatSize(); j++){
                                            boardGrid.get(Integer.parseInt(positionH + positionH2) + j).get(Integer.parseInt(positionV)).state = 'S';
                                            boardGrid.get(Integer.parseInt(positionH + positionH2) + j).get(Integer.parseInt(positionV)).setHasShip(true);
                                            boat.coordinatesOnBoard.add(positionH + positionH2 + positionV);
                                            boat.setPlaced(true);
                                        }
                                    }
                                }
                            }
                        }
                    }

                }else {
                    System.out.println("ERROR! Incorrect input for co-ordinates");
                }

                if (isValid == 2){
                    boolean checkState = false;
                    id = Character.toString(userChoice.charAt(0));
                    Boat boat = boats.get(Integer.parseInt(id));
                    positionH = Character.toString(userChoice.charAt(1));
                    positionV = Character.toString(userChoice.charAt(2));
                    positionV2 = Character.toString(userChoice.charAt(3));
                    alignment = Character.toString(userChoice.charAt(4));
                    if (!boat.getPlaced()){
                        positionH = horizontalToDigitConversion(positionH);
                        if (alignment.equals("H")){
                            if (Integer.parseInt(positionV) + boat.getBoatSize() > boardSize){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }else {
                                for (int i = 0; i < boat.getBoatSize(); i++) {
                                    if (boardGrid.get(Integer.parseInt(positionV + positionV2)).get(Integer.parseInt(positionH) + i).isHasShip()) {
                                        checkState = true;
                                        System.out.println("A boat is already in this position");
                                    }
                                }
                                if (!checkState){
                                    boatCheck =- 1;
                                    displayShipToConsole(boat);
                                    for (int j = 0; j < boat.getBoatSize(); j++){
                                        boardGrid.get(Integer.parseInt(positionV + positionV2)).get(Integer.parseInt(positionH) + j).state = 'S';
                                        boardGrid.get(Integer.parseInt(positionV + positionV2)).get(Integer.parseInt(positionH) + j).setHasShip(true);
                                        boat.coordinatesOnBoard.add(positionH + positionV + positionV2);
                                        boat.setPlaced(true);
                                    }
                                }
                            }
                        }
                        if (alignment.equals("V")){
                            if (Integer.parseInt((positionH)) + boat.getBoatSize() > boardSize){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }else {
                                for (int i = 0; i < boat.getBoatSize(); i++) {
                                    if (boardGrid.get(Integer.parseInt(positionH)).get(Integer.parseInt(positionV + positionV2) + i).isHasShip()) {
                                        checkState = true;
                                        System.out.println("A boat is already in this position");
                                    }
                                }
                                if (!checkState){
                                    boatCheck =- 1;
                                    displayShipToConsole(boat);
                                    for (int j = 0; j < boat.getBoatSize(); j++){
                                        boardGrid.get(Integer.parseInt(positionV + positionV2) + j).get(Integer.parseInt(positionH)).state = 'S';
                                        boardGrid.get(Integer.parseInt(positionV + positionV2) + j).get(Integer.parseInt(positionH)).setHasShip(true);
                                        boat.coordinatesOnBoard.add(positionH + positionV + positionV2);
                                        boat.setPlaced(true);
                                    }
                                }
                            }
                        }
                    }

                }else {
                    System.out.println("ERROR! Incorrect input for co-ordinates");
                }

            }
            if (userChoice.length() == 6){
                boolean isValid = new Checker().coordinatesCheckFor6(userChoice);
                if (isValid){
                    boolean checkState = false;
                    id = Character.toString(userChoice.charAt(0));
                    Boat boat = boats.get(Integer.parseInt(id));
                    positionH = Character.toString(userChoice.charAt(1));
                    positionH2 = Character.toString(userChoice.charAt(2));
                    positionV = Character.toString(userChoice.charAt(3));
                    positionV2 = Character.toString(userChoice.charAt(4));
                    alignment = Character.toString(userChoice.charAt(5));

                    if (!boat.getPlaced()){
                        positionH = horizontalToDigitConversion(positionH);
                        positionH2 = horizontalToDigitConversion(positionH2);
                        if (alignment.equals("H")){
                            if (Integer.parseInt(positionV + positionV2) + boat.getBoatSize() > boardSize){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }else {
                                for (int i = 0; i < boat.getBoatSize(); i++) {
                                    if (boardGrid.get(Integer.parseInt(positionV + positionV2)).get(Integer.parseInt(positionH + positionH2) + i).isHasShip()) {
                                        checkState = true;
                                        System.out.println("A boat is already in this position");
                                    }
                                }
                                if (!checkState){
                                    boatCheck =- 1;
                                    displayShipToConsole(boat);
                                    for (int j = 0; j < boat.getBoatSize(); j++){
                                        boardGrid.get(Integer.parseInt(positionV + positionV2)).get(Integer.parseInt(positionH + positionH2) + j).state = 'S';
                                        boardGrid.get(Integer.parseInt(positionV + positionV2)).get(Integer.parseInt(positionV + positionV2) + j).setHasShip(true);
                                        boat.coordinatesOnBoard.add(positionH + positionH2 + positionV + positionV2);
                                        boat.setPlaced(true);
                                    }
                                }
                            }
                        }
                        if (alignment.equals("V")){
                            if (Integer.parseInt((positionH + positionH2)) + boat.getBoatSize() > boardSize){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            }else {
                                for (int i = 0; i < boat.getBoatSize(); i++) {
                                    if (boardGrid.get(Integer.parseInt(positionH + positionH2)).get(Integer.parseInt(positionV + positionV2) + i).isHasShip()) {
                                        checkState = true;
                                        System.out.println("A boat is already in this position");
                                    }
                                }
                                if (!checkState){
                                    boatCheck =- 1;
                                    displayShipToConsole(boat);
                                    for (int j = 0; j < boat.getBoatSize(); j++){
                                        boardGrid.get(Integer.parseInt(positionV + positionV2) + j).get(Integer.parseInt(positionH + positionH2)).state = 'S';
                                        boardGrid.get(Integer.parseInt(positionV + positionV2) + j).get(Integer.parseInt(positionH + positionH2)).setHasShip(true);
                                        boat.coordinatesOnBoard.add(positionH + positionH2 + positionV + positionV2);
                                        boat.setPlaced(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // for 5 characters A11

        }while(boatCheck > 0);



    }

    public void autoPlaceShip(){

    }


    private String horizontalToDigitConversion(String toConvert){
        for (int i = 0; i < horizontalBoarder.size();i++){
            if (toConvert.equals(horizontalBoarder.get(i))){
                return verticalBoarder.get(i);
            }
        }
        return null;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public List<List<Tile>> getBoardGrid() {
        return boardGrid;
    }

    public void setBoardGrid(List<List<Tile>> boardGrid) {
        this.boardGrid = boardGrid;
    }

    public List<Boat> getBoats() {
        return boats;
    }

    public void setBoats(List<Boat> boats) {
        this.boats = boats;
    }
}
