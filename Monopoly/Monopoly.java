package esof322.pa4.team5;

import java.io.IOException;

/**
 *
 * @author Derek Wallace
 */
public class Monopoly {

    public static void main(String[] args) throws IOException {

        drawnGUI.getInstance();
        Board game=new Board();
        game.getStartingPositions();
        //drawnGUI.setBoard(game);
        //MonopolyGui.getInstance();
        //drawnGUI.getInstance();
        game.runGame();
    }

}
