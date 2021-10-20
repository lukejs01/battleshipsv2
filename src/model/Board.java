package model;


import utils.FileHelper;

import java.io.FileNotFoundException;
import java.util.*;

public class Board {

    private int boardSize = 0;
    private int cellSize = 0;
    private final int DEFAULT_BOARD_SIZE = 5;
    private final int DEFAULT_CELL_SIZE = 2;


    List<List<Tile>> board = new ArrayList<>();
    List<Boat> boats = new FileHelper().readBoatToList();

    private List<String> horizontalBoarder = new ArrayList<>(Arrays.asList("A","B","C","D","E","F","G","H","I","J"));
    private List<String> verticalBoarder = new ArrayList<>(Arrays.asList("0","1","2","3","4","5","6","7","8","9"));

    public Board() throws FileNotFoundException {
    }

    public void populateBoard() {
        List<Tile> row = new ArrayList<>();
        for (int i = 0; i < boardSize; i++){
            row.add(new Tile());

        }
        for (int j = 0; j < boardSize; j++){

            board.add(row);
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
                        int firstIndex = (verticalBoarderCounter/10)-1;
                        int secondIndex = verticalBoarderCounter%10;
                        System.out.print("| " + verticalBoarder.get(firstIndex) + verticalBoarder.get(secondIndex));
                    }

                    verticalBoarderCounter++;
                } else if (k % cellSize == 0 && i % cellSize != 0) {
                    c = '|';
                } else if (i % cellSize == 0 || k % cellSize == 0) {
                    c = '-';
                }else {
                    c = board.get(i / 2).get(k / 2).state;
                }
                System.out.print(c);
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
    }

    private void displayShipToConsole(Boat boatToRemove){
        System.out.println("ID   Boat Name             Length ");

        List<Boat> displayBoats = boats;
        for (Boat toCheck: displayBoats) {
            if (toCheck.equals(boatToRemove)){
                displayBoats.remove(boatToRemove);
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
        String positionV = "";
        String alignment = "";
        int boatCheck = boats.size();



        do {
            displayShipToConsole(new Boat());
            System.out.println("Choose a ship id and where you for like to place it(e.g 1 A3 V)");
            String userChoice = sc.nextLine().toUpperCase(Locale.ROOT).replaceAll("\\s+","");

            if (userChoice.length() >= 4 && userChoice.length() <= 6){

                if (userChoice.equals("%s%s%s%s")){
                    boolean checkState = false;


                    id = Character.toString(userChoice.charAt(0));
                    Boat boat = boats.get(Integer.parseInt(id));
                    positionH = Character.toString(userChoice.charAt(1));
                    positionV = Character.toString(userChoice.charAt(2));
                    alignment = Character.toString(userChoice.charAt(3));

                    if (boat.getPlaced() == false){
                        positionH = horizontalToDigitConversion(positionH);
                        if (alignment.equals("H")){
                            if (Integer.parseInt(positionH) + boat.getBoatSize() > boardSize){
                                System.out.println("Ship can't be placed there, try another co-ordinate!");
                            } else {
                                for (int i = 0; i < boat.getBoatSize(); i++) {
                                    if (board.get(Integer.parseInt(positionH + i)).get(Integer.parseInt(positionV)).isHasShip()) {
                                        checkState = true;
                                        System.out.println("A boat is already in this position");
                                    }

                                    if (!checkState) {
                                        for (int j = 0; j < boat.getBoatSize(); j++) {
                                            board.get(Integer.parseInt(positionH + j)).get(Integer.parseInt(positionV)).state = 'S';
                                            board.get(Integer.parseInt(positionH + j)).get(Integer.parseInt(positionV)).setHasShip(true);
                                            boat.coordinatesOnBoard.add(positionH + positionV);
                                            boat.setPlaced(true);
                                            boatCheck =- 1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (alignment.equals("V")){
                        if (Integer.parseInt(positionV) + boat.getBoatSize() > boardSize){
                            System.out.println("Ship can't be placed there, try another co-ordinate!");

                        }else {
                            for (int i = 0; i < boat.getBoatSize(); i++){
                                if (board.get(Integer.parseInt(positionH)).get(Integer.parseInt(positionV + i)).isHasShip()){
                                    checkState = true;
                                    System.out.println("A boat is already in this position");
                                }

                                if (!checkState){
                                    for (int j = 0; j < boat.getBoatSize(); j++) {
                                        board.get(Integer.parseInt(positionH)).get(Integer.parseInt(positionV +j)).state = 'S';
                                        board.get(Integer.parseInt(positionH)).get(Integer.parseInt(positionV + j)).setHasShip(true);
                                        boat.coordinatesOnBoard.add(positionH + positionV);
                                        boat.setPlaced(true);
                                        boatCheck =- 1;
                                    }
                                }
                            }
                        }
                    }

                }
            }else{
                System.out.println("ERROR! Incorrect input for co-ordinates");
            }

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

    public int getBoardWidth() {
        return boardSize;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardSize = boardWidth;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }
}
