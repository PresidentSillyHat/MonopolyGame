package esof322.pa4.team5;

/**
 * Created by Garret Gershmel on 11/11/2017.
 */
public class UtilitySpace extends Property {
    private Board gameBoard;

    public UtilitySpace(int space, int[] rents, int groups, int buildC, Board gb){
        super (space, rents, groups, buildC);
        gameBoard=gb;
    }

    public int landedAction(int roll){
        if (deeded == gameBoard.getOtherUtilOwner(this)) {
            return roll * 10;
        }
        return roll*4;
    }
    public int getMortgageValue(){
        return 75;
    }
    public int getHouses(){
        return 0;
    }
}
