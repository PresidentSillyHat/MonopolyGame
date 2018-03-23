package esof322.pa4.team5;
/**
 *
 * @author Derek Wallace, Garret Gershmel
 */
import java.io.IOException;
import java.util.Scanner;
import java.util.*;
import javax.swing.*;


public class Player {
    private Scanner scan = new Scanner(System.in);

    public String Name; //for player reference
    public String Token;    //for player representation
    public int Position;    //board will be stored as an array
    public int money;    //assume whole bills and no cents
    public int friendlyRR;	//Number of rr owned
    public boolean stillPlaying;   //if player isn't bankrupt
    public boolean testPlayer;	//so game doesn't run in entirety each test method
    public boolean hasGetOut = false;	//get of jail free

    private Board gameBoard;
    private int jailEscapeAttempts = 0;
    private ArrayList<Property> myProperties = new ArrayList<>();
    public boolean inJail; //if player is in jail
    private boolean bidStatus = true;

    public static Player current;

    public Player(String myName, String myToken, Board gameBoard, int number, boolean forTesting){
        money=1500;     //rules state players start with $1500. Two 500s, four 100s, etc
        Name=myName;
        Token=myToken;
        Position=0; //start on Go, 40 in total
        inJail=false;
        stillPlaying=true;
        friendlyRR=0;
        this.gameBoard = gameBoard;
        testPlayer=forTesting;
        if(forTesting==false){
            runSetup(number);
        }

    }
    public Player(String tempName){
        Name="Bank";    //temporary owner setup for not owned properties, causes problems in testing
    }

    public int rollDice(int rollNumber){
        int dice1=(int)(Math.random()*6+1);
        int dice2=(int)(Math.random()*6+1);
        if(rollNumber>2)
            return 0;      //to indicate 3 doubles were rolled, go to jail
        if(dice1==dice2){
            rollNumber++;   //rollNumber is passed to rollDice to see number of prev doubles
            return (-2)*dice1;  //negatives are double
        }
        //TODO: display dice roll on board
        return dice1+dice2;
    }
    public int runTurn() throws IOException{	//runs a turn

        JOptionPane.showMessageDialog(null,  Name+" is starting their turn","Start of turn", JOptionPane.INFORMATION_MESSAGE);	//change JOption.info
        current=this;

        if (!inJail || jailEscapeAttempts>2) {	//if in jail and its time to leave
            if (jailEscapeAttempts>2){
                money-=50;
                Position = 10;
                inJail=false;
            }
            int currRollNum = 0;
            boolean loop = true;
            while (loop) {
                loop = startRolling(currRollNum);
                currRollNum++;
            }
        } else {
            int choiceG = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
                    Name+" would you like to pay $50 to leave jail?",
                    "The rich don't go to jail",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);	//change info message

            if (choiceG==0){
                money-=50;
				Position = 10;
                inJail=false;
            } else {
                if (hasGetOut){
                    startRolling(-1);
                    inJail = false;
					Position = 10;
                    hasGetOut = false;
                }
                if (attemptJailEscape() < 0) {
                    inJail=false;
					Position = 10;
                    startRolling(-1);
                }
            }
        }
        

        System.out.println(Name + " has ended their turn\n\n");
        JOptionPane.showMessageDialog(null,  Name+" has ended their turn","Turn Ended", JOptionPane.INFORMATION_MESSAGE);
        return  Position;
    }
    public void pay(int amount, Player other){	//Pay to who needs paying
        other.money=other.money+amount;
        money=money-amount;
    }
    public int bid(int maxbid, Space prop){	//Bidding for auction
        int bid;
        String dialogMessage = Name + ", how much would you like to bid for " + prop.name + "?\nCurrent bid is " + maxbid;
        while (true) {
            UIManager.put("OptionPane.cancelButtonText", "Leave Auction");	//This changes cancel button!!
            UIManager.put("OptionPane.okButtonText", "Set Bid");			//This changes ok button!!
            String bidFromGUI = (String)JOptionPane.showInputDialog(JOptionPane.getRootFrame(), dialogMessage);

            if (bidFromGUI!=null) {
                int bidInt = Integer.parseInt(bidFromGUI);
                if (bidInt <= money && bidInt > maxbid) {
                    return bidInt;
                } else if (bidInt>money){
                    dialogMessage = "You only have $" + money;
                } else {
                    dialogMessage = "Must be more than the max bid of " + maxbid;
                }
            } else {
                bidStatus = false;
                return -1;
            }



        }
    }
    public void buySpace(Space title){	//Buy the space

        if(money>title.value){	//make special case for auctions
            money=money-title.value;
        }
        else{
            System.out.println("Error 1");
            return;
        }
        title.deeded=this;	//property now owned by player
        System.out.println("You bought it and have $" + money + " remaining");
        JOptionPane.showMessageDialog(null,  Name+" bought it and has $"+ money + " remaining","Congratulations", JOptionPane.INFORMATION_MESSAGE);
        if(title.getType()==1){
            myProperties.add(gameBoard.getProperty(current.Position));
        }

        else if (title.getType()==3){	//if its a railroad, increase number of railroads owned
            friendlyRR++;
        }
    }
    public void buySpace(Space title,int auctionedPrice){	//Buy the space

        if(money>auctionedPrice){	//make special case for auctions
            money=money-auctionedPrice;
        }
        else{
            System.out.println("Error 1");
            return;
        }
        title.deeded=this;	//property now owned by player
        System.out.println("You bought it and have $" + money + " remaining");
        JOptionPane.showMessageDialog(null,  Name+" bought it and has $"+ money + " remaining","Congratulations", JOptionPane.INFORMATION_MESSAGE);
        if(title.getType()==1){
            myProperties.add(gameBoard.getProperty(current.Position));
        }

        else if (title.getType()==3){	//if its a railroad, increase number of railroads owned
            friendlyRR++;
        }
    }
    public boolean stillBidding() {	//if still bidding
        return stillPlaying && bidStatus;
    }
    public void newBid(){	//reset bidding process
        bidStatus=true;
    }

    private boolean startRolling(int currRollNum) throws IOException {
		UIManager.put("OptionPane.okButtonText", "Okay");	//Set to normal again
		UIManager.put("OptionPane.cancelButtonText", "END GAME");			
        boolean retVal = false;
        int myRoll = rollDice(currRollNum);
        if (myRoll < 0) {
            retVal = true;
            System.out.println("You rolled doubles!");
            JOptionPane.showMessageDialog(null,  "You rolled doubles!","Doubles", JOptionPane.INFORMATION_MESSAGE);
        }
        myRoll = Math.abs(myRoll);
        System.out.println("You rolled " + myRoll);
        if (currRollNum > 2) {
            inJail = true;
            Position = -1;
            System.out.println("You rolled doubles 3 times and have been sent to jail!");
            JOptionPane.showMessageDialog(null,  "You rolled doubles 3 times and have been sent to jail!","STOP!", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        Position += myRoll;

        if (Position>39){	//If you passed go
            money+=200;
        }
        Position = Position % 40;

        if (Position==0){
            improvements();
        }
        drawnGUI.updateBoard(gameBoard);	//update to new position
        Space landedSpace = gameBoard.getSpace(Position);
        System.out.println("You landed on " + landedSpace.name + " at " + Position);
        if (landedSpace.getType() == Space.PROPERTY || landedSpace.getType() == Space.UTILITY || landedSpace.getType() == Space.RAILROAD) {
            if (landedSpace.deeded.Name.equals("Bank")) {
               
                int choiceG = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
                        this.Name+", would you like to buy "+landedSpace.name+"? You have $"+money+" and it costs $"+landedSpace.value,
                        "Buy Property?",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);
                
                if (choiceG == 0) { //0 seems to be Yes, 1 is no, etc
                    attemptBuy(landedSpace);
                } 
                else if (choiceG==1){
                    gameBoard.auction(landedSpace);
                }
                else{   //Lets player shut down game
                    System.exit(0);
                }
            } else {
                if (landedSpace.deeded != this) {
                    attemptPay(landedSpace.landedAction(myRoll), landedSpace.deeded);
                }
            }
            improvements();
			//scenarios for different spaces
        } else if (landedSpace.getType() == Space.COMMUNITY_CHEST){
            String message = gameBoard.pullCC(this);
            JOptionPane.showMessageDialog(null,  message,"Community Chest", JOptionPane.INFORMATION_MESSAGE);
            improvements();
            return retVal;
        } else if (landedSpace.getType() == Space.CHANCE){
            String message = gameBoard.pullChance(this);
            JOptionPane.showMessageDialog(null,  message,"Chance", JOptionPane.INFORMATION_MESSAGE);
            improvements();
            return retVal;
        } else if (landedSpace.getType() == Space.INCOME_TAX){
            payBank(true);
            improvements();
            return retVal;
        } else if (landedSpace.getType() == Space.GO_TO_JAIL){
            inJail=true;
            Position = -1;
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "You landed on Go To Jail!");
            return false;
        } else if (landedSpace.getType() == Space.LUXURY_TAX){
            payBank(false);
            improvements();
            return retVal;
        }
        return retVal;
    }
    private void improvements(){	//Ask plauers if they want to make improvements and to what
        int choiceG = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
                this.Name+", would you like to make any improvements on your properties?",
                "Continue turn?",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        if (choiceG==0){
            System.out.println("You're properties are: "); //TODO  this should be displayed in the players GUI
            Object[] possibilities = new Object[myProperties.size()+1];
            for (int i=0;i<myProperties.size();i++  ){
                Property property=myProperties.get(i);
                String houseAsString;
                if (property.getHouses() == 5){
                    houseAsString = " 1 Hotel";
                } else {
                    houseAsString = property.getHouses() + " houses";
                }
                possibilities[i]=(property.name + " with " + houseAsString);
            }
            possibilities[myProperties.size()]="Quit";

            while (true) {
                //System.out.println("Please enter the property's name you would like to improve (q to quit)"); //TODO they should be able to click on a property name from their list or select end turn which will no longer be grayed out
                //String response = scan.next();
                if(myProperties.size()>0){
                    String response=""+JOptionPane.showInputDialog(JOptionPane.getRootFrame(),
                            "Please select property",
                            "You own these",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            possibilities,
                            possibilities[0]);


                    if (response.equals("Quit")){
                        return;
                    }
                    else {//property options
                        for (Property property : myProperties) {
                            if (response.toLowerCase().contentEquals(property.name.toLowerCase()) && gameBoard.isMonopolized(this, property)) {
                                for (Property otherOfColor : myProperties) {
                                    if (otherOfColor.color == property.color && (property.getHouses() - otherOfColor.getHouses() < 1)) {
                                        System.out.println("Confirm improvement here for $" + property.buildCost);
                                        choiceG = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
                                                "Confirm improvement here for $" + property.buildCost,
                                                "Confirm Build?",
                                                JOptionPane.YES_NO_CANCEL_OPTION,
                                                JOptionPane.INFORMATION_MESSAGE);
                                        if (choiceG==0){//scan.next().contains("y")){
                                            buildHouse(property);
                                        }
                                    }
                                    else {
                                        
                                        JOptionPane.showMessageDialog(null,  "You must improve other properties in that group first","Not enough houses", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                }
                            } else {
                                
                                JOptionPane.showMessageDialog(null,  "You do not have a viable monopoly on this property","Not a monopoly", JOptionPane.INFORMATION_MESSAGE);

                            }
                        }
                    }

                }
                else {	//Homeless player response
                    JOptionPane.showMessageDialog(null,  "You can't build on nothing","You own nothing", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

        }
        else if(choiceG==2){System.exit(0);}
    }
    public void attemptBuy(Space title){	//check if player has enough money before buying
        if (money>title.value){

            buySpace(title);
        }
        else {	//failed to buy, send to auction
            gameBoard.auction(title);
        }
    }
    public void attemptPay(int cost, Player toPlayer){	//force mortgage if player cant pay
        if (cost<money){
            pay(cost, toPlayer);
            return;
        } else {
            System.out.println("You own the following properties: ");
            if (myProperties.size()>0) {
                for (Property myProperty : myProperties) {
                    System.out.println(myProperty.name + " with mortgage value of " + myProperty.getMortgageValue());
                }
                System.out.println("Type the name of the property you wish to mortgage: ");
                String mortgageProp = scan.next();
                for (Property myProperty : myProperties) {
                    if (mortgageProp.contentEquals(myProperty.name)) {
                        mortgageProperty(myProperty);
                    }
                }
            }
        }
        attemptPay(cost, toPlayer);
    }
    public void payBank(boolean isIncome){	//Taxes
        if (isIncome){
            int tenPercent = (int) (money*0.1);
            if (tenPercent<200){
                money-=tenPercent;
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "You had to pay $" + tenPercent + " for income tax. You now have " + money);
            } else {
                money-=200;
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "You had to pay $200 for income tax. You now have " + money);
            }
        } else {
            money-=100;
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "You had to pay $100 for income tax. You now have " + money);
        }
    }
    private void runSetup(int number){
        Name = (String)JOptionPane.showInputDialog(JOptionPane.getRootFrame(), "Player " + number + ", Please enter your name", "Name entry", JOptionPane.QUESTION_MESSAGE);

        String availableTokens[] = gameBoard.getAvailableTokens(); //TODO make this gray out token buttons
        Token = (String) JOptionPane.showInputDialog(JOptionPane.getRootFrame(),
                "Please choose a token",
                "Token Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                availableTokens,
                2);
        gameBoard.removeToken(Token);
    }
    private void mortgageProperty(Property title){	//mortgage property for money
        title.mortgage();
        money=money+title.getMortgageValue();
    }
    public boolean buildHouse(Property title){	//build house if possible
        if (gameBoard.isMonopolized(this, title) && !title.mortgageState && title.getHouses()<4){
            if(money>title.buildCost){
                money=money-title.buildCost;
            }
            else{
                return false;
            }
            title.buildHouse();
            return true;
        }
        System.out.println("Requirements not met");
        return false;
    }
    private int attemptJailEscape(){	//primary call when in jail
        jailEscapeAttempts++;
        int rollVal = rollDice(0);
        if (rollVal < 0)  {
            return rollVal;
        }
        else {
            return 0;
        }
    }
    private void unMortgageProperty(Property title){	//get property back
        title.unMortgage();
        money=money-title.value-(int)(title.value*.1);
    }

    public void payForHouses(){	//pay for houses attempted to build
        for (Property property:myProperties){
            if (property.getHouses()>0){
                money-=property.getHouses()*40;
            }
        }
    }
}
