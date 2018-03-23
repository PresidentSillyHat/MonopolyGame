package esof322.pa4.team5;

/**
 *
 * @author Derek Wallace, Garret Gershmel
 */
public class Property extends Space {
    public boolean mortgageState;	
    public int buildCost;
    public int color;
    public Player owner;

    private int[] rentCosts;
    private int houses = 0;

    public Property(int price, int[]rents, int group, int buildC){
        value=price;
        color=group;
        rentCosts=new int[rents.length];
        for (int i=0;i<rents.length;i++){
            rentCosts[i]=rents[i];
        }
        mortgageState=false;
        houses=0;
        buildCost=buildC;
    }

    public void mortgage(){	//mortgage
        mortgageState=true;
    }
    public void unMortgage(){	//unMortgage
        mortgageState=false;
    }
    public int getHouses(){	//get number of built houses on property
        return houses;
    }
    public void buildHouse(){	//Build a house upon this property
        houses++;
    }
    public int landedAction(int roll){	//player landed on this property
        return rentCosts[houses];
    }
    public int getMortgageValue(){
        return value/2;
    }
    public int getGroup(){	//color of group its in, helpful for finding if monopolized
        return color;
    }
}
