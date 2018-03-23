package esof322.pa4.team5;

/**
 *
 * @author Derek Wallace, Garret Gershmel
 */
public class Railroad extends Space{
    public boolean mortgageState;
    public int[] rentCosts;

    public Railroad(int price, int[]rents,int mv){
        value=price;
        rentCosts=new int[]{rents[0],rents[1],rents[2],rents[3]};
        mortgageState=false;
    }

    public int getMortgageValue(){
        return value/2;
    }
    public int landedAction(int roll){	//find the cost for a railroad
        if(mortgageState==true){
            return 0;
        }
        else if(deeded.friendlyRR==1){
            return rentCosts[0];
        }
        else if(deeded.friendlyRR==2){
            return rentCosts[1];
        }
        else if(deeded.friendlyRR==3){
            return rentCosts[2];
        }
        else{
            return rentCosts[3];
        }
    }
}
