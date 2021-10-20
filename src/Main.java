import model.Board;
import view.Menu;
import utils.FileReader;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

        FileReader reader = new FileReader();
        reader.readFile();
        Board board = new Board();
        board.setBoardDimensions();
        board.populateBoard();


        Menu menu = new Menu();
        menu.displayMenu();
        menu.userGameModeChoice();
        board.renderBoard();




    }


}
