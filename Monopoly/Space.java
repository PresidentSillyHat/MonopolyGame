package esof322.pa4.team5;

/**
 *
 * @author Derek Wallace, Garret Gershmel
 */
public class Space {
    public final static int PROPERTY = 1;
    public final static int UTILITY = 2;
    public final static int RAILROAD = 3;
    public final static int FREESPACE = 4;
    public final static int COMMUNITY_CHEST = 5;
    public final static int CHANCE = 6;
    public final static int INCOME_TAX = 7;
    public final static int GO_TO_JAIL = 8;
    public final static int LUXURY_TAX = 9;

    public String name;
    public Player deeded=new Player("None");
    public int value;
    

    private int spaceType; //1=property, 2=utility, 3=railroad, 4=freebie, 5=CC, 6=C, 7=Income tax

    public Space(){

    }
    public void setSpace(String n, int s){
        name=n;
        spaceType=s;
    }

    public int getType(){
        return spaceType;
    }
    public int landedAction(int roll){
        System.out.println("Free space");
        return 0;
    }

}
