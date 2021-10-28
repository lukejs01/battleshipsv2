package view;

import model.Board;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu {
    public Menu() {
    }



    public void displayMenu(){
        System.out.println("Welcome to battleships!");
        System.out.println("Please choose a mode you would like to play: ");
        System.out.println("1. Player Vs Computer (EASY)");
        System.out.println("2. Player VS Computer (HARD)");
        System.out.println("3. Player VS Player (EASY)");
        System.out.println("4. Player Vs Player (HARD)");
    }


    public void userGameModeChoice(Board board) throws FileNotFoundException {
        boolean check = false;
        Scanner in = new Scanner(System.in);




        do {
            System.out.print("Enter your choice: ");
            int userChoice = in.nextInt();
            System.out.println();
            switch (userChoice){
                case 1:
                    System.out.println("You have chosen: Player Vs Computer (EASY)");
                    check = true;
                    playerVsComputer(board);
                    break;

                case 2:
                    System.out.println("You have chosen: Player Vs Computer (HARD)");
                    check = true;
                    //
                    break;

                case 3:
                    System.out.println("You have chosen: Player Vs Player (EASY)");
                    check = true;
                    //
                    break;

                case 4:
                    System.out.println("You have chosen: Player Vs Player (HARD)");
                    check = true;
                    //
                    break;

                default:
                    System.out.println("ERROR! Please enter a correct number");
                    break;
            }
        }while (!check);

    }


    private void inGameChoice(Board board, Board oppBoard) throws FileNotFoundException {
        boolean check = false;
        Scanner in = new Scanner(System.in);



        System.out.println("1. Auto place all ships");
        System.out.println("2. Place your own ships");
        System.out.print("Enter your how you would like to place the ships: " );
        int userChoice = in.nextInt();

        do {
            switch (userChoice){
                case 1:
                    System.out.println();
                    System.out.println("You have selected to auto place ships");
                    System.out.println("Loading...");
                    board.autoPlaceShip();

                    do {
                        oppBoard.renderBoard(false);

                        if (oppBoard.shootAtShip()){
                            board.autoShootShip();
                            board.renderBoard(true);

                            break;
                        }

                    }while (board.getInGameBoats().size() > 0 && oppBoard.getInGameBoats().size() > 0);
                    check = true;
                    break;

                case 2:
                    System.out.println();
                    System.out.println("You have selected to place your own ships...");
                    board.placeShip(false);
                    check = true;
                    break;

                default:
                    System.out.println("ERROR! Please enter a correct number");
                    break;
            }

        }while (!check);
    }

    private void playerVsComputer(Board board) throws FileNotFoundException {
        Board oppBoard = new Board();
        oppBoard.setBoardDimensions();
        oppBoard.populateBoard();
        oppBoard.autoPlaceShip();
        inGameChoice(board, oppBoard);
    }
}
